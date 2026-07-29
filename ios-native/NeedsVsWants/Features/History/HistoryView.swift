import SwiftUI
import SwiftData

struct HistoryView: View {
    @Environment(EntryRepository.self) private var repo
    @Environment(AppModel.self) private var appModel
    @AppStorage("currency") private var currencyRaw = CurrencyOption.default.rawValue

    @Query(sort: \Entry.dateUtc, order: .reverse) private var entries: [Entry]
    @State private var vm: HistoryViewModel?

    @State private var showDeleteAlert = false
    @State private var entryToDelete: Entry?

    private var currency: CurrencyOption {
        CurrencyOption(rawValue: currencyRaw) ?? .default
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
            .background(AppColors.surface)
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

    private var grouped: [(key: String, entries: [Entry])] {
        vm?.grouped(entries: entries) ?? []
    }

    // MARK: - Day section

    private func daySection(_ key: String, _ dayEntries: [Entry]) -> some View {
        let totals = vm?.dayTotals(dayEntries) ?? (0, 0)
        return VStack(spacing: 0) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(formatDayHeader(key))
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundStyle(AppColors.textPrimary)
                    Text("\(dayEntries.count) entries")
                        .font(AppTypography.caption)
                        .foregroundStyle(AppColors.textSecondary)
                }
                Spacer()
                VStack(alignment: .trailing, spacing: 2) {
                    if totals.0 > 0 {
                        CurrencyText(cents: totals.0, currency: currency)
                            .foregroundStyle(AppColors.need)
                    }
                    if totals.1 > 0 {
                        CurrencyText(cents: totals.1, currency: currency)
                            .foregroundStyle(AppColors.want)
                    }
                }
            }
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
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(AppColors.divider, lineWidth: 1))
    }

    // MARK: - Empty state

    private var emptyState: some View {
        VStack(spacing: 16) {
            Circle()
                .stroke(AppColors.divider, lineWidth: 2)
                .frame(width: 100, height: 100)
                .overlay {
                    Image(systemName: "book")
                        .font(.system(size: 32))
                        .foregroundStyle(AppColors.textSecondary)
                }
            Text("The page waits for ink.")
                .font(AppTypography.displaySmall)
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
        // key is "yyyy-MM-dd"; format as "Jul 29, 2026"
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
