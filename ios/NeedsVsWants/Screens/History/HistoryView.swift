import SwiftUI

/// History: entries grouped by day, day Need/Want totals, delete confirm.
struct HistoryView: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings

    @State private var deleteTarget: Entry? = nil

    private var grouped: [(date: String, entries: [Entry])] {
        let dict = Dictionary(grouping: store.entries, by: \.date)
        return dict.keys.sorted(by: >).map { ($0, dict[$0]!.sorted { $0.dateUtc > $1.dateUtc }) }
    }

    private var dateRange: String? {
        guard let oldest = store.entries.last?.date, let newest = store.entries.first?.date else { return nil }
        let f = DateFormatter(); f.dateFormat = "yyyy-MM-dd"
        guard let od = f.date(from: oldest), let nd = f.date(from: newest) else { return nil }
        let out = DateFormatter(); out.dateFormat = "EEE, MMM d"
        return "\(out.string(from: od)) — \(out.string(from: nd))"
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: 6) {
                Eyebrow("HISTORY", color: .crimson)
                Text("LEDGER")
                    .font(AppFont.title)
                    .foregroundStyle(Color.textPrimary)
                GiltRule(width: 40)
                if let range = dateRange {
                    Text(range)
                        .font(.system(size: 11))
                        .tracking(0.4)
                        .foregroundStyle(Color.textMuted)
                }
            }
            .padding(.horizontal, AppSpacing.xl)
            .padding(.top, AppSpacing.xl)

            Spacer().frame(height: AppSpacing.lg)

            if store.entries.isEmpty {
                emptyState
            } else {
                ScrollView {
                    LazyVStack(spacing: 16) {
                        ForEach(grouped, id: \.date) { group in
                            dayCard(group)
                        }
                    }
                    .padding(.horizontal, AppSpacing.xl)
                    .padding(.vertical, 6)
                }
            }

            if !store.entries.isEmpty {
                PrimaryButton(title: "Log an expense") {
                    NotificationCenter.default.post(name: .switchToLog, object: nil)
                }
                .padding(.horizontal, AppSpacing.xl)
                .padding(.bottom, 12)
            }
        }
        .background(Color.surface)
        .alert("Delete entry?", isPresented: Binding(
            get: { deleteTarget != nil },
            set: { if !$0 { deleteTarget = nil } }
        )) {
            Button("Cancel", role: .cancel) { deleteTarget = nil }
            Button("Delete", role: .destructive) {
                if let e = deleteTarget { store.delete(e) }
                deleteTarget = nil
            }
        } message: {
            if let e = deleteTarget {
                Text("\(e.item) — \(CurrencyFormatter.format(e.costCents, symbol: settings.currencySymbol))")
            }
        }
    }

    private var emptyState: some View {
        VStack(spacing: 18) {
            Circle()
                .stroke(Color.crimson.opacity(0.35), lineWidth: 1)
                .frame(width: 96, height: 96)
            Eyebrow("EMPTY DIARY", color: .textMuted)
            Text("The page waits for ink.")
                .font(.system(size: 14))
                .foregroundStyle(Color.textSecondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private func dayCard(_ group: (date: String, entries: [Entry])) -> some View {
        let dayNeeds = group.entries.filter { $0.type == .need }.reduce(0) { $0 + $1.costCents }
        let dayWants = group.entries.filter { $0.type == .want }.reduce(0) { $0 + $1.costCents }
        return PremiumCard {
            VStack(alignment: .leading, spacing: 12) {
                HStack {
                    Text(displayDate(group.date).uppercased())
                        .font(.system(size: 11, weight: .semibold))
                        .tracking(1.6)
                        .foregroundStyle(Color.textPrimary)
                    Spacer()
                    Text("\(group.entries.count) entries")
                        .font(.system(size: 10))
                        .foregroundStyle(Color.textMuted)
                }
                HStack(spacing: 18) {
                    DayTotal(color: .need, label: "Need", value: CurrencyFormatter.format(dayNeeds, symbol: settings.currencySymbol))
                    DayTotal(color: .want, label: "Want", value: CurrencyFormatter.format(dayWants, symbol: settings.currencySymbol))
                }
                Divider().background(Color.divider)
                VStack(spacing: 8) {
                    ForEach(group.entries, id: \.id) { entry in
                        LedgerRow(entry: entry, symbol: settings.currencySymbol,
                                  onDelete: { deleteTarget = entry })
                    }
                }
            }
        }
    }

    private func displayDate(_ iso: String) -> String {
        let f = DateFormatter(); f.dateFormat = "yyyy-MM-dd"
        guard let d = f.date(from: iso) else { return iso }
        let out = DateFormatter(); out.dateFormat = "EEE, MMM d"
        return out.string(from: d)
    }
}

struct DayTotal: View {
    let color: Color
    let label: String
    let value: String
    var body: some View {
        HStack(spacing: 6) {
            Circle().fill(color).frame(width: 8, height: 8)
            Text(label + " ")
                .font(.system(size: 13))
                .foregroundStyle(Color.textSecondary)
            Text(value)
                .font(.system(size: CurrencyFormatter.adaptiveSize(value, base: 13), weight: .semibold).monospacedDigit())
                .foregroundStyle(Color.textPrimary)
                .lineLimit(1)
        }
    }
}
