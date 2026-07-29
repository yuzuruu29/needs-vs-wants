import SwiftUI

/// Hand-rolled Canvas donut chart (D6: no third-party chart libs).
/// Need (green) / Want (red), total centered. Empty state handled by parent.
struct DonutChart: View {
    let stats: SummaryStats
    let currency: CurrencyOption

    var body: some View {
        ZStack {
            Canvas { ctx, size in
                let outer = min(size.width, size.height) / 2
                let inner = outer * 0.62
                let center = CGPoint(x: size.width / 2, y: size.height / 2)
                let total = CGFloat(stats.totalCents)
                guard total > 0 else { return }

                var start = Angle.degrees(-90)
                let slices: [(CGFloat, Color)] = [
                    (CGFloat(stats.needsTotalCents) / total, AppColors.need),
                    (CGFloat(stats.wantsTotalCents) / total, AppColors.want),
                ]
                for (frac, color) in slices {
                    guard frac > 0 else { continue }
                    let end = start + .degrees(360 * frac)
                    let path = Path { p in
                        p.addArc(center: center, radius: outer,
                                  startAngle: start, endAngle: end, clockwise: false)
                        p.addArc(center: center, radius: inner,
                                  startAngle: end, endAngle: start, clockwise: true)
                        p.closeSubpath()
                    }
                    ctx.fill(path, with: .color(color))
                    start = end
                }
            }

            VStack(spacing: 2) {
                Text("TOTAL")
                    .font(AppTypography.eyebrow)
                    .foregroundStyle(AppColors.textSecondary)
                Text(CurrencyFormatter.format(cents: stats.totalCents, currency: currency))
                    .font(.system(size: 20, weight: .bold).monospacedDigit())
                    .foregroundStyle(AppColors.textPrimary)
            }
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(accessibilityLabel)
    }

    private var accessibilityLabel: String {
        guard stats.totalCents > 0 else { return "No expenses logged" }
        return "Total \(CurrencyFormatter.format(cents: stats.totalCents, currency: currency)). " +
               "Needs \(Int(stats.needPct * 100)) percent, wants \(Int(stats.wantPct * 100)) percent."
    }
}
