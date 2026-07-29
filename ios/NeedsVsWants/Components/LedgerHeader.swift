import SwiftUI

/// Sticky column header for the sealed-entry ledger. Fixed widths keep
/// TIME/ITEM/COST/TYPE aligned and stop TYPE from wrapping.
struct LedgerHeader: View {
    var body: some View {
        HStack(alignment: .center, spacing: 0) {
            Text("TIME")
                .frame(width: AppSpacing.Ledger.time, alignment: .leading)
                .ledgerHeaderStyle()
            Spacer().frame(width: AppSpacing.Ledger.gutter)
            Text("ITEM")
                .frame(maxWidth: .infinity, alignment: .leading)
                .ledgerHeaderStyle()
            Spacer().frame(width: AppSpacing.Ledger.gutter)
            Text("COST")
                .frame(width: AppSpacing.Ledger.cost, alignment: .trailing)
                .ledgerHeaderStyle()
            Spacer().frame(width: AppSpacing.Ledger.trailGutter)
            Text("TYPE")
                .frame(width: AppSpacing.Ledger.type, alignment: .center)
                .ledgerHeaderStyle(tracking: 0.55)
            Spacer().frame(width: AppSpacing.Ledger.tightGutter)
            Spacer().frame(width: AppSpacing.Ledger.delete)
        }
    }
}

extension View {
    func ledgerHeaderStyle(tracking: CGFloat = 1.1) -> some View {
        self
            .font(.system(size: 10, weight: .semibold))
            .tracking(tracking)
            .foregroundStyle(Color.textMuted)
            .lineLimit(1)
            .fixedSize(horizontal: false, vertical: true)
    }
}
