import Foundation

enum CurrencyFormatter {
    /// Format stored cents with a display-only currency symbol.
    static func format(_ cents: Int64, symbol: String) -> String {
        let whole = cents / 100
        let c = abs(cents) % 100
        return "\(symbol) \(whole).\(String(format: "%02d", c))"
    }

    /// Parse a user-typed cost string into cents. Filters via regex (no shake).
    static func parse(_ input: String) -> Int64? {
        let cleaned = input.replacingOccurrences(of: ",", with: "")
            .trimmingCharacters(in: .whitespaces)
        guard !cleaned.isEmpty else { return nil }
        guard cleaned.range(of: #"^\d+(\.\d{0,2})?$"#, options: .regularExpression) != nil else { return nil }
        let parts = cleaned.split(separator: ".")
        guard let whole = Int64(parts[0]) else { return nil }
        let cents: Int64
        if parts.count > 1 {
            let c = String(parts[1]).padding(toLength: 2, withPad: "0", startingAt: 0)
            guard let ci = Int64(c) else { return nil }
            cents = ci
        } else {
            cents = 0
        }
        return whole * 100 + cents
    }

    /// Shrink money text so it always fits its container instead of clipping.
    static func adaptiveSize(_ text: String, base: CGFloat) -> CGFloat {
        let len = text.count
        let factor: CGFloat = len <= 9 ? 1 : len <= 12 ? 0.82 : len <= 15 ? 0.68 : len <= 19 ? 0.58 : 0.5
        return base * factor
    }
}
