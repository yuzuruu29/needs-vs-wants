import SwiftUI

/// Native swipeable onboarding — themed to match Android `InstructionsOverlay`.
struct OnboardingView: View {
    @AppStorage("hasOnboarded") private var hasOnboarded = false
    @Environment(\.dismiss) private var dismiss
    @State private var page = 0

    private let pages: [(title: String, body: String)] = [
        (
            "Every expense is a Need or a Want",
            "Each purchase forces a single binary choice. You confront impulse spending in real time, not at month-end."
        ),
        (
            "The diary keeps 35 days",
            "Entries older than 35 days are automatically removed. This is a trainer, not an archive."
        ),
        (
            "Rows seal themselves",
            "The moment you enter item, cost, and type, the row seals — stamped with the current time and saved instantly."
        ),
    ]

    var body: some View {
        VStack(spacing: 0) {
            TabView(selection: $page) {
                ForEach(pages.indices, id: \.self) { i in
                    pageCard(pages[i])
                        .tag(i)
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never))

            pageDots
                .padding(.bottom, 20)

            bottomBar
                .padding(.horizontal, 24)
                .padding(.bottom, 28)
        }
        .inkWashBackground()
    }

    private func pageCard(_ page: (title: String, body: String)) -> some View {
        VStack(spacing: 16) {
            Spacer()
            Eyebrow(text: "WELCOME", color: AppColors.crimson)
            GiltRule(width: 32)
            Text(page.title)
                .font(AppTypography.display(22, weight: .bold))
                .foregroundStyle(AppColors.textPrimary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Text(page.body)
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
            Spacer()
            Spacer()
        }
    }

    private var pageDots: some View {
        HStack(spacing: 8) {
            ForEach(pages.indices, id: \.self) { i in
                Circle()
                    .fill(i == page ? AppColors.crimson : AppColors.divider)
                    .frame(width: 6, height: 6)
            }
        }
        .accessibilityHidden(true)
    }

    private var bottomBar: some View {
        HStack {
            Button("Skip") {
                finish()
            }
            .font(.system(size: 15, weight: .regular))
            .foregroundStyle(AppColors.textSecondary)

            Spacer()

            if page < pages.count - 1 {
                Button("Next") {
                    withAnimation { page += 1 }
                }
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(AppColors.crimson)
            } else {
                PrimaryButton(title: "Begin") {
                    finish()
                }
                .frame(width: 140)
            }
        }
    }

    private func finish() {
        hasOnboarded = true
        dismiss()
    }
}
