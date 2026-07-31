import SwiftUI

/// Shared ledger column header (D8: one geometry across Log + History).
/// Time 48 / Cost 88 / Type 42 — prevents TYPE label wrapping on narrow widths.
struct LedgerHeader: View {
    var body: some View {
        HStack(spacing: 0) {
            Eyebrow(text: "TIME").frame(width: 48, alignment: .leading)
            Eyebrow(text: "ITEM").frame(maxWidth: .infinity, alignment: .leading)
            Eyebrow(text: "COST").frame(width: 88, alignment: .trailing)
            Eyebrow(text: "TYPE").frame(width: 42, alignment: .center)
            Color.clear.frame(width: 32)   // delete column gutter
        }
        .padding(.vertical, 6)
    }
}
