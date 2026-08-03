import SwiftUI

/// Color-dot + "Need 62%" label under the donut — Android `LegendChip` parity.
struct LegendChip: View {
    let color: Color
    let label: String
    let percent: Int

    var body: some View {
        HStack(spacing: 6) {
            Circle()
                .fill(color)
                .frame(width: 8, height: 8)
            Text("\(label) \(percent)%")
                .font(.system(size: 12))
                .tracking(0.5)
                .foregroundStyle(AppColors.textSecondary)
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(label) \(percent) percent")
    }
}
