import XCTest
import SwiftData
@testable import NeedsVsWants

@MainActor
final class LogViewModelTests: XCTestCase {

    private func makeVM() throws -> LogViewModel {
        let config = ModelConfiguration(isStoredInMemoryOnly: true)
        let container = try ModelContainer(for: Entry.self, configurations: config)
        let repo = EntryRepository(context: container.mainContext)
        return LogViewModel(repo: repo)
    }

    func test_empty_input_cannot_seal() throws {
        let vm = try makeVM()
        XCTAssertFalse(vm.canSeal)
        XCTAssertFalse(vm.sealIfPossible())
    }

    func test_partial_input_cannot_seal() throws {
        let vm = try makeVM()
        vm.item = "Coffee"
        vm.costText = "3.50"
        // type not set
        XCTAssertFalse(vm.canSeal)
    }

    func test_valid_input_seals_and_resets() throws {
        let vm = try makeVM()
        vm.item = "Coffee"
        vm.costText = "3.50"
        vm.type = .want

        XCTAssertTrue(vm.canSeal)
        XCTAssertTrue(vm.sealIfPossible())

        // Form resets after seal
        XCTAssertEqual(vm.item, "")
        XCTAssertEqual(vm.costText, "")
        XCTAssertNil(vm.type)
        XCTAssertEqual(vm.sheetCount, 1)
    }

    func test_cost_parses_comma_decimal() throws {
        let vm = try makeVM()
        vm.costText = "3,50"   // EU decimal separator
        XCTAssertEqual(vm.costCents, 350)
    }

    func test_cost_rejects_zero_and_negative() throws {
        let vm = try makeVM()
        vm.costText = "0"
        XCTAssertNil(vm.costCents)

        vm.costText = "-5.00"
        XCTAssertNil(vm.costCents)
    }

    func test_20_cap_blocks_seal() throws {
        let vm = try makeVM()
        // Fill to 20
        for i in 0..<20 {
            _ = vm.repo.insert(item: "x\(i)", costCents: 100, type: .need)
        }
        XCTAssertTrue(vm.isSheetFull)

        // Even valid input can't seal
        vm.item = "Coffee"
        vm.costText = "3.50"
        vm.type = .want
        XCTAssertTrue(vm.canSeal)
        XCTAssertFalse(vm.sealIfPossible())
    }

    func test_start_new_sheet_clears_all() throws {
        let vm = try makeVM()
        _ = vm.repo.insert(item: "a", costCents: 100, type: .need)
        _ = vm.repo.insert(item: "b", costCents: 200, type: .want)
        XCTAssertEqual(vm.sheetCount, 2)

        vm.startNewSheet()
        XCTAssertEqual(vm.sheetCount, 0)
        XCTAssertFalse(vm.isSheetFull)
    }
}
