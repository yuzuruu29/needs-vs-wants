import SwiftUI

/// Compact Need/Want badge used in ledger rows (shows "N" / "W").
struct TypeBadge: View {
    let type: EntryType

    var body: some View {
        let color: Color = type == .need ? .need : .want
        Text(type == .need ? "N" : "W")
            .font(.system(size: 11, weight: .semibold))
            .foregroundStyle(color)
            .frame(width: AppSpacing.Ledger.type, height: 28)
            .overlay(RoundedRectangle(cornerRadius: 7).stroke(color, lineWidth: 1.2))
    }
}
