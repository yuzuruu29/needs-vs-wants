import SwiftUI

/// Native swipeable onboarding — TabView with .page style.
/// Replaces the old port's manual Next/Skip pagination.
struct OnboardingView: View {
    @AppStorage("hasOnboarded") private var hasOnboarded = false
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        TabView {
            card(
                icon: "cart",
                title: "Every expense is a Need or a Want",
                body: "Each purchase forces a single binary choice. You confront impulse spending in real time, not at month-end."
            )
            card(
                icon: "calendar",
                title: "The diary keeps 35 days",
                body: "Entries older than 35 days are automatically removed. This is a trainer, not an archive."
            )
            card(
                icon: "checkmark.seal",
                title: "Rows seal themselves",
                body: "The moment you enter item, cost, and type, the row seals — stamped with the current time and saved instantly."
            )
        }
        .tabViewStyle(.page(indexDisplayMode: .always))
        .indexViewStyle(.page(backgroundDisplayMode: .always))
        .background(AppColors.surface)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button("Got it") {
                    hasOnboarded = true
                    dismiss()
                }
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(AppColors.accent)
            }
        }
    }

    private func card(icon: String, title: String, body: String) -> some View {
        VStack(spacing: 24) {
            Spacer()
            Image(systemName: icon)
                .font(.system(size: 56))
                .foregroundStyle(AppColors.crimson)
            Text(title)
                .font(AppTypography.display(22, weight: .bold))
                .foregroundStyle(AppColors.textPrimary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Text(body)
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Spacer()
            Spacer()
        }
    }
}
