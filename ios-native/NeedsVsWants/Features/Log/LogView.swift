import SwiftUI
import SwiftData

struct LogView: View {
    @Environment(EntryRepository.self) private var repo
    @AppStorage("currency") private var currencyRaw = CurrencyOption.default.rawValue

    @State private var vm = LogViewModel.placeholder
    @FocusState private var focus: Field?

    @Query(sort: \Entry.dateUtc, order: .reverse) private var entries: [Entry]

    @State private var showDeleteAlert = false
    @State private var entryToDelete: Entry?

    private var currency: CurrencyOption {
        CurrencyOption(rawValue: currencyRaw) ?? .default
    }

    enum Field: Hashable { case item, cost }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 20) {
                    headerSection
                    if vm.isSheetFull {
                        sheetFullPrompt
                    } else {
                        activeEntryCard
                    }
                    sealedLedger
                }
                .padding(.horizontal, 20)
                .padding(.bottom, 40)
            }
            .background(AppColors.surface)
            .navigationTitle("")
            .toolbar {
                ToolbarItem(placement: .keyboard) {
                    Button("Done") { focus = nil }
                }
            }
            .onAppear {
                vm.attach(repo: repo)
            }
        }
    }

    // MARK: - Header

    private var headerSection: some View {
        VStack(spacing: 4) {
            HStack {
                Eyebrow(text: "TODAY · \(Entry.dayFormatter.string(from: Date()))")
                Spacer()
                Eyebrow(text: "SHEET \(vm.sheetCount) / 20")
            }
            Text("LOG")
                .font(AppTypography.displayMedium)
                .foregroundStyle(AppColors.textPrimary)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
    }

    // MARK: - Active entry card

    private var activeEntryCard: some View {
        VStack(spacing: 16) {
            VStack(alignment: .leading, spacing: 8) {
                Eyebrow(text: "ITEM")
                TextField("What did you buy?", text: $vm.item)
                    .textFieldStyle(.roundedBorder)
                    .focused($focus, equals: .item)
                    .submitLabel(.next)
                    .onSubmit { focus = .cost }
                    .accessibilityLabel("Item name")
            }

            VStack(alignment: .leading, spacing: 8) {
                Eyebrow(text: "COST")
                TextField("0.00", text: $vm.costText)
                    .textFieldStyle(.roundedBorder)
                    .keyboardType(.decimalPad)
                    .focused($focus, equals: .cost)
                    .accessibilityLabel("Cost")
            }

            VStack(alignment: .leading, spacing: 8) {
                Eyebrow(text: "TYPE")
                HStack(spacing: 10) {
                    typeChip(.need, "Need", AppColors.need)
                    typeChip(.want, "Want", AppColors.want)
                }
            }
        }
        .padding(16)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .overlay(RoundedRectangle(cornerRadius: 14).stroke(AppColors.divider, lineWidth: 1))
        .onChange(of: vm.canSeal) { _, canSeal in
            if canSeal {
                vm.sealIfPossible()
            }
        }
    }

    private func typeChip(_ t: EntryType, _ label: String, _ color: Color) -> some View {
        let isSelected = vm.type == t
        return Button {
            vm.type = t
            if vm.canSeal { vm.sealIfPossible() }
        } label: {
            Text(label)
                .font(.system(size: 14, weight: .semibold))
                .foregroundStyle(isSelected ? .white : color)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 12)
                .background(isSelected ? color : color.opacity(0.12))
                .clipShape(RoundedRectangle(cornerRadius: 10))
        }
        .accessibilityLabel(label)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }

    // MARK: - 20-cap prompt

    private var sheetFullPrompt: some View {
        VStack(spacing: 12) {
            Image(systemName: "tray.full")
                .font(.system(size: 32))
                .foregroundStyle(AppColors.gold)
            Text("Sheet is full")
                .font(AppTypography.displaySmall)
            Text("You've logged 20 entries. Start a new sheet to continue.")
                .font(AppTypography.body)
                .foregroundStyle(AppColors.textSecondary)
                .multilineTextAlignment(.center)
            PrimaryButton(title: "Start new sheet") {
                vm.startNewSheet()
            }
        }
        .padding(24)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .overlay(RoundedRectangle(cornerRadius: 14).stroke(AppColors.gold, lineWidth: 1))
    }

    // MARK: - Sealed ledger

    private var sealedLedger: some View {
        VStack(spacing: 0) {
            LedgerHeader()
            if entries.isEmpty {
                Text("Sealed entries appear here.")
                    .font(AppTypography.caption)
                    .foregroundStyle(AppColors.textSecondary)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.vertical, 30)
            } else {
                ForEach(entries) { entry in
                    LedgerRow(
                        entry: entry,
                        currency: currency,
                        onDelete: {
                            entryToDelete = entry
                            showDeleteAlert = true
                        }
                    )
                    Divider().background(AppColors.divider)
                }
            }
        }
        .alert("Delete entry?", isPresented: $showDeleteAlert) {
            Button("Cancel", role: .cancel) {}
            Button("Delete", role: .destructive) {
                if let entry = entryToDelete {
                    _ = repo.delete(entry)
                    Haptics.warn()
                }
            }
        } message: {
            if let entry = entryToDelete {
                Text("Remove \(entry.item)?")
            }
        }
    }
}
