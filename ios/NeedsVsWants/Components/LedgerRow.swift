import SwiftUI

/// Single expense line used on both Log and History. The trailing cluster
/// (cost · type · delete) stays tight; item takes flexible space.
struct LedgerRow: View {
    let entry: Entry
    let symbol: String
    let onDelete: () -> Void
    var showCard: Bool = false

    private var money: String { CurrencyFormatter.format(entry.costCents, symbol: symbol) }

    var body: some View {
        let content = HStack(alignment: .center, spacing: 0) {
            Text(entry.time)
                .font(.system(size: 11).monospacedDigit())
                .tracking(0.2)
                .foregroundStyle(Color.textMuted)
                .frame(width: AppSpacing.Ledger.time, alignment: .leading)
                .lineLimit(1)
            Spacer().frame(width: AppSpacing.Ledger.gutter)
            Text(entry.item)
                .font(.system(size: 14, weight: .medium))
                .foregroundStyle(Color.textPrimary)
                .frame(maxWidth: .infinity, alignment: .leading)
                .lineLimit(1)
                .truncationMode(.tail)
            Spacer().frame(width: AppSpacing.Ledger.gutter)
            Text(money)
                .font(.system(size: CurrencyFormatter.adaptiveSize(money, base: 13)).monospacedDigit().weight(.semibold))
                .foregroundStyle(Color.textPrimary)
                .frame(width: AppSpacing.Ledger.cost, alignment: .trailing)
                .lineLimit(1)
            Spacer().frame(width: AppSpacing.Ledger.trailGutter)
            TypeBadge(type: entry.type)
            Spacer().frame(width: AppSpacing.Ledger.tightGutter)
            Button(action: onDelete) {
                Image(systemName: "trash")
                    .font(.system(size: 16))
                    .foregroundStyle(Color.danger.opacity(0.55))
                    .frame(width: AppSpacing.Ledger.delete, height: AppSpacing.Ledger.delete)
            }
        }
        .padding(.vertical, showCard ? 13 : 11)
        .padding(.horizontal, showCard ? 14 : 0)

        if showCard {
            content
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color.surfaceRaised)
                .clipShape(RoundedRectangle(cornerRadius: 12))
        } else {
            content.frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}
