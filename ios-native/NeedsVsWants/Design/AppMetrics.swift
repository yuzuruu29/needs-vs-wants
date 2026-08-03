import CoreGraphics

/// Shared geometry tokens — keep card/input radii consistent across screens.
/// Cards match Android `GiltCard` (16); compact chips/inputs stay at 10–12.
enum AppMetrics {
    static let cardRadius: CGFloat = 16
    static let inputRadius: CGFloat = 12
    static let chipRadius: CGFloat = 10
    static let giltRuleWidth: CGFloat = 40
    static let giltRuleHeight: CGFloat = 1.5
}
