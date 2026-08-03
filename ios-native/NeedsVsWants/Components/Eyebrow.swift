import SwiftUI

/// Small-caps eyebrow label — the "TODAY", "SHEET n/20" style overlines.
struct Eyebrow: View {
    let text: String
    var color: Color = AppColors.textSecondary

    var body: some View {
        Text(text.uppercased())
            .font(AppTypography.eyebrow)
            .tracking(1.2)
            .foregroundStyle(color)
    }
}
