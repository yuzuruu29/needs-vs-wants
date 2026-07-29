import SwiftUI

/// @Observable navigation coordinator — replaces the old port's NotificationCenter
/// tab-switching and opacity-toggled ZStack. Native TabView uses this for selection.
@MainActor
@Observable
final class AppModel {
    enum Tab: Hashable, CaseIterable {
        case summary, log, history, settings

        var label: String {
            switch self {
            case .summary:  return "Summary"
            case .log:      return "Log"
            case .history:  return "History"
            case .settings: return "Settings"
            }
        }

        var icon: String {
            switch self {
            case .summary:  return "house"
            case .log:      return "cart"
            case .history:  return "clock"
            case .settings: return "gearshape"
            }
        }
    }

    var selectedTab: Tab = .summary
    var showOnboarding = false

    func switchToLog() { selectedTab = .log }
}
