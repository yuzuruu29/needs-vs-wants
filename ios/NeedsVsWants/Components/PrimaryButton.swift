import SwiftUI

/// Primary action button — solid crimson, white uppercase label.
struct PrimaryButton: View {
    let title: String
    let action: () -> Void
    var height: CGFloat = 54

    var body: some View {
        Button(action: action) {
            Text(title.uppercased())
                .font(.system(size: 14, weight: .semibold))
                .tracking(1.2)
                .foregroundStyle(Color.surfaceCard)
                .frame(maxWidth: .infinity, minHeight: height)
        }
        .background(Color.crimson)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .buttonStyle(.plain)
    }
}
