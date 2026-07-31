import Foundation

/// Pure, deterministic aggregation of entries over a period window.
///
/// Separated from the repository so it can be unit-tested with plain `Entry`
/// arrays and no SwiftData container.
struct StatsEngine {
    let calendar: Calendar
    let now: Date

    init(calendar: Calendar = .current, now: Date = Date()) {
        self.calendar = calendar
        self.now = now
    }

    /// Inclusive start of the window for a period.
    /// - day: start of today
    /// - week: today minus 6 days (7-day inclusive window)
    /// - all:  today minus 34 days (35-day inclusive window, matches retention)
    func startOf(_ period: Period) -> Date {
        let today = calendar.startOfDay(for: now)
        switch period {
        case .day:  return today
        case .week: return calendar.date(byAdding: .day, value: -6, to: today)!
        case .all:  return calendar.date(byAdding: .day, value: -34, to: today)!
        }
    }

    func stats(for period: Period, entries: [Entry]) -> SummaryStats {
        let since = startOf(period)
        var s = SummaryStats()
        for e in entries where e.dateUtc >= since {
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

    /// "Jul 29" or "Jul 23 – Jul 29" caption for the period rotor.
    func rangeCaption(_ period: Period) -> String {
        let f = DateFormatter()
        f.dateFormat = "MMM d"
        f.locale = Locale(identifier: "en_US_POSIX")
        switch period {
        case .day:  return f.string(from: now)
        case .week, .all:
            return "\(f.string(from: startOf(period))) – \(f.string(from: now))"
        }
    }
}
