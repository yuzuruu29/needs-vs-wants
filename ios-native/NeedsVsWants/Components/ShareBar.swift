import SwiftUI

/// Hairline ratio bar — a compact alternative to the donut for stat cards.
/// Need (green) left, Want (red) right, proportional by cents.
struct ShareBar: View {
    let stats: SummaryStats

    var body: some View {
        GeometryReader { geo in
            let total = CGFloat(stats.totalCents)
            let needW = total > 0 ? geo.size.width * CGFloat(stats.needsTotalCents) / total : 0

            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 2)
                    .fill(AppColors.want.opacity(0.25))
                RoundedRectangle(cornerRadius: 2)
                    .fill(AppColors.need)
                    .frame(width: needW)
            }
        }
        .frame(height: 4)
        .accessibilityLabel("Need \(Int(stats.needPct * 100)) percent, want \(Int(stats.wantPct * 100)) percent")
    }
}
