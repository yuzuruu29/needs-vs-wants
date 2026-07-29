import SwiftUI

/// Log screen: one active entry, a ledger of sealed rows, 20-cap handoff.
/// Sealing IS saving — the row is written to SwiftData the instant
/// item+cost+type are valid, exactly like the Android reference.
struct LogView: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings

    @State private var item = ""
    @State private var cost = ""
    @State private var type: EntryType? = nil
    @State private var deleteTarget: Entry? = nil

    private var todayLabel: String {
        let f = DateFormatter(); f.dateFormat = "MMM d, yyyy"; return f.string(from: Date())
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            // Header
            VStack(alignment: .leading, spacing: 6) {
                Eyebrow("TODAY  ·  \(todayLabel)", color: .crimson)
                HStack(alignment: .bottom) {
                    Text("LOG")
                        .font(AppFont.title)
                        .foregroundStyle(Color.textPrimary)
                    Spacer()
                    VStack(alignment: .trailing, spacing: 2) {
                        Eyebrow("SHEET", color: .textMuted, size: 10)
                        Text("\(store.sheetCount) / 20")
                            .font(.system(size: 15, weight: .semibold))
                            .foregroundStyle(store.sheetCount >= 18 ? Color.danger : Color.textPrimary)
                    }
                }
                GiltRule(width: 40)
            }
            .padding(.horizontal, AppSpacing.xl)
            .padding(.top, AppSpacing.xl)

            if store.isSheetFull {
                fullSheetPrompt
                    .padding(.horizontal, AppSpacing.xl)
                    .padding(.top, AppSpacing.lg)
            } else {
                activeEntryCard
                    .padding(.horizontal, AppSpacing.xl)
                    .padding(.top, AppSpacing.lg)
            }

            Spacer().frame(height: AppSpacing.lg)

            if !store.entries.isEmpty {
                LedgerHeader()
                    .padding(.horizontal, AppSpacing.xl + 14)
                    .padding(.vertical, 6)
                Rectangle().fill(Color.divider).frame(height: 1)
                    .padding(.horizontal, AppSpacing.xl)
            }

            ScrollView {
                if store.entries.isEmpty {
                    Spacer().frame(height: 40)
                }
                LazyVStack(spacing: 8) {
                    ForEach(Array(store.entries.reversed()), id: \.id) { entry in
                        LedgerRow(entry: entry, symbol: settings.currencySymbol,
                                  onDelete: { deleteTarget = entry }, showCard: true)
                    }
                }
                .padding(.horizontal, AppSpacing.xl)
                .padding(.vertical, 6)
            }
        }
        .background(Color.surface)
        .onChange(of: item) { _ in attemptSeal() }
        .onChange(of: cost) { _ in attemptSeal() }
        .onChange(of: type) { _ in attemptSeal() }
        .alert("Delete entry?", isPresented: Binding(
            get: { deleteTarget != nil },
            set: { if !$0 { deleteTarget = nil } }
        )) {
            Button("Cancel", role: .cancel) { deleteTarget = nil }
            Button("Delete", role: .destructive) {
                if let e = deleteTarget { store.delete(e) }
                deleteTarget = nil
            }
        } message: {
            if let e = deleteTarget {
                Text("\(e.item) — \(CurrencyFormatter.format(e.costCents, symbol: settings.currencySymbol))")
            }
        }
    }

    private var activeEntryCard: some View {
        PremiumCard {
            VStack(spacing: 14) {
                TextField("ITEM", text: $item)
                    .textFieldStyle(.plain)
                    .font(.system(size: 15))
                    .foregroundStyle(Color.textPrimary)
                    .padding(12)
                    .background(Color.surfaceSunken)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dividerStrong, lineWidth: 1))
                HStack(alignment: .center, spacing: 10) {
                    TextField("COST", text: $cost)
                        .textFieldStyle(.plain)
                        .font(.system(size: 15).monospacedDigit())
                        .keyboardType(.decimalPad)
                        .foregroundStyle(Color.textPrimary)
                        .padding(12)
                        .background(Color.surfaceSunken)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dividerStrong, lineWidth: 1))
                    TypeChip(label: "NEED", selected: type == .need, color: .need) {
                        type = .need
                    }
                    TypeChip(label: "WANT", selected: type == .want, color: .want) {
                        type = .want
                    }
                }
            }
        }
    }

    private var fullSheetPrompt: some View {
        PremiumCard {
            VStack(spacing: 8) {
                Eyebrow("SHEET COMPLETE", color: .marketGreen)
                Text("20 / 20 entries sealed")
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundStyle(Color.textPrimary)
                PrimaryButton(title: "Start new sheet", action: {})
            }
            .frame(maxWidth: .infinity)
        }
    }

    // MARK: - Auto-seal

    private func attemptSeal() {
        guard !store.isSheetFull else { return }
        let trimmed = item.trimmingCharacters(in: .whitespaces)
        guard let cents = CurrencyFormatter.parse(cost), cents > 0 else { return }
        guard let t = type else { return }
        guard !trimmed.isEmpty else { return }

        store.insert(item: trimmed, costCents: cents, type: t)
        // Reset the active card — sealing already persisted the row.
        item = ""
        cost = ""
        type = nil
    }
}

struct TypeChip: View {
    let label: String
    let selected: Bool
    let color: Color
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(label)
                .font(.system(size: 11, weight: selected ? .semibold : .regular))
                .foregroundStyle(selected ? color : Color.textSecondary)
                .padding(.horizontal, 14)
                .padding(.vertical, 13)
                .frame(minWidth: 64)
                .background(selected ? color.opacity(0.18) : Color.clear)
                .overlay(RoundedRectangle(cornerRadius: 10).stroke(selected ? color : Color.dividerStrong, lineWidth: 1))
                .clipShape(RoundedRectangle(cornerRadius: 10))
        }
    }
}
