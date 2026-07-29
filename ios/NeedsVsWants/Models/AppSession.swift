import Foundation
import Observation

/// In-memory session state that must survive tab switches
/// (SwiftUI destroys a screen's @State when you switch tabs via a `switch`).
@Observable
final class AppSession {
    var selectedPeriod: Period = .day
}
