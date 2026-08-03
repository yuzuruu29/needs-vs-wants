import SwiftUI
import SwiftData

@main
struct NeedsVsWantsApp: App {
    @State private var appModel = AppModel()
    @AppStorage("hasOnboarded") private var hasOnboarded = false

    let container: ModelContainer
    @State private var repo: EntryRepository
    @State private var didPurge = false

    init() {
        let container: ModelContainer
        do {
            container = try ModelContainer(for: Entry.self)
        } catch {
            fatalError("ModelContainer failed: \(error)")
        }
        self.container = container
        _repo = State(initialValue: EntryRepository(context: container.mainContext))
    }

    var body: some Scene {
        WindowGroup {
            TabView(selection: tabBinding) {
                SummaryView()
                    .tag(AppModel.Tab.summary)
                    .tabItem { Label(AppModel.Tab.summary.label, systemImage: AppModel.Tab.summary.icon) }

                LogView()
                    .tag(AppModel.Tab.log)
                    .tabItem { Label(AppModel.Tab.log.label, systemImage: AppModel.Tab.log.icon) }

                HistoryView()
                    .tag(AppModel.Tab.history)
                    .tabItem { Label(AppModel.Tab.history.label, systemImage: AppModel.Tab.history.icon) }

                SettingsView()
                    .tag(AppModel.Tab.settings)
                    .tabItem { Label(AppModel.Tab.settings.label, systemImage: AppModel.Tab.settings.icon) }
            }
            .tint(AppColors.accent)
            .environment(repo)
            .environment(appModel)
            .fullScreenCover(isPresented: $appModel.showOnboarding) {
                OnboardingView()
            }
            .onAppear {
                // Throttled 35-day purge — once per launch, not every foreground.
                if !didPurge {
                    repo.purgeOlderThan(days: 35)
                    didPurge = true
                }
                if !hasOnboarded {
                    appModel.showOnboarding = true
                }
            }
        }
        .modelContainer(container)
    }

    private var tabBinding: Binding<AppModel.Tab> {
        Binding(
            get: { appModel.selectedTab },
            set: { appModel.selectedTab = $0 }
        )
    }
}
