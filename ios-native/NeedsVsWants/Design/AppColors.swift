import SwiftUI

/// Adaptive supermarket-premium palette (D7), as light/dark semantic tokens.
///
/// Light values reproduce D7 exactly (crimson #C8102E, market green #0B6B3A,
/// gold #E8A92A, warm cream #FAFAF7). Dark values are tuned variants — same
/// brand identity, readable on black. The old `ios/` port only had light mode;
/// this is the adaptive improvement the plan calls for.
enum AppColors {
    static let surface = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.10, green: 0.10, blue: 0.10, alpha: 1)
            : UIColor(red: 0.980, green: 0.980, blue: 0.969, alpha: 1) // #FAFAF7
    })

    static let surfaceCard = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.16, green: 0.16, blue: 0.17, alpha: 1)
            : UIColor.white
    })

    static let surfaceRaised = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.20, green: 0.20, blue: 0.21, alpha: 1)
            : UIColor(red: 0.953, green: 0.945, blue: 0.918, alpha: 1) // #F3F1EA
    })

    static let surfaceSunken = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.13, green: 0.13, blue: 0.14, alpha: 1)
            : UIColor(red: 0.969, green: 0.957, blue: 0.925, alpha: 1) // #F7F4EC
    })

    static let crimson = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.88, green: 0.22, blue: 0.30, alpha: 1)
            : UIColor(red: 0.784, green: 0.063, blue: 0.180, alpha: 1) // #C8102E
    })

    static let crimsonDeep = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.78, green: 0.16, blue: 0.22, alpha: 1)
            : UIColor(red: 0.643, green: 0.055, blue: 0.145, alpha: 1) // #A40E25
    })

    static let marketGreen = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.22, green: 0.72, blue: 0.45, alpha: 1)
            : UIColor(red: 0.043, green: 0.420, blue: 0.227, alpha: 1) // #0B6B3A
    })

    static let gold = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(red: 0.91, green: 0.74, blue: 0.33, alpha: 1)
            : UIColor(red: 0.910, green: 0.662, blue: 0.165, alpha: 1) // #E8A92A
    })

    static let divider = Color(uiColor: UIColor { tc in
        tc.userInterfaceStyle == .dark
            ? UIColor(white: 0.28, alpha: 1)
            : UIColor(red: 0.910, green: 0.898, blue: 0.863, alpha: 1) // #E8E5DC
    })

    static let textPrimary = Color.primary
    static let textSecondary = Color.secondary

    // MARK: - Semantic (D7: Need = green "go", Want = red "stop")
    static let need = marketGreen
    static let want = crimson
    static let accent = crimson
}
