import Foundation
import SwiftData
import Observation

/// Single source of truth for entries. SwiftData-backed.
/// Mirrors the Android `EntryDao` + `SummaryUseCase` behavior.
/// All access happens on the main actor (SwiftUI views), so the store is
/// created and used there; it is intentionally NOT @MainActor-marked so it can
/// be constructed in the App's nonisolated init.
@Observable
final class EntryStore {
    private let context: ModelContext
    private(set) var entries: [Entry] = []

    init(context: ModelContext) {
        self.context = context
        fetchAll()
        purgeOlderThan(days: 35)            // Android: purge on Application.onCreate
    }

    // MARK: - Reads

    private func fetchAll() {
        let descriptor = FetchDescriptor<Entry>(
            sortBy: [SortDescriptor(\.dateUtc, order: .reverse)]
        )
        entries = (try? context.fetch(descriptor)) ?? []
    }

    // MARK: - Writes

    /// Builds, inserts, and persists an entry stamped at `now()`. Sealing IS saving.
    func insert(item: String, costCents: Int64, type: EntryType) {
        let now = Date()
        let dateStr = Self.dayFormatter.string(from: now)
        let timeStr = Self.timeFormatter.string(from: now)
        let entry = Entry(
            dateUtc: now,
            date: dateStr,
            time: timeStr,
            item: item,
            costCents: costCents,
            type: type
        )
        context.insert(entry)
        try? context.save()
        fetchAll()
    }

    func delete(_ entry: Entry) {
        context.delete(entry)
        try? context.save()
        fetchAll()
    }

    func deleteAll() {
        for e in entries { context.delete(e) }
        try? context.save()
        fetchAll()
    }

    // MARK: - Retention

    func purgeOlderThan(days: Int) {
        let cutoff = Calendar.current.date(byAdding: .day, value: -days, to: Date())!
        let predicate = #Predicate<Entry> { $0.dateUtc < cutoff }
        let descriptor = FetchDescriptor<Entry>(predicate: predicate)
        if let stale = try? context.fetch(descriptor) {
            for e in stale { context.delete(e) }
            try? context.save()
            fetchAll()
        }
    }

    // MARK: - Sheet cap (parity note: counts TOTAL entries, like Android)

    var sheetCount: Int { entries.count }
    var isSheetFull: Bool { entries.count >= 20 }

    // MARK: - Summary

    func stats(for period: Period) -> SummaryStats {
        let since = startOf(period: period)
        let window = entries.filter { $0.dateUtc >= since }
        var s = SummaryStats()
        for e in window {
            if e.type == .need {
                s.needsTotalCents += e.costCents
                s.needsCount += 1
            } else {
                s.wantsTotalCents += e.costCents
                s.wantsCount += 1
            }
        }
        return s
    }

    private func startOf(period: Period) -> Date {
        let cal = Calendar.current
        let now = Date()
        switch period {
        case .day:  return cal.startOfDay(for: now)
        case .week: return cal.date(byAdding: .day, value: -6, to: cal.startOfDay(for: now))!
        case .all:  return cal.date(byAdding: .day, value: -34, to: cal.startOfDay(for: now))!
        }
    }

    // MARK: - Formatters

    static let dayFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "yyyy-MM-dd"; return f
    }()
    static let timeFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "HH:mm"; return f
    }()
}
