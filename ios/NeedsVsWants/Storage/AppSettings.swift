import Foundation
import Observation

/// User preferences + first-launch flag. Persisted to UserDefaults.
/// Currency is display-only; stored cents are never mutated here.
@Observable
final class AppSettings {
    var currencySymbol: String {
        didSet { UserDefaults.standard.set(currencySymbol, forKey: "currency_symbol") }
    }
    var currencyCode: String {
        didSet { UserDefaults.standard.set(currencyCode, forKey: "currency_code") }
    }
    var firstLaunch: Bool {
        didSet { UserDefaults.standard.set(firstLaunch, forKey: "first_launch") }
    }

    init() {
        let d = UserDefaults.standard
        currencySymbol = d.string(forKey: "currency_symbol") ?? "₱"
        currencyCode = d.string(forKey: "currency_code") ?? "PHP"
        firstLaunch = (d.object(forKey: "first_launch") == nil) ? true : d.bool(forKey: "first_launch")
    }

    func setCurrency(symbol: String, code: String) {
        currencySymbol = symbol
        currencyCode = code
    }

    func completeFirstLaunch() {
        firstLaunch = false
    }

    /// Wipe resets currency + first-launch but keeps the diary empty (entries
    /// are deleted separately by EntryStore.deleteAll).
    func reset() {
        currencySymbol = "₱"
        currencyCode = "PHP"
        firstLaunch = true
    }
}
