import CoreGraphics

enum AppSpacing {
    static let xs: CGFloat = 6
    static let sm: CGFloat = 10
    static let md: CGFloat = 14
    static let lg: CGFloat = 18
    static let xl: CGFloat = 20
    static let screenH: CGFloat = 20

    // Shared ledger column geometry (TIME/ITEM/COST/TYPE never drift).
    enum Ledger {
        static let time: CGFloat = 48
        static let cost: CGFloat = 88
        static let type: CGFloat = 42
        static let delete: CGFloat = 32
        static let gutter: CGFloat = 10
        static let trailGutter: CGFloat = 8
        static let tightGutter: CGFloat = 6
    }
}
