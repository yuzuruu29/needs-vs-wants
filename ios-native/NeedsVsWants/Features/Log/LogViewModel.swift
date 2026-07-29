import SwiftUI
import SwiftData

/// Auto-seal state machine for the Log screen.
///
/// D5: Date/Time stamp the moment Item + Cost + Type are all valid.
/// Sealing IS saving — the entry is inserted immediately, then the form resets.
/// D4: 20-cap counts TOTAL entries (Android parity).
@MainActor
@Observable
final class LogViewModel {
    var item = ""
    var costText = ""
    var type: EntryType? = nil

    private let repo: EntryRepository

    init(repo: EntryRepository) {
        self.repo = repo
    }

    // MARK: - Computed state

    /// Parses the cost text into cents. Accepts "12.50", "12,50" (EU), "12".
    var costCents: Int64? {
        let cleaned = costText.trimmingCharacters(in: .whitespaces)
            .replacingOccurrences(of: ",", with: ".")
        guard let value = Double(cleaned), value > 0 else { return nil }
        return Int64((value * 100).rounded())
    }

    var canSeal: Bool {
        !item.trimmingCharacters(in: .whitespaces).isEmpty
            && costCents != nil
            && type != nil
    }

    var isSheetFull: Bool { repo.isSheetFull }
    var sheetCount: Int { repo.sheetCount }

    // MARK: - Actions

    /// Seals (saves) the entry if valid and sheet isn't full. Returns true if sealed.
    @discardableResult
    func sealIfPossible() -> Bool {
        guard canSeal, !isSheetFull,
              let cents = costCents, let t = type else { return false }

        let result = repo.insert(
            item: item.trimmingCharacters(in: .whitespaces),
            costCents: cents,
            type: t
        )
        if case .success = result {
            Haptics.seal()
            reset()
            return true
        }
        return false
    }

    func reset() {
        item = ""
        costText = ""
        type = nil
    }

    /// "Start new sheet" handoff (D4): clears all entries so a new 20-row sheet can begin.
    func startNewSheet() {
        _ = repo.deleteAll()
        Haptics.success()
    }
}
