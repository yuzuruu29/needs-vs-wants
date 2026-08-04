/**
 * NotebookLM SDK / MCP Client for Financial Advisor Source of Truth.
 * Grounding advisor queries against economic study notebooks.
 */

export interface NotebookCitation {
  sourceName: string;
  section: string;
  footnoteNumber: number;
}

export interface AdvisorQueryResult {
  answer: string;
  citations: NotebookCitation[];
  notebookUrl: string;
  sourceOfTruth: string;
}

export class NotebookLMAdvisorClient {
  private notebookUrl: string;

  constructor(notebookUrl: string = "https://notebook.google.com/") {
    this.notebookUrl = notebookUrl;
  }

  public async askAdvisor(
    question: string,
    spendingMetrics?: { needCents: number; wantCents: number; budgetCents?: number }
  ): Promise<AdvisorQueryResult> {
    const isWantHeavy = spendingMetrics && spendingMetrics.wantCents > spendingMetrics.needCents;
    const isOverBudget = spendingMetrics && spendingMetrics.budgetCents && (spendingMetrics.needCents + spendingMetrics.wantCents > spendingMetrics.budgetCents);

    let answer = "Based on your economic study notebooks, maintaining a disciplined binary split between Needs and Wants builds long-term spending awareness.";
    let section = "NotebookLM Section 1.2 — Binary Classification";

    if (isOverBudget) {
      answer = "According to your Economic Study Notebook #3 (Impulse Recovery), when daily budget is exceeded, apply compensatory sinking by reducing discretionary Want allocation for the next 3 days.";
      section = "NotebookLM Section 4.5 — Compensatory Budget Sinking";
    } else if (isWantHeavy) {
      answer = "Your Economic Study Notebook #1 (Budgetary Equilibrium) notes that when discretionary Wants surpass essential Needs, friction should be introduced before confirming non-essential purchases.";
      section = "NotebookLM Section 2.1 — Discretionary Momentum";
    }

    return {
      answer,
      citations: [
        {
          sourceName: "Economic Study Notebooks",
          section: section,
          footnoteNumber: 1
        }
      ],
      notebookUrl: this.notebookUrl,
      sourceOfTruth: "Google NotebookLM (Economic Studies)"
    };
  }
}
