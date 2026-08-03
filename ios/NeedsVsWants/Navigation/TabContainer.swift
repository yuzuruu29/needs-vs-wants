import SwiftUI

/// Bottom tab navigation styled as a floating pill to echo the Android nav.
/// Screens are kept mounted (toggled by opacity, not conditionally removed) so
/// each screen's @State — e.g. the active Log draft — survives tab switches.
struct TabContainer: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings
    @Environment(AppSession.self) private var session

    @State private var selected: Tab = .summary
    @State private var showInstructions = false

    enum Tab: Hashable, CaseIterable {
        case summary, log, advisor, history, settings
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            ZStack {
                SummaryView(showInstructions: $showInstructions)
                    .padding(.bottom, 84)
                    .opacity(selected == .summary ? 1 : 0)
                    .allowsHitTesting(selected == .summary)

                LogView()
                    .padding(.bottom, 84)
                    .opacity(selected == .log ? 1 : 0)
                    .allowsHitTesting(selected == .log)

                AdvisorView()
                    .padding(.bottom, 84)
                    .opacity(selected == .advisor ? 1 : 0)
                    .allowsHitTesting(selected == .advisor)

                HistoryView()
                    .padding(.bottom, 84)
                    .opacity(selected == .history ? 1 : 0)
                    .allowsHitTesting(selected == .history)

                SettingsView(showInstructions: $showInstructions)
                    .padding(.bottom, 84)
                    .opacity(selected == .settings ? 1 : 0)
                    .allowsHitTesting(selected == .settings)
            }

            floatingPill
        }
        .ignoresSafeArea(edges: .bottom)
        .background(Color.surface)
        .sheet(isPresented: $showInstructions) {
            InstructionsView { showInstructions = false }
        }
        .onReceive(NotificationCenter.default.publisher(for: .switchToLog)) { _ in
            withAnimation(.easeOut(duration: 0.18)) { selected = .log }
        }
        .task {
            if settings.firstLaunch {
                showInstructions = true
            }
        }
    }

    private var floatingPill: some View {
        HStack(spacing: 0) {
            ForEach(Tab.allCases, id: \.self) { tab in
                pill(tab)
            }
        }
        .padding(6)
        .background(Color.surfaceCard)
        .overlay(RoundedRectangle(cornerRadius: 20).stroke(Color.divider, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .padding(.horizontal, 16)
        .padding(.bottom, 12)
    }

    private func pill(_ tab: Tab) -> some View {
        let isSel = selected == tab
        return Button {
            withAnimation(.easeOut(duration: 0.18)) { selected = tab }
        } label: {
            HStack(spacing: 8) {
                Image(systemName: isSel ? tab.filledIcon : tab.outlineIcon)
                    .font(.system(size: 22))
                    .foregroundStyle(isSel ? Color.crimson : Color.textSecondary)
                if isSel {
                    Text(tab.label)
                        .font(.system(size: 12, weight: .semibold))
                        .tracking(1)
                        .foregroundStyle(Color.crimson)
                }
            }
            .padding(.horizontal, isSel ? 18 : 22)
            .padding(.vertical, 10)
            .background(isSel ? Color.crimson.opacity(0.12) : Color.clear)
            .clipShape(RoundedRectangle(cornerRadius: 14))
        }
    }
}

extension TabContainer.Tab {
    var label: String {
        switch self {
        case .summary: return "Summary"
        case .log: return "Log"
        case .advisor: return "Advisor"
        case .history: return "History"
        case .settings: return "Settings"
        }
    }
    var filledIcon: String {
        switch self {
        case .summary: return "house.fill"
        case .log: return "cart.fill"
        case .advisor: return "lightbulb.fill"
        case .history: return "clock.fill"
        case .settings: return "gearshape.fill"
        }
    }
    var outlineIcon: String {
        switch self {
        case .summary: return "house"
        case .log: return "cart"
        case .advisor: return "lightbulb"
        case .history: return "clock"
        case .settings: return "gearshape"
        }
    }
}
