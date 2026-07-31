import Foundation

/// Formats currency-immune cents into a display string per the chosen currency.
///
/// Improvement over the old `ios/` port: uses `NumberFormatter(.currency)` with
/// the currency's locale, so symbol placement, grouping, and — crucially —
/// minor-unit rules are correct. The old port hand-rolled `\(symbol) \(whole).\(c2)`
/// which produced wrong output for JPY (yen has no minor units).
enum CurrencyFormatter {

    static func format(cents: Int64, currency: CurrencyOption) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = currency.locale
        formatter.currencyCode = currency.code

        if currency == .jpy {
            // JPY (and other zero-minor currencies): whole units, no decimals.
            formatter.maximumFractionDigits = 0
            formatter.minimumFractionDigits = 0
            return formatter.string(from: NSNumber(value: cents / 100)) ?? ""
        }

        return formatter.string(from: NSNumber(value: Double(cents) / 100.0)) ?? ""
    }
}
