import XCTest
@testable import NeedsVsWants

/// P0 smoke test — verifies the test target links against the app target.
/// Replaced by real repository/view-model tests in Task P1.
final class SmokeTests: XCTestCase {
    func test_smoke() throws {
        XCTAssertTrue(true)
    }
}
