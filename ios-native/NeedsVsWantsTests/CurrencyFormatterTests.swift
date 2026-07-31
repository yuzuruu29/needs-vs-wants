import XCTest
@testable import NeedsVsWants

/// Verifies CurrencyFormatter — including the JPY bug fix (the old port rendered
/// "¥100.00" for yen, which has no minor units).
final class CurrencyFormatterTests: XCTestCase {

    func test_php_shows_two_decimals() {
        let s = CurrencyFormatter.format(cents: 12345, currency: .php)
        XCTAssertTrue(s.contains("123.45"), "expected 123.45 in '\(s)'")
    }

    func test_usd_shows_two_decimals() {
        let s = CurrencyFormatter.format(cents: 250, currency: .usd)
        XCTAssertTrue(s.contains("2.50"), "expected 2.50 in '\(s)'")
    }

    func test_jpy_has_no_minor_units() {
        // 1000 cents = 10 whole yen. JPY must not render decimals.
        let s = CurrencyFormatter.format(cents: 1000, currency: .jpy)
        XCTAssertTrue(s.contains("10"), "expected 10 in '\(s)'")
        XCTAssertFalse(s.contains("."), "JPY must not show decimals: '\(s)'")
    }

    func test_zero_cents() {
        let s = CurrencyFormatter.format(cents: 0, currency: .usd)
        XCTAssertTrue(s.contains("0.00"), "expected 0.00 in '\(s)'")
    }

    func test_negative_is_supported() {
        let s = CurrencyFormatter.format(cents: -500, currency: .usd)
        // Should render -5.00 (or (5.00)) — just assert the magnitude is present.
        XCTAssertTrue(s.contains("5.00"), "expected 5.00 magnitude in '\(s)'")
    }
}
