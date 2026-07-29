import SwiftUI

/// Groups entries by day for sectioned display. Pure functions — no state.
@MainActor
@Observable
final class HistoryViewModel {

    /// Named tuple so callers can use .needs / .wants instead of .0 / .1.
    struct DayTotals: Equatable {
        let needs: Int64
        let wants: Int64
    }

    /// Groups entries by dayKey (yyyy-MM-dd), newest first.
    func grouped(entries: [Entry]) -> [(key: String, entries: [Entry])] {
        let grouped = Dictionary(grouping: entries, by: { $0.dayKey })
        return grouped
            .sorted { $0.key > $1.key }
            .map { (key: $0.key, entries: $0.value) }
    }

    func dayTotals(_ entries: [Entry]) -> DayTotals {
        var needs: Int64 = 0, wants: Int64 = 0
        for e in entries {
            if e.type == .need { needs += e.costCents } else { wants += e.costCents }
        }
        return DayTotals(needs: needs, wants: wants)
    }
}
