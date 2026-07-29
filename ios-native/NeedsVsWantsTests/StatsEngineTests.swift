import XCTest
@testable import NeedsVsWants

final class StatsEngineTests: XCTestCase {

    /// Fixed "today" so tests are deterministic regardless of when CI runs.
    private var calendar: Calendar {
        var c = Calendar(identifier: .gregorian)
        c.timeZone = TimeZone(identifier: "UTC")!
        return c
    }
    private var now: Date {
        calendar.date(from: DateComponents(year: 2026, month: 7, day: 29, hour: 12))!
    }

    private func entry(_ offsetDays: Int, _ cents: Int64, _ type: EntryType) -> Entry {
        Entry(dateUtc: calendar.date(byAdding: .day, value: offsetDays, to: now)!,
              item: "x", costCents: cents, type: type)
    }

    func test_day_window_only_today() {
        let engine = StatsEngine(calendar: calendar, now: now)
        let entries = [entry(0, 100, .need), entry(-1, 200, .want)]
        let s = engine.stats(for: .day, entries: entries)
        XCTAssertEqual(s.totalCents, 100)
        XCTAssertEqual(s.needsCount, 1)
        XCTAssertEqual(s.wantsCount, 0)
    }

    func test_week_includes_today_and_prior_six_days() {
        let engine = StatsEngine(calendar: calendar, now: now)
        let entries = [
            entry(0, 100, .need),
            entry(-6, 200, .want),
            entry(-7, 999, .need)   // outside the 7-day inclusive window
        ]
        let s = engine.stats(for: .week, entries: entries)
        XCTAssertEqual(s.totalCents, 300)
        XCTAssertEqual(s.needsCount, 1)
        XCTAssertEqual(s.wantsCount, 1)
    }

    func test_all_window_is_35_days() {
        let engine = StatsEngine(calendar: calendar, now: now)
        let entries = [
            entry(-34, 100, .need),   // included (start of window)
            entry(-35, 999, .want)    // excluded
        ]
        let s = engine.stats(for: .all, entries: entries)
        XCTAssertEqual(s.totalCents, 100)
    }

    func test_need_pct_is_fraction_not_percent() {
        let engine = StatsEngine(calendar: calendar, now: now)
        let s = engine.stats(for: .day, entries: [entry(0, 300, .need), entry(0, 100, .want)])
        XCTAssertEqual(s.needPct, 0.75, accuracy: 0.0001)
    }

    func test_empty_window_zero_division_safe() {
        let engine = StatsEngine(calendar: calendar, now: now)
        let s = engine.stats(for: .day, entries: [])
        XCTAssertEqual(s.needPct, 0)
        XCTAssertEqual(s.totalCents, 0)
    }
}
