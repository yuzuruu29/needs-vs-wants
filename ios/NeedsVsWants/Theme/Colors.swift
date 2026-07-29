import SwiftUI

extension Color {
    /// Initialize from a 24-bit RGB hex value (e.g. 0xC8102E).
    init(hex: UInt32) {
        let r = Double((hex >> 16) & 0xFF) / 255.0
        let g = Double((hex >> 8) & 0xFF) / 255.0
        let b = Double(hex & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: 1)
    }

    // Surfaces
    static let surface        = Color(hex: 0xFAFAF7)
    static let surfaceCard    = Color(hex: 0xFFFFFF)
    static let surfaceRaised  = Color(hex: 0xF3F1EA)
    static let surfaceSunken  = Color(hex: 0xF7F4EC)
    static let divider        = Color(hex: 0xE8E5DC)
    static let dividerStrong  = Color(hex: 0xD6D2C6)

    // Brand accents
    static let crimson        = Color(hex: 0xC8102E)
    static let crimsonDeep    = Color(hex: 0xA40E25)
    static let marketGreen    = Color(hex: 0x0B6B3A)
    static let marketGreenDeep = Color(hex: 0x084F2A)
    static let gold           = Color(hex: 0xE8A92A)
    static let goldSoft       = Color(hex: 0xF4C968)
    static let goldDeep       = Color(hex: 0xB9881E)

    // Semantic tagging
    static let need   = Color(hex: 0x0B6B3A)   // need = green
    static let want   = Color(hex: 0xC8102E)   // want = red
    static let danger = Color(hex: 0xC8102E)

    // Text
    static let textPrimary   = Color(hex: 0x1A1A1A)
    static let textSecondary = Color(hex: 0x5A5A5A)
    static let textMuted     = Color(hex: 0x8A8A8A)
}
