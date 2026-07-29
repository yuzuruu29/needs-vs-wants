import Foundation

enum Period: String, CaseIterable {
    case day = "DAY"
    case week = "WEEK"
    case all = "ALL"

    var label: String {
        switch self {
        case .day:  return "Day"
        case .week: return "Week"
        case .all:  return "All (35d)"
        }
    }
}
