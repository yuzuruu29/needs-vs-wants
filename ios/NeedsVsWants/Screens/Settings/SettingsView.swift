import SwiftUI

/// Settings: currency radio, data wipe, about. Currency is display-only.
struct SettingsView: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings
    @Binding var showInstructions: Bool

    @State private var showWipeConfirm = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                VStack(alignment: .leading, spacing: 6) {
                    Eyebrow("PREFERENCES", color: .crimson)
                    Text("SETTINGS")
                        .font(AppFont.title)
                        .foregroundStyle(Color.textPrimary)
                    GiltRule(width: 40)
                }

                Spacer().frame(height: 28)
                sectionLabel("CURRENCY")
                Spacer().frame(height: 10)
                currencyCard

                Spacer().frame(height: 28)
                sectionLabel("DATA")
                Spacer().frame(height: 10)
                wipeRow

                Spacer().frame(height: 28)
                sectionLabel("ABOUT")
                Spacer().frame(height: 10)
                aboutCard
            }
            .padding(.horizontal, AppSpacing.xl)
            .padding(.top, AppSpacing.xl)
            .padding(.bottom, 12)
        }
        .background(Color.surface)
        .alert("Wipe all data?", isPresented: $showWipeConfirm) {
            Button("Cancel", role: .cancel) {}
            Button("Wipe", role: .destructive) {
                store.deleteAll()
                settings.reset()
                settings.completeFirstLaunch()
            }
        } message: {
            Text("This will permanently delete all entries and reset settings.\nThere is no recovery.")
        }
    }

    private func sectionLabel(_ text: String) -> some View {
        Eyebrow(text, color: .textMuted, size: 12)
    }

    private var currencyCard: some View {
        PremiumCard(padding: 6) {
            VStack(spacing: 0) {
                ForEach(Array(CurrencyOption.defaults.enumerated()), id: \.element.id) { i, option in
                    let selected = option.code == settings.currencyCode
                    HStack(spacing: 14) {
                        ZStack {
                            if selected {
                                Circle().fill(Color.gold).frame(width: 18, height: 18)
                                Circle().fill(Color.surfaceCard).frame(width: 10, height: 10)
                            } else {
                                Circle().stroke(Color.dividerStrong, lineWidth: 1).frame(width: 18, height: 18)
                            }
                        }
                        .frame(width: 18, height: 18)
                        Text(option.symbol)
                            .font(.system(size: 15, weight: .semibold))
                            .foregroundStyle(selected ? Color.gold : Color.textSecondary)
                        Text(option.label)
                            .font(.system(size: 14))
                            .foregroundStyle(selected ? Color.textPrimary : Color.textSecondary)
                    }
                    .contentShape(Rectangle())
                    .onTapGesture { settings.setCurrency(symbol: option.symbol, code: option.code) }
                    .padding(.horizontal, 12)
                    .padding(.vertical, 12)

                    if i < CurrencyOption.defaults.count - 1 {
                        Divider().background(Color.divider).padding(.horizontal, 12)
                    }
                }
            }
        }
    }

    private var wipeRow: some View {
        PremiumCard {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Wipe diary")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(Color.danger)
                    Text("Permanently delete all entries & reset settings")
                        .font(.system(size: 11))
                        .foregroundStyle(Color.textMuted)
                }
                Spacer()
                Image(systemName: "chevron.right")
                    .foregroundStyle(Color.danger.opacity(0.6))
            }
            .contentShape(Rectangle())
            .onTapGesture { showWipeConfirm = true }
        }
    }

    private var aboutCard: some View {
        PremiumCard {
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    RoundedRectangle(cornerRadius: 2).fill(Color.crimson).frame(width: 10, height: 10)
                    Text("Needs vs. Wants Expense Tracker")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundStyle(Color.textPrimary)
                    Spacer()
                    Text("v1.0.0").font(.system(size: 11)).foregroundStyle(Color.textMuted)
                }
                Text("This app allows you to record all of your daily expenses, helping you become more aware of your spending habits and tendencies. By consistently tracking every expense, you can better distinguish between your needs and wants, make smarter financial decisions, and develop stronger self-discipline.")
                    .font(.system(size: 14))
                    .foregroundStyle(Color.textSecondary)
                Button {
                    settings.completeFirstLaunch()
                    showInstructions = true
                } label: {
                    Text("How it works")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundStyle(Color.crimson)
                }
                .padding(.top, 2)
            }
        }
    }
}
