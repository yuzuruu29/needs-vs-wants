import XCTest
import SwiftData
@testable import NeedsVsWants

@MainActor
final class EntryRepositoryTests: XCTestCase {

    /// In-memory container so tests never touch disk.
    private func makeRepo() throws -> EntryRepository {
        let config = ModelConfiguration(isStoredInMemoryOnly: true)
        let container = try ModelContainer(for: Entry.self, configurations: config)
        return EntryRepository(context: container.mainContext)
    }

    func test_insert_round_trip_and_count() throws {
        let repo = try makeRepo()
        let result = repo.insert(item: "Coffee", costCents: 250, type: .want)
        switch result { case .success: break; case .failure(let e): XCTFail("insert failed: \(e)") }
        XCTAssertEqual(repo.sheetCount, 1)
    }

    func test_purge_removes_entries_older_than_35_days() throws {
        let repo = try makeRepo()
        let old = Calendar.current.date(byAdding: .day, value: -40, to: Date())!
        _ = repo.insertAt(date: old, item: "stale", costCents: 100, type: .need)
        repo.purgeOlderThan(days: 35)
        XCTAssertEqual(repo.sheetCount, 0)
    }

    func test_purge_keeps_entries_within_35_days() throws {
        let repo = try makeRepo()
        let recent = Calendar.current.date(byAdding: .day, value: -10, to: Date())!
        _ = repo.insertAt(date: recent, item: "fresh", costCents: 100, type: .need)
        repo.purgeOlderThan(days: 35)
        XCTAssertEqual(repo.sheetCount, 1)
    }

    func test_sheet_full_at_20_total_parity() throws {
        // D4/Android parity: the cap counts TOTAL entries, not per-day.
        let repo = try makeRepo()
        for i in 0..<20 {
            _ = repo.insert(item: "x\(i)", costCents: 100, type: .need)
        }
        XCTAssertTrue(repo.isSheetFull)
    }

    func test_deleteAll_clears_sheet() throws {
        let repo = try makeRepo()
        _ = repo.insert(item: "a", costCents: 100, type: .need)
        _ = repo.insert(item: "b", costCents: 200, type: .want)
        _ = repo.deleteAll()
        XCTAssertEqual(repo.sheetCount, 0)
    }

    func test_stats_aggregates_by_period() throws {
        let repo = try makeRepo()
        _ = repo.insert(item: "need", costCents: 300, type: .need)
        _ = repo.insert(item: "want", costCents: 100, type: .want)
        let s = repo.stats(for: .day, engine: StatsEngine())
        XCTAssertEqual(s.totalCents, 400)
        XCTAssertEqual(s.needPct, 0.75, accuracy: 0.0001)
    }
}
