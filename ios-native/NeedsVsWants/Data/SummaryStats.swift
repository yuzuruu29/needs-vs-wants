import Foundation

/// Aggregated totals for a period window. Value type — safe to test & compare.
struct SummaryStats: Equatable {
    var needsTotalCents: Int64 = 0
    var wantsTotalCents: Int64 = 0
    var needsCount: Int = 0
    var wantsCount: Int = 0

    var totalCents: Int64 { needsTotalCents + wantsTotalCents }
    var totalCount: Int { needsCount + wantsCount }

    /// Fraction of spend that was Needs, in [0, 1]. 0 when nothing logged.
    var needPct: Double {
        totalCents == 0 ? 0 : Double(needsTotalCents) / Double(totalCents)
    }
    var wantPct: Double {
        totalCents == 0 ? 0 : Double(wantsTotalCents) / Double(totalCents)
    }
}
