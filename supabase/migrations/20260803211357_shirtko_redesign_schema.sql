ALTER TABLE orders ADD COLUMN IF NOT EXISTS payment_method TEXT CHECK (payment_method IS NULL OR payment_method IN ('gcash', 'cod'));

CREATE TABLE IF NOT EXISTS product_price_overrides (
  slug TEXT PRIMARY KEY,
  price_php NUMERIC NOT NULL CHECK (price_php > 0),
  stock_status TEXT NOT NULL DEFAULT 'available' CHECK (stock_status IN ('available', 'unavailable')),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_by UUID REFERENCES profiles(id) ON DELETE SET NULL
);

ALTER TABLE product_price_overrides ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Anyone can read price overrides" ON product_price_overrides FOR SELECT USING (true);
CREATE POLICY "Admins manage price overrides" ON product_price_overrides FOR ALL USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin') WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE TABLE IF NOT EXISTS store_settings (
  key TEXT PRIMARY KEY,
  value TEXT NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE store_settings ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Anyone can read store settings" ON store_settings FOR SELECT USING (true);
CREATE POLICY "Admins manage store settings" ON store_settings FOR ALL USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin') WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

INSERT INTO store_settings (key, value) VALUES ('shipping_flat_fee', '80'), ('gcash_number', '0966 774 0292'), ('gcash_name', 'ShirtKo Apparel Store') ON CONFLICT (key) DO NOTHING;

CREATE TABLE IF NOT EXISTS custom_print_requests (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  shopper_id UUID REFERENCES profiles(id) ON DELETE SET NULL,
  contact_name TEXT NOT NULL,
  contact_phone TEXT NOT NULL,
  contact_email TEXT,
  garment_type TEXT NOT NULL,
  garment_color TEXT NOT NULL,
  placement TEXT NOT NULL,
  size_breakdown JSONB NOT NULL DEFAULT '{}',
  quantity_total INTEGER NOT NULL DEFAULT 1,
  notes TEXT,
  artwork_path TEXT,
  status TEXT NOT NULL DEFAULT 'new' CHECK (status IN ('new', 'quoted', 'approved', 'in_production', 'done', 'cancelled')),
  quote_php NUMERIC,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_custom_print_shopper ON custom_print_requests(shopper_id);
CREATE INDEX IF NOT EXISTS idx_custom_print_status ON custom_print_requests(status);

ALTER TABLE custom_print_requests ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Anyone can create custom print requests" ON custom_print_requests FOR INSERT WITH CHECK (true);
CREATE POLICY "Users read own custom print requests" ON custom_print_requests FOR SELECT USING (shopper_id = auth.uid() OR (SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins update custom print requests" ON custom_print_requests FOR UPDATE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins delete custom print requests" ON custom_print_requests FOR DELETE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE OR REPLACE FUNCTION public.track_order(p_order_id text, p_phone text)
RETURNS jsonb LANGUAGE plpgsql SECURITY DEFINER SET search_path = public AS $$
DECLARE
  v_order orders%ROWTYPE;
  v_items jsonb;
  v_normalized_phone text;
  v_uuid uuid;
BEGIN
  BEGIN v_uuid := p_order_id::uuid; EXCEPTION WHEN others THEN RETURN jsonb_build_object('ok', false, 'error', 'invalid_id'); END;
  v_normalized_phone := regexp_replace(trim(p_phone), '\s+', '', 'g');
  SELECT * INTO v_order FROM orders WHERE id = v_uuid;
  IF NOT FOUND THEN RETURN jsonb_build_object('ok', false, 'error', 'not_found'); END IF;
  IF v_order.phone IS DISTINCT FROM v_normalized_phone AND regexp_replace(v_order.phone, '\D', '', 'g') IS DISTINCT FROM regexp_replace(v_normalized_phone, '\D', '', 'g') THEN
    RETURN jsonb_build_object('ok', false, 'error', 'phone_mismatch');
  END IF;
  SELECT coalesce(jsonb_agg(to_jsonb(oi)), '[]'::jsonb) INTO v_items FROM order_items oi WHERE oi.order_id = v_order.id;
  RETURN jsonb_build_object('ok', true, 'order', jsonb_build_object('id', v_order.id, 'order_name', v_order.order_name, 'order_status', v_order.order_status, 'fin_status', v_order.fin_status, 'total_php', v_order.total_php, 'amount_to_be_paid', v_order.amount_to_be_paid, 'shipping_fee', v_order.shipping_fee, 'payment_method', v_order.payment_method, 'created_at', v_order.created_at, 'items', v_items));
END;
$$;
REVOKE ALL ON FUNCTION public.track_order(text, text) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.track_order(text, text) TO anon, authenticated;;
