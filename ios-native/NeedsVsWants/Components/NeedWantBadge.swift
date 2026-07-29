import SwiftUI

/// Native iOS chip/tag for Need/Want classification.
/// Need = green, Want = red (D7 semantic mapping).
struct NeedWantBadge: View {
    let type: EntryType

    var body: some View {
        Text(type == .need ? "NEED" : "WANT")
            .font(.system(size: 10, weight: .bold))
            .tracking(0.5)
            .padding(.horizontal, 6)
            .padding(.vertical, 3)
            .background(color.opacity(0.14))
            .foregroundStyle(color)
            .clipShape(RoundedRectangle(cornerRadius: 4))
            .accessibilityLabel(type == .need ? "Need" : "Want")
    }

    private var color: Color {
        type == .need ? AppColors.need : AppColors.want
    }
}
