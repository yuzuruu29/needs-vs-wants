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
            .inkWashBackground()
            .navigationTitle("")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        appModel.showOnboarding = true
                    } label: {
                        Image(systemName: "questionmark.circle")
                            .foregroundStyle(AppColors.crimson)
                    }
                    .accessibilityLabel("How it works")
                }
            }
        }
    }

    // MARK: - Header

    private var headerSection: some View {
        VStack(spacing: 6) {
            Eyebrow(text: "A 35-Day Trainer", color: AppColors.crimson)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, 8)

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
            .frame(maxWidth: .infinity, alignment: .leading)

            GiltRule(width: 40)
                .frame(maxWidth: .infinity, alignment: .leading)

            Text("Expense Tracker")
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(AppColors.crimson)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
    }

    // MARK: - Period rotor

    private var periodRotor: some View {
        VStack(spacing: 6) {
            PeriodRotor(period: $period)
            Text(rangeCaption)
                .font(AppTypography.caption)
                .foregroundStyle(AppColors.textSecondary)
        }
    }

    // MARK: - Donut

    @ViewBuilder
    private var donutSection: some View {
        if stats.totalCents > 0 {
            VStack(spacing: 14) {
                DonutChart(stats: stats, currency: currency)
                    .frame(width: 180, height: 180)
                    .giltGlow(alpha: 0.16, diameter: 220)

                HStack(spacing: 20) {
                    LegendChip(
                        color: AppColors.need,
                        label: "Need",
                        percent: Int((stats.needPct * 100).rounded())
                    )
                    Rectangle()
                        .fill(AppColors.divider)
                        .frame(width: 1, height: 14)
                    LegendChip(
                        color: AppColors.want,
                        label: "Want",
                        percent: Int((stats.wantPct * 100).rounded())
                    )
                }
            }
        } else {
            emptyDonut
        }
    }

    private var emptyDonut: some View {
        VStack(spacing: 16) {
            Circle()
                .stroke(AppColors.divider, lineWidth: 16)
                .frame(width: 150, height: 150)
                .giltGlow(alpha: 0.18, diameter: 200)

            Eyebrow(text: "EMPTY DIARY")
            Text("Log your first expense\nto start the diary.")
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
                .multilineTextAlignment(.center)
        }
    }

    // MARK: - Stat cards

    private var statCards: some View {
        HStack(spacing: 12) {
            moneyStatCard(
                title: "NEEDS",
                cents: stats.needsTotalCents,
                accent: AppColors.need,
                fraction: stats.needPct
            )
            moneyStatCard(
                title: "WANTS",
                cents: stats.wantsTotalCents,
                accent: AppColors.want,
                fraction: stats.wantPct
            )
            percentStatCard(
                title: "NEED %",
                percent: Int((stats.needPct * 100).rounded()),
                fraction: stats.needPct
            )
        }
    }

    private func moneyStatCard(
        title: String,
        cents: Int64,
        accent: Color,
        fraction: Double
    ) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Eyebrow(text: title)
            CurrencyText(cents: cents, currency: currency)
                .font(.system(size: 15, weight: .bold).monospacedDigit())
                .foregroundStyle(accent)
                .minimumScaleFactor(0.5)
                .lineLimit(1)
            AccentShareBar(fraction: fraction, accent: accent)
                .padding(.top, 2)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.vertical, 14)
        .padding(.horizontal, 12)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: AppMetrics.cardRadius))
        .overlay(
            RoundedRectangle(cornerRadius: AppMetrics.cardRadius)
                .stroke(AppColors.divider, lineWidth: 1)
        )
    }

    private func percentStatCard(title: String, percent: Int, fraction: Double) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Eyebrow(text: title)
            Text("\(percent)%")
                .font(.system(size: 15, weight: .bold).monospacedDigit())
                .foregroundStyle(AppColors.gold)
                .lineLimit(1)
            AccentShareBar(fraction: fraction, accent: AppColors.gold)
                .padding(.top, 2)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.vertical, 14)
        .padding(.horizontal, 12)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: AppMetrics.cardRadius))
        .overlay(
            RoundedRectangle(cornerRadius: AppMetrics.cardRadius)
                .stroke(AppColors.divider, lineWidth: 1)
        )
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Need \(percent) percent")
    }

    // MARK: - CTA

    private var ctaButton: some View {
        PrimaryButton(title: "Log an expense") {
            appModel.switchToLog()
        }
        .padding(.top, 4)
    }
}
