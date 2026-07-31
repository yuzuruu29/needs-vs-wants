import Foundation

enum Period: String, CaseIterable, Hashable {
    case day, week, all

    var label: String {
        switch self {
        case .day:  return "Day"
        case .week: return "Week"
        case .all:  return "All (35d)"
        }
    }
}
