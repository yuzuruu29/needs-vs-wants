import SwiftUI

/// Groups entries by day for sectioned display. Thin observable.
@MainActor
@Observable
final class HistoryViewModel {
    private let repo: EntryRepository

    init(repo: EntryRepository) {
        self.repo = repo
    }

    /// Groups entries by dayKey (yyyy-MM-dd), newest first.
    func grouped(entries: [Entry]) -> [(key: String, entries: [Entry])] {
        let grouped = Dictionary(grouping: entries, by: { $0.dayKey })
        return grouped.sorted { $0.key > $1.key }
    }

    func dayTotals(_ entries: [Entry]) -> (needs: Int64, wants: Int64) {
        var needs: Int64 = 0, wants: Int64 = 0
        for e in entries {
            if e.type == .need { needs += e.costCents } else { wants += e.costCents }
        }
        return (needs, wants)
    }
}
