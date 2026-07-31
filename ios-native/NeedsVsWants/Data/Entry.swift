import Foundation
import SwiftData

/// Single source of truth for one logged expense.
///
/// Improvement over the old `ios/` port: only `dateUtc` is stored. The old port
/// also stored denormalized `date` ("yyyy-MM-dd") and `time` ("HH:mm") strings,
/// which could drift from `dateUtc`. Here those are **computed** from `dateUtc`,
/// so there is exactly one representation of the moment.
@Model
final class Entry {
    var id: UUID
    var dateUtc: Date
    var item: String
    var costCents: Int64          // currency-immune (D2)
    var typeRaw: String           // "NEED" | "WANT"

    init(id: UUID = UUID(), dateUtc: Date, item: String, costCents: Int64, type: EntryType) {
        self.id = id
        self.dateUtc = dateUtc
        self.item = item
        self.costCents = costCents
        self.typeRaw = type.rawValue
    }

    var type: EntryType { EntryType(rawValue: typeRaw) ?? .need }

    /// "yyyy-MM-dd" grouping key — computed, never stored.
    var dayKey: String { Self.dayFormatter.string(from: dateUtc) }
    /// "HH:mm" display label — computed, never stored.
    var timeLabel: String { Self.timeFormatter.string(from: dateUtc) }

    // POSIX locale keeps grouping/output stable regardless of device locale.
    static let dayFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "yyyy-MM-dd"; f.locale = Locale(identifier: "en_US_POSIX"); return f
    }()
    static let timeFormatter: DateFormatter = {
        let f = DateFormatter(); f.dateFormat = "HH:mm"; f.locale = Locale(identifier: "en_US_POSIX"); return f
    }()
}
