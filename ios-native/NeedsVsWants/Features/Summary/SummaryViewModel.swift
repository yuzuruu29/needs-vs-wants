import SwiftUI

/// Owns period selection and delegates stats computation to repo + engine.
/// Thin observable — no business logic here, just UI state.
@MainActor
@Observable
final class SummaryViewModel {
    var period: Period = .day

    private let repo: EntryRepository
    private let engine: StatsEngine

    init(repo: EntryRepository, engine: StatsEngine = StatsEngine()) {
        self.repo = repo
        self.engine = engine
    }

    var stats: SummaryStats { repo.stats(for: period, engine: engine) }
    var rangeCaption: String { engine.rangeCaption(period) }
}
