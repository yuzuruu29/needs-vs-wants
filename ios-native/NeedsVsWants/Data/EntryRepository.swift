import Foundation
import SwiftData

enum RepositoryError: Error {
    case saveFailed
    case deleteFailed
}

/// All data access goes through here.
///
/// Improvement over the old `ios/` port's god `EntryStore`: this is a thin,
/// `Result`-returning CRUD layer — no `try?` swallowing errors, no stats
/// aggregation mixed in (that lives in `StatsEngine`). Views read via `@Query`;
/// they do not call the repository for reads.
@MainActor
final class EntryRepository {
    private let context: ModelContext

    init(context: ModelContext) {
        self.context = context
    }

    // MARK: - Writes

    /// Insert an entry stamped at now. Sealing IS saving (D5).
    @discardableResult
    func insert(item: String, costCents: Int64, type: EntryType) -> Result<Entry, RepositoryError> {
        insertAt(date: Date(), item: item, costCents: costCents, type: type)
    }

    /// Insert an entry at an explicit date (used by tests and back-dating).
    @discardableResult
    func insertAt(date: Date, item: String, costCents: Int64, type: EntryType) -> Result<Entry, RepositoryError> {
        let entry = Entry(dateUtc: date, item: item, costCents: costCents, type: type)
        context.insert(entry)
        do {
            try context.save()
            return .success(entry)
        } catch {
            return .failure(.saveFailed)
        }
    }

    @discardableResult
    func delete(_ entry: Entry) -> Result<Void, RepositoryError> {
        context.delete(entry)
        do {
            try context.save()
            return .success(())
        } catch {
            return .failure(.deleteFailed)
        }
    }

    @discardableResult
    func deleteAll() -> Result<Void, RepositoryError> {
        let all = (try? context.fetch(FetchDescriptor<Entry>())) ?? []
        for e in all { context.delete(e) }
        do {
            try context.save()
            return .success(())
        } catch {
            return .failure(.saveFailed)
        }
    }

    // MARK: - Retention (D3): silent auto-purge, no prompt.

    func purgeOlderThan(days: Int) {
        guard let cutoff = Calendar.current.date(byAdding: .day, value: -days, to: Date()) else { return }
        let predicate = #Predicate<Entry> { $0.dateUtc < cutoff }
        if let stale = try? context.fetch(FetchDescriptor<Entry>(predicate: predicate)) {
            for e in stale { context.delete(e) }
            try? context.save()
        }
    }

    // MARK: - Sheet cap (D4 / Android parity: counts TOTAL entries)

    var sheetCount: Int { (try? context.fetch(FetchDescriptor<Entry>()))?.count ?? 0 }
    var isSheetFull: Bool { sheetCount >= 20 }

    // MARK: - Stats

    func stats(for period: Period, engine: StatsEngine) -> SummaryStats {
        let entries = (try? context.fetch(FetchDescriptor<Entry>())) ?? []
        return engine.stats(for: period, entries: entries)
    }
}
