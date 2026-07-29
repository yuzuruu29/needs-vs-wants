import XCTest
import SwiftData
@testable import NeedsVsWants

@MainActor
final class SummaryViewModelTests: XCTestCase {

    private func makeVM(seedEntries: [(String, Int64, EntryType)] = []) throws -> SummaryViewModel {
        let config = ModelConfiguration(isStoredInMemoryOnly: true)
        let container = try ModelContainer(for: Entry.self, configurations: config)
        let repo = EntryRepository(context: container.mainContext)
        for (item, cents, type) in seedEntries {
            _ = repo.insert(item: item, costCents: cents, type: type)
        }
        return SummaryViewModel(repo: repo)
    }

    func test_default_period_is_day() throws {
        let vm = try makeVM()
        XCTAssertEqual(vm.period, .day)
    }

    func test_period_switch_updates_stats() throws {
        let vm = try makeVM(seedEntries: [
            ("today need", 300, .need),
            ("today want", 100, .want),
        ])
        // Day window should see both (inserted at "now")
        let dayStats = vm.stats
        XCTAssertEqual(dayStats.totalCents, 400)

        // All window (35d) should also see both
        vm.period = .all
        let allStats = vm.stats
        XCTAssertEqual(allStats.totalCents, 400)
    }

    func test_range_caption_is_nonempty() throws {
        let vm = try makeVM()
        XCTAssertFalse(vm.rangeCaption.isEmpty)
    }
}
