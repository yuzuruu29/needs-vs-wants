import SwiftUI
import SwiftData
import UIKit

@main
struct NeedsVsWantsApp: App {
    let container: ModelContainer
    @State private var entryStore: EntryStore
    @State private var settings: AppSettings
    @State private var session: AppSession

    init() {
        let schema = Schema([Entry.self])
        let config = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)
        let container: ModelContainer
        do {
            container = try ModelContainer(for: schema, configurations: [config])
        } catch {
            fatalError("Failed to create model container: \(error)")
        }
        self.container = container

        let ctx = ModelContext(container)
        _entryStore = State(initialValue: EntryStore(context: ctx))
        _settings = State(initialValue: AppSettings())
        _session = State(initialValue: AppSession())
    }

    var body: some Scene {
        WindowGroup {
            TabContainer()
                .environment(entryStore)
                .environment(settings)
                .environment(session)
                .onAppear { configureBars() }
        }
        .modelContainer(container)
    }

    private func configureBars() {
        let appearance = UITabBarAppearance()
        appearance.configureWithTransparentBackground()
        appearance.backgroundColor = .clear
        UITabBar.appearance().standardAppearance = appearance
        UITabBar.appearance().scrollEdgeAppearance = appearance

        UINavigationBar.appearance().titleTextAttributes = [
            .foregroundColor: UIColor(Color.textPrimary)
        ]
    }
}
