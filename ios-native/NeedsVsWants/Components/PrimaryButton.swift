import SwiftUI

/// Crimson filled primary button with accessibility label/hint.
struct PrimaryButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 14)
                .background(AppColors.crimson)
                .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .accessibilityLabel(title)
    }
}
