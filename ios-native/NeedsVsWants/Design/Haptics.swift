import UIKit

/// Centralized haptic feedback. The old `ios/` port had zero haptics — this is
/// a deliberate polish addition.
///
/// - seal:   medium impact when an entry auto-seals (the "stamp" feel)
/// - warn:   notification warning on delete / 20-cap reached / wipe
/// - success: notification success on "start new sheet" / wipe complete
enum Haptics {
    static func seal() {
        UIImpactFeedbackGenerator(style: .medium).impactOccurred()
    }
    static func warn() {
        UINotificationFeedbackGenerator().notificationOccurred(.warning)
    }
    static func success() {
        UINotificationFeedbackGenerator().notificationOccurred(.success)
    }
}
