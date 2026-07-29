import SwiftUI

/// White surface, hairline border, calm large corners. Matches Android `GiltCard`.
struct PremiumCard<Content: View>: View {
    let content: () -> Content
    var padding: CGFloat = AppSpacing.md
    var accent: Bool = false

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            content()
        }
        .padding(padding)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.surfaceCard)
        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.divider, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .if(accent) { view in
            view.overlay(
                LinearGradient(colors: [.clear, Color.gold.opacity(0.55), .clear],
                               startPoint: .leading, endPoint: .trailing)
                    .frame(height: 1.5)
                    .padding(.top, 0),
                alignment: .top
            )
            .clipShape(RoundedRectangle(cornerRadius: 16))
        }
    }
}

extension View {
    @ViewBuilder
    func `if`<T: View>(_ condition: Bool, transform: (Self) -> T) -> some View {
        if condition { transform(self) } else { self }
    }
}
