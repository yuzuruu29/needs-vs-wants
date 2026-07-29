import SwiftUI

/// Shared ledger row (D8: one geometry across Log + History).
/// Uses a native `Button` for delete (the old port's rectangle + tap gesture
/// was invisible to VoiceOver). Fixed column widths match `LedgerHeader`.
struct LedgerRow: View {
    let entry: Entry
    let currency: CurrencyOption
    let onDelete: () -> Void

    var body: some View {
        HStack(spacing: 0) {
            Text(entry.timeLabel)
                .font(.system(size: 11, weight: .medium))
                .foregroundStyle(AppColors.textSecondary)
                .frame(width: 48, alignment: .leading)

            Text(entry.item)
                .font(.system(size: 13))
                .foregroundStyle(AppColors.textPrimary)
                .lineLimit(1)
                .frame(maxWidth: .infinity, alignment: .leading)

            CurrencyText(cents: entry.costCents, currency: currency)
                .frame(width: 88, alignment: .trailing)

            NeedWantBadge(type: entry.type)
                .frame(width: 42, alignment: .center)

            Button(action: onDelete) {
                Image(systemName: "trash")
                    .font(.system(size: 13))
                    .foregroundStyle(AppColors.crimson.opacity(0.7))
            }
            .frame(width: 32)
            .accessibilityLabel("Delete \(entry.item)")
            .accessibilityHint("Removes this entry from the diary")
        }
        .padding(.vertical, 8)
        .accessibilityElement(children: .contain)
    }
}
