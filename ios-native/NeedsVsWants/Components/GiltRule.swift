import SwiftUI

/// Short gold rule under section titles — Android `GiltRule` parity.
struct GiltRule: View {
    var width: CGFloat = AppMetrics.giltRuleWidth
    var height: CGFloat = AppMetrics.giltRuleHeight

    var body: some View {
        Rectangle()
            .fill(AppColors.gold)
            .frame(width: width, height: height)
            .accessibilityHidden(true)
    }
}
