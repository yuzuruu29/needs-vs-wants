import Foundation

enum CurrencyOption: String, CaseIterable, Codable, Identifiable {
    case php, usd, eur, jpy, sgd

    var id: String { rawValue }
    var code: String { rawValue.uppercased() }

    var symbol: String {
        switch self {
        case .php: return "₱"
        case .usd: return "$"
        case .eur: return "€"
        case .jpy: return "¥"
        case .sgd: return "S$"
        }
    }

    var displayName: String {
        switch self {
        case .php: return "₱ PHP"
        case .usd: return "$ USD"
        case .eur: return "€ EUR"
        case .jpy: return "¥ JPY"
        case .sgd: return "S$ SGD"
        }
    }

    /// Locale drives correct decimal/grouping and currency symbol placement.
    var locale: Locale {
        switch self {
        case .php: return Locale(identifier: "en_PH")
        case .usd: return Locale(identifier: "en_US")
        case .eur: return Locale(identifier: "en_IE")
        case .jpy: return Locale(identifier: "ja_JP")
        case .sgd: return Locale(identifier: "en_SG")
        }
    }

    static let `default`: CurrencyOption = .php
}
