import SwiftUI

/// Three-card first-launch overlay. Auto-shows on first launch (TabContainer),
/// reachable from Summary "?" and Settings "How it works".
struct InstructionsView: View {
    let onDismiss: () -> Void
    @State private var page = 0

    private let titles = [
        "Every expense is a Need or a Want",
        "Your diary keeps 35 days",
        "Rows seal themselves"
    ]
    private let bodies = [
        "Each entry forces a binary choice. There is no middle ground. This is the lesson.",
        "Older entries are automatically removed. The window is always 35 days.",
        "When you fill in item, cost, and type, the row saves instantly. Tap a sealed row to remove it."
    ]

    var body: some View {
        VStack(spacing: 0) {
            VStack(spacing: 8) {
                Eyebrow("WELCOME", color: .crimson)
                GiltRule(width: 32)
            }
            .padding(.top, 28)

            Spacer()

            VStack(spacing: 18) {
                Text(titles[page])
                    .font(.system(size: 22, weight: .bold, design: .serif))
                    .foregroundStyle(Color.textPrimary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 24)
                Text(bodies[page])
                    .font(.system(size: 16))
                    .foregroundStyle(Color.textSecondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }

            Spacer()

            HStack(spacing: 8) {
                ForEach(0..<3, id: \.self) { i in
                    Circle()
                        .fill(i == page ? Color.crimson : Color.divider)
                        .frame(width: 6, height: 6)
                }
            }
            .padding(.bottom, 20)

            HStack {
                Button("Skip") { onDismiss() }
                    .foregroundStyle(Color.textMuted)
                Spacer()
                if page < 2 {
                    Button("Next") { withAnimation { page += 1 } }
                        .foregroundStyle(Color.crimson)
                        .fontWeight(.semibold)
                } else {
                    Button("Begin") { onDismiss() }
                        .foregroundStyle(Color.crimson)
                        .fontWeight(.bold)
                }
            }
            .padding(.horizontal, 28)
            .padding(.bottom, 28)
        }
        .presentationDragIndicator(.visible)
        .presentationDetents([.medium, .large])
        .background(Color.surface)
    }
}
