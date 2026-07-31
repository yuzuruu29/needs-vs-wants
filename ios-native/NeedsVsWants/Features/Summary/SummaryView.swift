import SwiftUI
import SwiftData

struct SummaryView: View {
    @Environment(EntryRepository.self) private var repo
    @Environment(AppModel.self) private var appModel
    @AppStorage("currency") private var currencyRaw = CurrencyOption.default.rawValue

    @State private var period: Period = .day

    private var currency: CurrencyOption {
        CurrencyOption(rawValue: currencyRaw) ?? .default
    }

    private var stats: SummaryStats { repo.stats(for: period) }
    private var rangeCaption: String { StatsEngine().rangeCaption(period) }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 24) {
                    headerSection
                    periodRotor
                    donutSection
                    statCards
                    ctaButton
                }
                .padding(.horizontal, 20)
                .padding(.bottom, 40)
            }
            .background(AppColors.surface)
            .navigationTitle("")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        appModel.showOnboarding = true
                    } label: {
                        Image(systemName: "questionmark.circle")
                            .foregroundStyle(AppColors.textSecondary)
                    }
                    .accessibilityLabel("How it works")
                }
            }
        }
    }

    // MARK: - Header

    private var headerSection: some View {
        VStack(spacing: 4) {
            HStack(spacing: 8) {
                Text("NEEDS")
                    .font(AppTypography.displayMedium)
                    .foregroundStyle(AppColors.textPrimary)
                Text("vs")
                    .font(AppTypography.display(18, weight: .regular))
                    .foregroundStyle(AppColors.textSecondary)
                Text("WANTS")
                    .font(AppTypography.displayMedium)
                    .foregroundStyle(AppColors.crimson)
            }
            .padding(.top, 8)

            Text("Expense Tracker")
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
        }
    }

    // MARK: - Period rotor

    private var periodRotor: some View {
        VStack(spacing: 6) {
            Picker("Period", selection: $period) {
                ForEach(Period.allCases, id: \.self) { p in
                    Text(p.label).tag(p)
                }
            }
            .pickerStyle(.segmented)

            Text(rangeCaption)
                .font(AppTypography.caption)
                .foregroundStyle(AppColors.textSecondary)
        }
    }

    // MARK: - Donut

    @ViewBuilder
    private var donutSection: some View {
        if stats.totalCents > 0 {
            DonutChart(stats: stats, currency: currency)
                .frame(width: 180, height: 180)
        } else {
            emptyDonut
        }
    }

    private var emptyDonut: some View {
        VStack(spacing: 8) {
            Circle()
                .stroke(AppColors.divider, lineWidth: 2)
                .frame(width: 180, height: 180)
                .overlay {
                    VStack(spacing: 4) {
                        Text("TOTAL")
                            .font(AppTypography.eyebrow)
                            .foregroundStyle(AppColors.textSecondary)
                        Text("Log your first expense to start the diary.")
                            .font(AppTypography.caption)
                            .foregroundStyle(AppColors.textSecondary)
                            .multilineTextAlignment(.center)
                            .padding(.horizontal, 30)
                    }
                }
        }
    }

    // MARK: - Stat cards

    @ViewBuilder
    private var statCards: some View {
        HStack(spacing: 12) {
            statCard("NEEDS", stats.needsTotalCents, AppColors.need, stats.needsCount)
            statCard("WANTS", stats.wantsTotalCents, AppColors.want, stats.wantsCount)
            statCard("NEED %", Int64(Int(stats.needPct * 100)), AppColors.accent, nil, suffix: "%")
        }
    }

    private func statCard(_ title: String, _ cents: Int64, _ color: Color, _ count: Int?, suffix: String = "") -> some View {
        VStack(spacing: 8) {
            Eyebrow(text: title)
            if suffix == "%" {
                Text("\(Int(cents))%")
                    .font(.system(size: 22, weight: .bold).monospacedDigit())
                    .foregroundStyle(color)
            } else {
                CurrencyText(cents: cents, currency: currency)
                    .font(.system(size: 18, weight: .bold).monospacedDigit())
                    .foregroundStyle(color)
            }
            if let count {
                Text("\(count) entries")
                    .font(AppTypography.caption)
                    .foregroundStyle(AppColors.textSecondary)
            }
            ShareBar(stats: stats)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .padding(.horizontal, 10)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(AppColors.divider, lineWidth: 1))
    }

    // MARK: - CTA

    private var ctaButton: some View {
        PrimaryButton(title: "Log an expense") {
            appModel.switchToLog()
        }
        .padding(.top, 4)
    }
}
