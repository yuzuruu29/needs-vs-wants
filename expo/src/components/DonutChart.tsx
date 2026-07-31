/**
 * Hand-drawn SVG donut chart — faithful port of DonutChart.swift.
 *
 * Need (green) / Want (red), total centered. No third-party chart libs (D6).
 * Uses react-native-svg arcs. Empty state (total = 0) renders a muted ring.
 */
import { memo } from "react";
import { StyleSheet, Text, View } from "react-native";
import Svg, { Path } from "react-native-svg";
import { formatCents } from "../data/CurrencyFormatter";
import { needPct, wantPct, type CurrencyCode, type SummaryStats, totalCents } from "../data/schema";
import { useThemeColors } from "../design/theme";

interface Props {
  stats: SummaryStats;
  currency: CurrencyCode;
  size?: number;
}

/** SVG arc path between two angles for a ring of given radii. */
function arcPath(cx: number, cy: number, outerR: number, innerR: number, startDeg: number, endDeg: number): string {
  // -90° = top (12 o'clock), clockwise.
  const toRad = (deg: number) => ((deg - 90) * Math.PI) / 180;
  const sOut = { x: cx + outerR * Math.cos(toRad(startDeg)), y: cy + outerR * Math.sin(toRad(startDeg)) };
  const eOut = { x: cx + outerR * Math.cos(toRad(endDeg)), y: cy + outerR * Math.sin(toRad(endDeg)) };
  const sIn = { x: cx + innerR * Math.cos(toRad(endDeg)), y: cy + innerR * Math.sin(toRad(endDeg)) };
  const eIn = { x: cx + innerR * Math.cos(toRad(startDeg)), y: cy + innerR * Math.sin(toRad(startDeg)) };
  const largeArc = endDeg - startDeg > 180 ? 1 : 0;
  return [
    `M ${sOut.x} ${sOut.y}`,
    `A ${outerR} ${outerR} 0 ${largeArc} 1 ${eOut.x} ${eOut.y}`,
    `L ${sIn.x} ${sIn.y}`,
    `A ${innerR} ${innerR} 0 ${largeArc} 0 ${eIn.x} ${eIn.y}`,
    "Z",
  ].join(" ");
}

function DonutChartImpl({ stats, currency, size = 200 }: Props) {
  const colors = useThemeColors();
  const total = totalCents(stats);
  const outer = size / 2;
  const inner = outer * 0.62;
  const cx = size / 2;
  const cy = size / 2;

  let slices: { frac: number; color: string }[] = [];
  if (total > 0) {
    slices = [
      { frac: needPct(stats), color: colors.need },
      { frac: wantPct(stats), color: colors.want },
    ];
  }

  let startAngle = 0;
  const paths = slices
    .filter((s) => s.frac > 0)
    .map((s) => {
      const endAngle = startAngle + 360 * s.frac;
      const d = arcPath(cx, cy, outer, inner, startAngle, endAngle);
      startAngle = endAngle;
      return <Path key={s.color} d={d} fill={s.color} />;
    });

  const a11Label =
    total > 0
      ? `Total ${formatCents(total, currency)}. Needs ${Math.round(needPct(stats) * 100)} percent, wants ${Math.round(wantPct(stats) * 100)} percent.`
      : "No expenses logged";

  return (
    <View style={styles.container} accessibilityLabel={a11Label} accessibilityRole="image">
      <Svg width={size} height={size}>
        {/* Muted background ring for empty state. */}
        {total === 0 && (
          <Path
            d={arcPath(cx, cy, outer, inner, 0, 359.99)}
            fill={colors.surfaceRaised}
          />
        )}
        {paths}
      </Svg>
      <View style={[styles.centerLabel, { width: size, height: size }]}>
        <Text style={[styles.eyebrow, { color: colors.textSecondary }]}>TOTAL</Text>
        <Text style={[styles.total, { color: colors.textPrimary }]}>
          {formatCents(total, currency)}
        </Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { alignItems: "center", justifyContent: "center" },
  centerLabel: {
    position: "absolute",
    alignItems: "center",
    justifyContent: "center",
  },
  eyebrow: {
    fontSize: 11,
    fontWeight: "600",
    textTransform: "uppercase",
    letterSpacing: 0.8,
  },
  total: {
    fontSize: 20,
    fontWeight: "bold",
    fontVariant: ["tabular-nums"],
    marginTop: 2,
  },
});

export const DonutChart = memo(DonutChartImpl);
