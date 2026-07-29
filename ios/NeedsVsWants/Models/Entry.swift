import Foundation
import SwiftData

@Model
final class Entry {
    var id: UUID
    var dateUtc: Date
    var date: String        // "yyyy-MM-dd" — grouping key
    var time: String        // "HH:mm" (24h local)
    var item: String
    var costCents: Int64    // currency-immune
    var typeRaw: String     // "NEED" | "WANT"

    init(
        id: UUID = UUID(),
        dateUtc: Date,
        date: String,
        time: String,
        item: String,
        costCents: Int64,
        type: EntryType
    ) {
        self.id = id
        self.dateUtc = dateUtc
        self.date = date
        self.time = time
        self.item = item
        self.costCents = costCents
        self.typeRaw = type.rawValue
    }

    var type: EntryType {
        EntryType(rawValue: typeRaw) ?? .need
    }
}

extension Entry: Identifiable {}
