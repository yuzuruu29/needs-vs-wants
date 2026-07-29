import SwiftUI

struct SettingsView: View {
    @Environment(EntryRepository.self) private var repo
    @Environment(AppModel.self) private var appModel

    @AppStorage("currency") private var currencyRaw = CurrencyOption.default.rawValue
    @AppStorage("hasOnboarded") private var hasOnboarded = false

    @State private var showWipeAlert = false

    private var currency: CurrencyOption {
        CurrencyOption(rawValue: currencyRaw) ?? .default
    }

    var body: some View {
        NavigationStack {
            Form {
                Section("Currency") {
                    Picker("Currency", selection: $currencyRaw) {
                        ForEach(CurrencyOption.allCases) { c in
                            Text(c.displayName).tag(c.rawValue)
                        }
                    }
                    .pickerStyle(.inline)
                    .labelsHidden()
                }

                Section("Data") {
                    Button(role: .destructive) {
                        showWipeAlert = true
                    } label: {
                        Label("Wipe diary", systemImage: "trash")
                    }
                    .accessibilityHint("Deletes all entries permanently")
                }

                Section("About") {
                    LabeledContent("Version", value: "1.0.0")
                    Button {
                        appModel.showOnboarding = true
                    } label: {
                        Label("How it works", systemImage: "questionmark.circle")
                    }
                }
            }
            .navigationTitle("Settings")
            .alert("Wipe entire diary?", isPresented: $showWipeAlert) {
                Button("Cancel", role: .cancel) {}
                Button("Wipe all", role: .destructive) {
                    _ = repo.deleteAll()
                    Haptics.warn()
                }
            } message: {
                Text("This deletes all logged expenses. This cannot be undone.")
            }
        }
    }
}
