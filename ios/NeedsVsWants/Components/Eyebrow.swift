import SwiftUI

/// Wide-spaced uppercase micro heading used across all screens.
struct Eyebrow: View {
    let text: String
    var color: Color = .crimson
    var size: CGFloat = 11

    var body: some View {
        Text(text.uppercased())
            .font(.system(size: size, weight: .semibold))
            .tracking(size * 0.12)
            .foregroundStyle(color)
    }
}
