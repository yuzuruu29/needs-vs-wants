import SwiftUI

/// Hybrid typography (locked decision): Playfair Display SC for display titles
/// (bundled, editorial premium), SF Pro for body/UI (native, Dynamic Type).
///
/// Display fonts use fixed sizes (Playfair is a display face — scaling it via
/// Dynamic Type hurts readability). Body fonts use semantic SF Pro styles that
/// automatically participate in Dynamic Type.
enum AppTypography {

    // MARK: - Display (Playfair Display SC — bundled)

    static func display(_ size: CGFloat, weight: Font.Weight = .bold) -> Font {
        let name: String
        switch weight {
        case .bold, .heavy, .black: name = "PlayfairDisplaySC-Bold"
        default:                    name = "PlayfairDisplaySC-Regular"
        }
        return .custom(name, size: size)
    }

    static let displayLarge  = display(34, weight: .bold)
    static let displayMedium = display(22, weight: .bold)
    static let displaySmall  = display(17, weight: .regular)

    // MARK: - Body (SF Pro — system, Dynamic Type compliant)

    static let eyebrow = Font.system(size: 11, weight: .semibold).smallCaps()
    static let body    = Font.body
    static let caption = Font.caption
    static let money   = Font.body.monospacedDigit()
    static let moneySmall = Font.system(.caption, design: .default).monospacedDigit()
}
