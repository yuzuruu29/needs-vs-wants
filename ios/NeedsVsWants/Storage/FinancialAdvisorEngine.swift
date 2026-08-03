import Foundation

/// Pure offline advisor engine — mirrors Android `FinancialAdvisorEngine`.
/// Grounded in Google NotebookLM economic study notebooks (Max tier).

struct AdvisorCitation: Equatable {
    let title: String
    let section: String
    let notebookUrl: String

    init(
        title: String,
        section: String,
        notebookUrl: String = "https://notebook.google.com/"
    ) {
        self.title = title
        self.section = section
        self.notebookUrl = notebookUrl
    }
}

struct AdvisorInsight: Equatable {
    let headline: String
    let advice: String
    let citation: AdvisorCitation
    let isWarning: Bool
}

enum ChatSender: Equatable {
    case user
    case advisor
}

struct ChatMessage: Identifiable, Equatable {
    let id: String
    let sender: ChatSender
    let text: String
    let citation: AdvisorCitation?
    let isWarning: Bool
    let timestampMs: Int64

    init(
        id: String = UUID().uuidString,
        sender: ChatSender,
        text: String,
        citation: AdvisorCitation? = nil,
        isWarning: Bool = false,
        timestampMs: Int64 = Int64(Date().timeIntervalSince1970 * 1000)
    ) {
        self.id = id
        self.sender = sender
        self.text = text
        self.citation = citation
        self.isWarning = isWarning
        self.timestampMs = timestampMs
    }
}

enum FinancialAdvisorEngine {
    static let sourceOfTruthTitle = "Google NotebookLM — Economic Studies"
    static let defaultNotebookUrl = "https://notebook.google.com/"

    static func generateInsight(
        entries: [Entry],
        currencySymbol: String,
        dailyBudgetCents: Int64? = nil
    ) -> AdvisorInsight {
        let totalNeed = entries.filter { $0.type == .need }.reduce(Int64(0)) { $0 + $1.costCents }
        let totalWant = entries.filter { $0.type == .want }.reduce(Int64(0)) { $0 + $1.costCents }
        let totalSpent = totalNeed + totalWant

        if let budget = dailyBudgetCents, budget > 0, totalSpent > budget {
            let over = CurrencyFormatter.format(totalSpent - budget, symbol: currencySymbol)
            return AdvisorInsight(
                headline: "Compensatory Budget Recovery Active",
                advice: "According to your Economic Study Notebook #3 (Impulse Recovery), you have exceeded today's budget limit by \(over). Reduce discretionary Want spending by 33% over the next 3 days to restore liquidity equilibrium.",
                citation: AdvisorCitation(
                    title: "Notebook #3: Impulse Recovery",
                    section: "NotebookLM Section 4.5 — Compensatory Sinking Protocol"
                ),
                isWarning: true
            )
        }

        if totalWant > totalNeed && totalSpent > 0 {
            return AdvisorInsight(
                headline: "Discretionary Spending Exceeds Baseline Needs",
                advice: "Your Economic Study Notebook #1 (Budgetary Equilibrium) principles indicate that discretionary Wants currently surpass essential Needs. Introduce a 24-hour delay before logging additional Want items.",
                citation: AdvisorCitation(
                    title: "Notebook #1: Budgetary Equilibrium",
                    section: "NotebookLM Section 1.2 — Binary Classification Dynamics"
                ),
                isWarning: true
            )
        }

        return AdvisorInsight(
            headline: "Spending Within Economic Study Targets",
            advice: "Based on your Google NotebookLM economic study notebooks, your spending velocity is balanced. Essential Needs form the anchor of your daily ledger.",
            citation: AdvisorCitation(
                title: "Notebook #2: Behavioral Friction",
                section: "NotebookLM Section 3.1 — Real-Time Behavioral Control"
            ),
            isWarning: false
        )
    }

    static func evaluateConversationalQuery(
        query: String,
        entries: [Entry],
        currencySymbol: String,
        dailyBudgetCents: Int64? = nil
    ) -> ChatMessage {
        let lower = query.lowercased()
        let totalNeed = entries.filter { $0.type == .need }.reduce(Int64(0)) { $0 + $1.costCents }
        let totalWant = entries.filter { $0.type == .want }.reduce(Int64(0)) { $0 + $1.costCents }
        let totalSpent = totalNeed + totalWant

        let text: String
        let citation: AdvisorCitation
        let isWarning: Bool

        if lower.contains("over") || lower.contains("overspend") || lower.contains("exceed")
            || lower.contains("limit") || lower.contains("budget") {
            if let budget = dailyBudgetCents, budget > 0, totalSpent > budget {
                let over = CurrencyFormatter.format(totalSpent - budget, symbol: currencySymbol)
                text = "You are currently over budget by \(over) today. Per Notebook #3 (Impulse Recovery), absorb this overspend by lowering your Want spending allowance over the next 3 days."
                citation = AdvisorCitation(
                    title: "Notebook #3: Impulse Recovery",
                    section: "NotebookLM Section 4.5 — Compensatory Sinking Protocol"
                )
                isWarning = true
            } else {
                text = "Your daily spending is within your budget. Keep maintaining real-time log friction for non-essential Want items."
                citation = AdvisorCitation(
                    title: "Notebook #2: Behavioral Friction",
                    section: "NotebookLM Section 3.1 — Micro-Transaction Friction"
                )
                isWarning = false
            }
        } else if lower.contains("buy") || lower.contains("afford") || lower.contains("want") {
            if totalWant >= totalNeed && totalSpent > 0 {
                let wants = CurrencyFormatter.format(totalWant, symbol: currencySymbol)
                let needs = CurrencyFormatter.format(totalNeed, symbol: currencySymbol)
                text = "Discretionary Want spend currently equals or exceeds essential Needs (\(wants) Wants vs \(needs) Needs). Notebook #1 advises waiting 24 hours before buying another Want."
                citation = AdvisorCitation(
                    title: "Notebook #1: Budgetary Equilibrium",
                    section: "NotebookLM Section 1.2 — Binary Classification"
                )
                isWarning = true
            } else {
                let needs = CurrencyFormatter.format(totalNeed, symbol: currencySymbol)
                text = "Your Needs currently form the majority of your ledger (\(needs) Needs). You have capacity for a deliberate Want if it stays within your budget."
                citation = AdvisorCitation(
                    title: "Notebook #1: Budgetary Equilibrium",
                    section: "NotebookLM Section 1.2 — Binary Classification"
                )
                isWarning = false
            }
        } else {
            text = "Grounded in your Google NotebookLM economic studies: Always classify entries at point-of-sale to preserve behavioral friction. Needs should remain your core financial anchor."
            citation = AdvisorCitation(
                title: "Notebook #2: Behavioral Control",
                section: "NotebookLM Section 3.1 — Real-Time Control"
            )
            isWarning = false
        }

        return ChatMessage(
            id: "advisor_\(Int64(Date().timeIntervalSince1970 * 1000))",
            sender: .advisor,
            text: text,
            citation: citation,
            isWarning: isWarning
        )
    }
}
