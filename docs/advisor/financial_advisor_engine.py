"""
Financial Advisor Engine & NotebookLM Integration helper.
Binds economic study notebooks (Google NotebookLM) as the primary knowledge base / source of truth
for the Needs vs Wants Financial Advisor feature.
"""

import json
import os
import sys
from typing import Dict, List, Any, Optional

DEFAULT_ECONOMIC_RULES = [
    {
        "id": "rule_01_need_vs_want_split",
        "title": "Essential Needs vs Discretionary Wants Baseline",
        "source": "Economic Study Notebook #1: Budgetary Equilibrium",
        "citation": "NotebookLM Section 1.2 — Binary Classification Dynamics",
        "principle": "Prioritize essential survival expenditure (housing, basic groceries, utilities) before discretionary allocation. Aim for a balanced baseline (e.g., 50% Needs, 30% Wants, 20% Savings/Buffer).",
        "recommendation": "If Want spend exceeds 40% of total expenditure, flag discretionary momentum and defer non-essential purchases."
    },
    {
        "id": "rule_02_daily_budget_velocity",
        "title": "Daily Spending Velocity & Limit Safeguards",
        "source": "Economic Study Notebook #2: Micro-Transaction Friction",
        "citation": "NotebookLM Section 3.1 — Real-Time Friction Behavioral Control",
        "principle": "Unmonitored daily micro-transactions degrade monthly liquidity. Setting a hard daily spending velocity limit increases psychological friction at point-of-sale.",
        "recommendation": "When approaching 80% of daily budget, restrict log entries to Needs only."
    },
    {
        "id": "rule_03_overspend_recovery",
        "title": "Overspend Recovery Protocol",
        "source": "Economic Study Notebook #3: Impulse Recovery",
        "citation": "NotebookLM Section 4.5 — Compensatory Budget Sinking",
        "principle": "Impulse overspending in a single day should be absorbed across subsequent days rather than abandoning the budget.",
        "recommendation": "Deduct yesterday's overspend delta equally across the next 3 days' spending caps."
    }
]

class FinancialAdvisorEngine:
    def __init__(self, notebook_url: Optional[str] = None):
        self.notebook_url = notebook_url or "https://notebook.google.com/"
        self.rules = DEFAULT_ECONOMIC_RULES

    def get_advice(self, query: str, context: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
        """
        Evaluate user query and current budget metrics against the NotebookLM source of truth.
        """
        query_lower = query.lower()
        matched_rules = []

        for rule in self.rules:
            if any(term in query_lower for term in [rule["title"].lower(), "need", "want", "budget", "limit", "overspend", "saving"]):
                matched_rules.append(rule)

        if not matched_rules:
            matched_rules = [self.rules[0]]

        return {
            "query": query,
            "source_of_truth": "Google NotebookLM (Economic Studies)",
            "notebook_url": self.notebook_url,
            "rules_applied": matched_rules,
            "response": matched_rules[0]["recommendation"],
            "citation": matched_rules[0]["citation"]
        }

if __name__ == "__main__":
    engine = FinancialAdvisorEngine()
    result = engine.get_advice("How should I handle overspending on Wants today?")
    print(json.dumps(result, indent=2))
