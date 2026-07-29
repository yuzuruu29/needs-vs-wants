import Foundation

struct CurrencyOption: Identifiable, Hashable {
    let id = UUID()
    let symbol: String
    let code: String
    let label: String

    static let defaults: [CurrencyOption] = [
        .init(symbol: "₱", code: "PHP", label: "PHP"),
        .init(symbol: "$", code: "USD", label: "USD"),
        .init(symbol: "€", code: "EUR", label: "EUR"),
        .init(symbol: "¥", code: "JPY", label: "JPY"),
        .init(symbol: "S$", code: "SGD", label: "SGD"),
    ]
}
