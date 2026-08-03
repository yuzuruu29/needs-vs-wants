import SwiftUI

/// Max-tier Financial Advisor — parity with Android `FinancialAdvisorScreen`.
/// Chat + insight engine run offline; Max access is gated via StoreKitManager.
struct AdvisorView: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings
    @StateObject private var storeKit = StoreKitManager.shared

    @State private var chatMessages: [ChatMessage] = [
        ChatMessage(
            id: "init_welcome",
            sender: .advisor,
            text: "Hello! I am your Financial Advisor, grounded in your Google NotebookLM economic studies. Ask me anything about your budget, spending ratios, or overspend recovery!"
        )
    ]
    @State private var inputText = ""
    @State private var showPaywall = false

    private var hasMaxAccess: Bool { storeKit.isMax }

    private var insight: AdvisorInsight {
        FinancialAdvisorEngine.generateInsight(
            entries: store.entries,
            currencySymbol: settings.currencySymbol,
            dailyBudgetCents: nil
        )
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                header
                sourceOfTruthBanner
                if !hasMaxAccess {
                    maxLockBanner
                }
                insightCard
                chatCard
                notebooksCard
            }
            .padding(16)
            .padding(.bottom, 24)
        }
        .background(Color.surface)
        .sheet(isPresented: $showPaywall) {
            PaywallView()
        }
    }

    // MARK: - Sections

    private var header: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text("FINANCIAL ADVISOR")
                    .font(.system(size: 11, weight: .bold))
                    .tracking(2)
                    .foregroundStyle(Color.gold)
                Spacer()
                Text("MAX TIER")
                    .font(.system(size: 10, weight: .bold))
                    .foregroundStyle(Color.crimson)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)
                    .background(Color.crimson.opacity(0.15))
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
            Text("Grounded in Economic Studies")
                .font(.system(size: 24, weight: .bold))
                .foregroundStyle(Color.textPrimary)
        }
    }

    private var sourceOfTruthBanner: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("SINGLE SOURCE OF TRUTH")
                .font(.system(size: 11, weight: .bold))
                .foregroundStyle(Color.marketGreen)
            Text(FinancialAdvisorEngine.sourceOfTruthTitle)
                .font(.system(size: 16, weight: .bold))
                .foregroundStyle(Color.textPrimary)
            Text("All recommendations are strictly verified against your Google NotebookLM economic study notebooks.")
                .font(.system(size: 13))
                .foregroundStyle(Color.textSecondary)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.surfaceCard)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color.gold.opacity(0.5), lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private var maxLockBanner: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("MAX TIER FEATURE LOCKED")
                .font(.system(size: 11, weight: .bold))
                .foregroundStyle(Color.crimson)
            Text("Unlock AI Financial Advisor")
                .font(.system(size: 16, weight: .bold))
                .foregroundStyle(Color.textPrimary)
            Text("Upgrade to Max Tier to enable real-time conversational budget analysis grounded in your Google NotebookLM economic studies.")
                .font(.system(size: 13))
                .foregroundStyle(Color.textSecondary)
            Button {
                showPaywall = true
            } label: {
                Text("View Max plans")
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundStyle(.white)
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)
                    .background(Color.crimson)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
            }
            .padding(.top, 4)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.crimson.opacity(0.08))
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color.crimson.opacity(0.4), lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private var insightCard: some View {
        let i = insight
        return VStack(alignment: .leading, spacing: 8) {
            Text(i.isWarning ? "ADVISOR ALERT" : "ADVISOR RECOMMENDATION")
                .font(.system(size: 11, weight: .bold))
                .foregroundStyle(i.isWarning ? Color.crimson : Color.marketGreen)
            Text(i.headline)
                .font(.system(size: 18, weight: .bold))
                .foregroundStyle(Color.textPrimary)
            Text(i.advice)
                .font(.system(size: 14))
                .foregroundStyle(Color.textPrimary)
            Divider().overlay(Color.gold.opacity(0.3))
            Text("CITATIONS & FOOTNOTES")
                .font(.system(size: 11, weight: .bold))
                .foregroundStyle(Color.gold)
            Text("• \(i.citation.title)")
                .font(.system(size: 13, weight: .bold))
                .foregroundStyle(Color.textPrimary)
            Text(i.citation.section)
                .font(.system(size: 12))
                .foregroundStyle(Color.textSecondary)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.surfaceCard)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(
                    i.isWarning ? Color.crimson.opacity(0.6) : Color.marketGreen.opacity(0.6),
                    lineWidth: 1
                )
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private var chatCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text("ACCESSIBLE AI CHATBOT")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundStyle(Color.gold)
                Spacer()
                Text("Voice / Text")
                    .font(.system(size: 10))
                    .foregroundStyle(Color.textSecondary)
            }

            ForEach(chatMessages) { msg in
                chatBubble(msg)
            }

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 6) {
                    quickChip("Can I buy a Want today?") {
                        send("Can I buy a Want item today?")
                    }
                    quickChip("Overspend status") {
                        send("What is my overspend status?")
                    }
                    quickChip("Need ratio") {
                        send("How is my Need to Want ratio?")
                    }
                }
            }

            HStack(spacing: 8) {
                TextField("Ask your Advisor...", text: $inputText)
                    .textFieldStyle(.roundedBorder)
                    .disabled(!hasMaxAccess)
                Button {
                    send(inputText)
                    inputText = ""
                } label: {
                    Image(systemName: "paperplane.fill")
                        .foregroundStyle(.white)
                        .frame(width: 44, height: 44)
                        .background(Color.marketGreen)
                        .clipShape(Circle())
                }
                .disabled(inputText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty || !hasMaxAccess)
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .opacity(hasMaxAccess ? 1 : 0.55)
    }

    private var notebooksCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("ECONOMIC STUDY NOTEBOOKS")
                .font(.system(size: 11, weight: .bold))
                .foregroundStyle(Color.gold)
            studyRow("Notebook #1", "Budgetary Equilibrium & Need/Want Ratio")
            studyRow("Notebook #2", "Real-Time Transaction Behavioral Control")
            studyRow("Notebook #3", "Impulse Recovery & Compensatory Sinking")
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    // MARK: - Helpers

    private func chatBubble(_ message: ChatMessage) -> some View {
        let isUser = message.sender == .user
        return HStack {
            if isUser { Spacer(minLength: 40) }
            VStack(alignment: .leading, spacing: 2) {
                Text(isUser ? "YOU" : "NOTEBOOKLM ADVISOR")
                    .font(.system(size: 9, weight: .bold))
                    .foregroundStyle(isUser ? Color.marketGreen : Color.gold)
                Text(message.text)
                    .font(.system(size: 13))
                    .foregroundStyle(Color.textPrimary)
                if let cit = message.citation {
                    Text("Source: \(cit.title)")
                        .font(.system(size: 9, weight: .bold))
                        .foregroundStyle(Color.textSecondary)
                }
            }
            .padding(10)
            .background(isUser ? Color.marketGreen.opacity(0.15) : Color.surfaceRaised)
            .clipShape(RoundedRectangle(cornerRadius: 14))
            if !isUser { Spacer(minLength: 40) }
        }
    }

    private func quickChip(_ label: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(label)
                .font(.system(size: 10))
                .foregroundStyle(Color.textPrimary)
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .background(Color.surfaceRaised)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color.gold.opacity(0.4), lineWidth: 1)
                )
                .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .disabled(!hasMaxAccess)
    }

    private func studyRow(_ title: String, _ description: String) -> some View {
        HStack(alignment: .top, spacing: 8) {
            RoundedRectangle(cornerRadius: 4)
                .fill(Color.marketGreen)
                .frame(width: 8, height: 8)
                .padding(.top, 4)
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.system(size: 14, weight: .bold))
                    .foregroundStyle(Color.textPrimary)
                Text(description)
                    .font(.system(size: 12))
                    .foregroundStyle(Color.textSecondary)
            }
        }
        .padding(.vertical, 4)
    }

    private func send(_ query: String) {
        let trimmed = query.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty, hasMaxAccess else { return }
        let userMsg = ChatMessage(sender: .user, text: trimmed)
        chatMessages.append(userMsg)
        let response = FinancialAdvisorEngine.evaluateConversationalQuery(
            query: trimmed,
            entries: store.entries,
            currencySymbol: settings.currencySymbol,
            dailyBudgetCents: nil
        )
        chatMessages.append(response)
    }
}
