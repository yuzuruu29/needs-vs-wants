-- ShirtKo base schema (profiles, orders, coupons, RLS)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE profiles (
  id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  full_name TEXT,
  phone TEXT,
  email TEXT,
  role TEXT NOT NULL DEFAULT 'shopper' CHECK (role IN ('shopper', 'admin')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE OR REPLACE FUNCTION handle_new_user()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public
AS $$
BEGIN
  INSERT INTO public.profiles (id, email, full_name, phone)
  VALUES (
    NEW.id,
    NEW.email,
    COALESCE(NEW.raw_user_meta_data->>'full_name', NEW.raw_user_meta_data->>'name'),
    NEW.phone
  );
  RETURN NEW;
END;
$$;

REVOKE EXECUTE ON FUNCTION handle_new_user() FROM PUBLIC, anon, authenticated;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE FUNCTION handle_new_user();

CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  slug TEXT NOT NULL UNIQUE,
  name TEXT NOT NULL,
  eyebrow TEXT NOT NULL,
  description TEXT NOT NULL,
  story TEXT NOT NULL,
  image_url TEXT NOT NULL,
  image_alt TEXT NOT NULL,
  accent TEXT NOT NULL CHECK (accent IN ('pink', 'blue', 'mint', 'lavender', 'oat')),
  collection_slug TEXT NOT NULL,
  collection_name TEXT NOT NULL,
  fits JSONB NOT NULL DEFAULT '[]',
  colors JSONB NOT NULL DEFAULT '[]',
  design_options JSONB NOT NULL DEFAULT '[]',
  placement TEXT NOT NULL DEFAULT '',
  sku TEXT NOT NULL DEFAULT '',
  price_php NUMERIC,
  status TEXT NOT NULL DEFAULT 'inquiry' CHECK (status IN ('inquiry', 'published')),
  availability TEXT,
  lead_time TEXT,
  size_options JSONB,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE orders (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  shopper_id UUID REFERENCES profiles(id) ON DELETE SET NULL,
  brand TEXT NOT NULL DEFAULT 'ShirtKo',
  order_date DATE NOT NULL DEFAULT CURRENT_DATE,
  order_name TEXT NOT NULL,
  phone TEXT NOT NULL,
  shipping_street TEXT NOT NULL,
  province TEXT NOT NULL,
  city TEXT NOT NULL,
  total_items INTEGER NOT NULL DEFAULT 0,
  total_php NUMERIC NOT NULL DEFAULT 0,
  amount_to_be_paid NUMERIC NOT NULL DEFAULT 0,
  shipping_fee NUMERIC NOT NULL DEFAULT 0,
  fin_status TEXT NOT NULL DEFAULT 'pending' CHECK (fin_status IN ('paid', 'pending')),
  order_status TEXT NOT NULL DEFAULT 'new' CHECK (order_status IN ('new', 'processing', 'ready', 'completed', 'cancelled')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE order_items (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  lineitem_name TEXT NOT NULL,
  sku TEXT NOT NULL DEFAULT '',
  quantity INTEGER NOT NULL DEFAULT 1,
  unit_price NUMERIC NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);

CREATE TABLE coupons (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  code TEXT NOT NULL UNIQUE,
  redeemed BOOLEAN NOT NULL DEFAULT false,
  redeemed_at TIMESTAMPTZ,
  expires_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_coupons_order_id ON coupons(order_id);
CREATE INDEX idx_coupons_code ON coupons(code);

CREATE TABLE provinces_cities (
  id SERIAL PRIMARY KEY,
  province TEXT NOT NULL,
  city TEXT NOT NULL,
  area TEXT
);

CREATE INDEX idx_provinces_cities_province ON provinces_cities(province);
CREATE INDEX idx_provinces_cities_city ON provinces_cities(city);

ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE products ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE order_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE coupons ENABLE ROW LEVEL SECURITY;
ALTER TABLE provinces_cities ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can read own profile" ON profiles FOR SELECT USING (auth.uid() = id);
CREATE POLICY "Users can update own profile" ON profiles FOR UPDATE USING (auth.uid() = id);

CREATE POLICY "Anyone can read published products" ON products FOR SELECT USING (status = 'published');
CREATE POLICY "Admins can read all products" ON products FOR SELECT USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can insert products" ON products FOR INSERT WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can update products" ON products FOR UPDATE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can delete products" ON products FOR DELETE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE POLICY "Shoppers can read own orders" ON orders FOR SELECT USING (auth.uid() = shopper_id);
CREATE POLICY "Shoppers can create orders" ON orders FOR INSERT WITH CHECK (auth.uid() = shopper_id);
CREATE POLICY "Admins can read all orders" ON orders FOR SELECT USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can insert orders" ON orders FOR INSERT WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can update orders" ON orders FOR UPDATE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can delete orders" ON orders FOR DELETE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE POLICY "Shoppers can read own order items" ON order_items FOR SELECT USING (EXISTS (SELECT 1 FROM orders WHERE orders.id = order_items.order_id AND orders.shopper_id = auth.uid()));
CREATE POLICY "Shoppers can create order items" ON order_items FOR INSERT WITH CHECK (EXISTS (SELECT 1 FROM orders WHERE orders.id = order_items.order_id AND orders.shopper_id = auth.uid()));
CREATE POLICY "Admins can read all order items" ON order_items FOR SELECT USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can insert order items" ON order_items FOR INSERT WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can update order items" ON order_items FOR UPDATE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can delete order items" ON order_items FOR DELETE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE POLICY "Shoppers can read own coupons" ON coupons FOR SELECT USING (EXISTS (SELECT 1 FROM orders WHERE orders.id = coupons.order_id AND orders.shopper_id = auth.uid()));
CREATE POLICY "Admins can read all coupons" ON coupons FOR SELECT USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can insert coupons" ON coupons FOR INSERT WITH CHECK ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');
CREATE POLICY "Admins can update coupons" ON coupons FOR UPDATE USING ((SELECT role FROM profiles WHERE id = auth.uid()) = 'admin');

CREATE POLICY "Anyone can read provinces_cities" ON provinces_cities FOR SELECT USING (true);

CREATE INDEX idx_orders_shopper_id ON orders(shopper_id);
CREATE INDEX idx_orders_fin_status ON orders(fin_status);
CREATE INDEX idx_orders_order_status ON orders(order_status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_collection ON products(collection_slug);

CREATE OR REPLACE FUNCTION generate_coupon_code()
RETURNS TEXT LANGUAGE plpgsql SET search_path = public AS $$
DECLARE
  chars TEXT := 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  result TEXT := '';
  i INT;
BEGIN
  FOR i IN 1..8 LOOP
    result := result || substr(chars, floor(random() * length(chars) + 1)::int, 1);
  END LOOP;
  result := 'SHK' || result;
  RETURN result;
END;
$$;

CREATE OR REPLACE FUNCTION handle_fin_status_change()
RETURNS TRIGGER LANGUAGE plpgsql SECURITY DEFINER SET search_path = public AS $$
DECLARE
  existing_coupon RECORD;
  new_code TEXT;
BEGIN
  IF NEW.fin_status = 'paid' AND (OLD.fin_status IS NULL OR OLD.fin_status != 'paid') THEN
    SELECT * INTO existing_coupon FROM public.coupons WHERE order_id = NEW.id;
    IF existing_coupon.id IS NOT NULL THEN
      UPDATE public.coupons SET redeemed = false, redeemed_at = NULL WHERE id = existing_coupon.id;
    ELSE
      LOOP
        new_code := public.generate_coupon_code();
        BEGIN
          INSERT INTO public.coupons (order_id, code, expires_at) VALUES (NEW.id, new_code, NOW() + INTERVAL '30 days');
          EXIT;
        EXCEPTION WHEN unique_violation THEN NULL;
        END;
      END LOOP;
    END IF;
  END IF;
  RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS on_fin_status_change ON orders;
CREATE TRIGGER on_fin_status_change AFTER UPDATE ON orders FOR EACH ROW WHEN (NEW.fin_status IS DISTINCT FROM OLD.fin_status) EXECUTE FUNCTION handle_fin_status_change();
REVOKE EXECUTE ON FUNCTION handle_fin_status_change() FROM PUBLIC, anon, authenticated;

CREATE OR REPLACE FUNCTION public.claim_coupon(p_phone text, p_code text)
RETURNS jsonb LANGUAGE plpgsql SECURITY DEFINER SET search_path = public AS $$
DECLARE
  v_order orders%ROWTYPE;
  v_coupon coupons%ROWTYPE;
  v_normalized_phone text;
  v_normalized_code text;
BEGIN
  IF auth.uid() IS NULL THEN RETURN jsonb_build_object('ok', false, 'error', 'not_authenticated'); END IF;
  v_normalized_phone := regexp_replace(trim(p_phone), '\s+', '', 'g');
  v_normalized_code := upper(trim(p_code));
  SELECT * INTO v_coupon FROM coupons WHERE upper(code) = v_normalized_code LIMIT 1;
  IF NOT FOUND THEN RETURN jsonb_build_object('ok', false, 'error', 'invalid_code'); END IF;
  IF v_coupon.redeemed THEN RETURN jsonb_build_object('ok', false, 'error', 'already_redeemed'); END IF;
  IF v_coupon.expires_at IS NOT NULL AND v_coupon.expires_at < now() THEN RETURN jsonb_build_object('ok', false, 'error', 'expired'); END IF;
  SELECT * INTO v_order FROM orders WHERE id = v_coupon.order_id;
  IF NOT FOUND THEN RETURN jsonb_build_object('ok', false, 'error', 'order_missing'); END IF;
  IF v_order.phone IS DISTINCT FROM v_normalized_phone AND regexp_replace(v_order.phone, '\D', '', 'g') IS DISTINCT FROM regexp_replace(v_normalized_phone, '\D', '', 'g') THEN
    RETURN jsonb_build_object('ok', false, 'error', 'phone_mismatch');
  END IF;
  IF v_order.shopper_id IS NOT NULL AND v_order.shopper_id IS DISTINCT FROM auth.uid() THEN RETURN jsonb_build_object('ok', false, 'error', 'order_owned_by_other'); END IF;
  UPDATE orders SET shopper_id = auth.uid(), updated_at = now() WHERE id = v_order.id;
  UPDATE profiles SET phone = COALESCE(NULLIF(phone, ''), v_normalized_phone) WHERE id = auth.uid() AND (phone IS NULL OR phone = '');
  RETURN jsonb_build_object('ok', true, 'coupon', to_jsonb(v_coupon), 'order_id', v_order.id);
END;
$$;
REVOKE ALL ON FUNCTION public.claim_coupon(text, text) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.claim_coupon(text, text) TO authenticated;;
