import SwiftUI

/// Monospaced, adaptive money display. Uses `CurrencyFormatter` so JPY and
/// other zero-minor currencies render correctly.
struct CurrencyText: View {
    let cents: Int64
    let currency: CurrencyOption

    var body: some View {
        Text(CurrencyFormatter.format(cents: cents, currency: currency))
            .font(AppTypography.money)
            .foregroundStyle(AppColors.textPrimary)
            .lineLimit(1)
            .minimumScaleFactor(0.7)
            .accessibilityLabel(spokenLabel)
    }

    private var spokenLabel: String {
        // VoiceOver reads the formatted string; ensure it's not "123.45" but
        // the locale-aware spoken form. NumberFormatter already handles this.
        CurrencyFormatter.format(cents: cents, currency: currency)
    }
}
