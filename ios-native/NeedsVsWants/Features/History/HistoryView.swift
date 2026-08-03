import SwiftUI
import SwiftData

struct HistoryView: View {
    @Environment(EntryRepository.self) private var repo
    @Environment(AppModel.self) private var appModel
    @AppStorage("currency") private var currencyRaw = CurrencyOption.default.rawValue

    @Query(sort: \Entry.dateUtc, order: .reverse) private var entries: [Entry]

    @State private var showDeleteAlert = false
    @State private var entryToDelete: Entry?

    private let vm = HistoryViewModel()

    private var currency: CurrencyOption {
        CurrencyOption(rawValue: currencyRaw) ?? .default
    }

    private var grouped: [(key: String, entries: [Entry])] {
        vm.grouped(entries: entries)
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                if entries.isEmpty {
                    emptyState
                } else {
                    VStack(spacing: 16) {
                        ForEach(grouped, id: \.key) { section in
                            daySection(section.key, section.entries)
                        }
                    }
                    .padding(.horizontal, 20)
                    .padding(.bottom, 40)
                }
            }
            .inkWashBackground()
            .navigationTitle("History")
        }
        .alert("Delete entry?", isPresented: $showDeleteAlert) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive) {
                if let entry = entryToDelete {
                    _ = repo.delete(entry)
                    Haptics.warn()
                }
            }
        }
    }

    // MARK: - Day section

    private func daySection(_ key: String, _ dayEntries: [Entry]) -> some View {
        let totals = vm.dayTotals(dayEntries)
        return VStack(spacing: 0) {
            HStack {
                Text(formatDayHeader(key).uppercased())
                    .font(.system(size: 11, weight: .semibold))
                    .tracking(1.6)
                    .foregroundStyle(AppColors.textPrimary)
                Spacer()
                Text("\(dayEntries.count) entries")
                    .font(AppTypography.caption)
                    .foregroundStyle(AppColors.textSecondary)
            }
            .padding(.bottom, 12)

            HStack(spacing: 18) {
                dayTotal(color: AppColors.need, label: "Need", cents: totals.needs)
                dayTotal(color: AppColors.want, label: "Want", cents: totals.wants)
            }
            .padding(.bottom, 14)

            Divider().background(AppColors.divider)
                .padding(.bottom, 8)

            LedgerHeader()

            ForEach(dayEntries) { entry in
                LedgerRow(
                    entry: entry,
                    currency: currency,
                    onDelete: {
                        entryToDelete = entry
                        showDeleteAlert = true
                    }
                )
                Divider().background(AppColors.divider)
            }
        }
        .padding(16)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: AppMetrics.cardRadius))
        .overlay(
            RoundedRectangle(cornerRadius: AppMetrics.cardRadius)
                .stroke(AppColors.divider, lineWidth: 1)
        )
    }

    private func dayTotal(color: Color, label: String, cents: Int64) -> some View {
        HStack(spacing: 6) {
            Circle()
                .fill(color)
                .frame(width: 8, height: 8)
            Text(label)
                .font(.system(size: 13))
                .foregroundStyle(AppColors.textSecondary)
            CurrencyText(cents: cents, currency: currency)
                .font(.system(size: 13, weight: .semibold).monospacedDigit())
                .foregroundStyle(AppColors.textPrimary)
                .minimumScaleFactor(0.5)
                .lineLimit(1)
        }
        .accessibilityElement(children: .combine)
        .accessibilityLabel("\(label) \(CurrencyFormatter.format(cents: cents, currency: currency))")
    }

    // MARK: - Empty state

    private var emptyState: some View {
        VStack(spacing: 16) {
            Circle()
                .stroke(AppColors.crimson.opacity(0.35), lineWidth: 1)
                .frame(width: 96, height: 96)
                .giltGlow(alpha: 0.14, diameter: 140)
            Eyebrow(text: "EMPTY DIARY")
            Text("The page waits for ink.")
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
            PrimaryButton(title: "Log an expense") {
                appModel.switchToLog()
            }
            .padding(.horizontal, 40)
        }
        .frame(maxWidth: .infinity, minHeight: 400)
    }

    // MARK: - Helpers

    private func formatDayHeader(_ key: String) -> String {
        let input = DateFormatter()
        input.dateFormat = "yyyy-MM-dd"
        input.locale = Locale(identifier: "en_US_POSIX")
        guard let date = input.date(from: key) else { return key }
        let output = DateFormatter()
        output.dateStyle = .medium
        output.timeStyle = .none
        return output.string(from: date)
    }
}
