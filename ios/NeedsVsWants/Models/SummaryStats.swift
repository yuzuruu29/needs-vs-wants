import Foundation

struct SummaryStats {
    var needsTotalCents: Int64 = 0
    var wantsTotalCents: Int64 = 0
    var needsCount: Int = 0
    var wantsCount: Int = 0

    var totalCents: Int64 { needsTotalCents + wantsTotalCents }
    var needsPct: Int { totalCents > 0 ? Int(needsTotalCents * 100 / totalCents) : 0 }
    var wantsPct: Int { totalCents > 0 ? 100 - needsPct : 0 }
}
