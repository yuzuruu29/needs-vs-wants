import SwiftUI

/// Home screen: period rotor, hand-built donut, stat cards, CTA.
struct SummaryView: View {
    @Environment(EntryStore.self) private var store
    @Environment(AppSettings.self) private var settings
    @Environment(AppSession.self) private var session

    @Binding var showInstructions: Bool

    private var stats: SummaryStats { store.stats(for: session.selectedPeriod) }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                header
                Spacer().frame(height: 28)
                periodRotor
                Spacer().frame(height: 28)
                donut
                Spacer().frame(height: 28)
                statCards
                Spacer().frame(height: 16)
                PrimaryButton(title: "Log an expense") {
                    // Switch to the Log tab.
                    NotificationCenter.default.post(name: .switchToLog, object: nil)
                }
            }
            .padding(.horizontal, AppSpacing.xl)
            .padding(.top, AppSpacing.xl)
            .padding(.bottom, 12)
        }
        .background(Color.surface)
    }

    private var header: some View {
        HStack(alignment: .top) {
            VStack(alignment: .leading, spacing: 6) {
                Eyebrow("A 35-Day Trainer", color: .crimson)
                Text("NEEDS\nvs WANTS")
                    .font(AppFont.hero)
                    .foregroundStyle(Color.textPrimary)
                    .lineSpacing(2)
                GiltRule(width: 40)
                Text("Expense Tracker")
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundStyle(Color.crimson)
                Text(periodLabel)
                    .font(.system(size: 11, weight: .semibold))
                    .foregroundStyle(Color.crimson)
                Text(periodRange)
                    .font(.system(size: 11))
                    .tracking(0.6)
                    .foregroundStyle(Color.textMuted)
            }
            Spacer()
            Button {
                settings.completeFirstLaunch()
                showInstructions = true
            } label: {
                Image(systemName: "questionmark.circle")
                    .font(.system(size: 20))
                    .foregroundStyle(Color.crimson)
                    .frame(width: 36, height: 36)
            }
        }
    }

    private var periodRotor: some View {
        HStack(spacing: 4) {
            ForEach(Period.allCases, id: \.self) { p in
                let sel = session.selectedPeriod == p
                Button {
                    withAnimation(.easeOut(duration: 0.2)) { session.selectedPeriod = p }
                } label: {
                    Text(p.label)
                        .font(.system(size: 12, weight: sel ? .semibold : .regular))
                        .foregroundStyle(sel ? Color.surfaceCard : Color.textSecondary)
                        .frame(maxWidth: .infinity, minHeight: 36)
                        .background(sel ? Color.crimson : Color.clear)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
            }
        }
        .padding(4)
        .background(Color.surfaceCard)
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.divider, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    @ViewBuilder
    private var donut: some View {
        if stats.totalCents == 0 {
            VStack(spacing: 12) {
                ZStack {
                    GlowRing()
                    Circle()
                        .stroke(Color.divider, lineWidth: 16)
                        .frame(width: 150, height: 150)
                }
                Eyebrow("EMPTY DIARY", color: .textMuted)
                Text("Log your first expense\nto start the diary.")
                    .font(.system(size: 14))
                    .foregroundStyle(Color.textSecondary)
                    .multilineTextAlignment(.center)
            }
            .frame(maxWidth: .infinity)
        } else {
            VStack(spacing: 14) {
                ZStack {
                    GlowRing()
                    DonutChart(needs: stats.needsTotalCents, wants: stats.wantsTotalCents)
                        .frame(width: 180, height: 180)
                    VStack(spacing: 2) {
                        Eyebrow("TOTAL", color: .textMuted, size: 10)
                        Text(CurrencyFormatter.format(stats.totalCents, symbol: settings.currencySymbol))
                            .font(.system(size: CurrencyFormatter.adaptiveSize(
                                CurrencyFormatter.format(stats.totalCents, symbol: settings.currencySymbol), base: 22),
                                weight: .bold).monospacedDigit())
                            .foregroundStyle(Color.textPrimary)
                            .lineLimit(1)
                    }
                }
                HStack(spacing: 20) {
                    LegendChip(color: .need, label: "Need", pct: stats.needsPct)
                    Rectangle().fill(Color.divider).frame(width: 1, height: 14)
                    LegendChip(color: .want, label: "Want", pct: stats.wantsPct)
                }
            }
            .frame(maxWidth: .infinity)
        }
    }

    private var statCards: some View {
        HStack(spacing: 12) {
            StatCard(label: "NEEDS", value: CurrencyFormatter.format(stats.needsTotalCents, symbol: settings.currencySymbol),
                     accent: .need, pct: stats.needsPct)
            StatCard(label: "WANTS", value: CurrencyFormatter.format(stats.wantsTotalCents, symbol: settings.currencySymbol),
                     accent: .want, pct: stats.wantsPct)
            StatCard(label: "NEED %", value: "\(stats.needsPct)%", accent: .gold, pct: stats.needsPct)
        }
    }

    // MARK: - Range helpers

    private var periodLabel: String {
        switch session.selectedPeriod {
        case .day:  return "TODAY"
        case .week: return "THIS WEEK"
        case .all:  return "ALL 35 DAYS"
        }
    }

    private var periodRange: String {
        let fmt = DateFormatter(); fmt.dateFormat = "MMM d"
        let today = Date()
        switch session.selectedPeriod {
        case .day:
            return fmt.string(from: today)
        case .week:
            let start = Calendar.current.date(byAdding: .day, value: -6, to: today)!
            return "\(fmt.string(from: start)) — \(fmt.string(from: today))"
        case .all:
            let start = Calendar.current.date(byAdding: .day, value: -34, to: today)!
            return "\(fmt.string(from: start)) — \(fmt.string(from: today))"
        }
    }
}

// MARK: - Charts (hand-built, no third-party)

struct DonutChart: View {
    let needs: Int64
    let wants: Int64

    var body: some View {
        let total = CGFloat(needs + wants)
        let needsSweep = total > 0 ? CGFloat(needs) / total * 360 : 0
        Canvas { ctx, size in
            let ring: CGFloat = 22
            let rect = CGRect(x: ring/2, y: ring/2, width: size.width - ring, height: size.height - ring)
            let c = CGPoint(x: size.width/2, y: size.height/2)
            let radius = min(rect.width, rect.height)/2
            func arc(_ color: Color, _ start: CGFloat, _ sweep: CGFloat) {
                var p = Path()
                p.addArc(center: c, radius: radius, startAngle: .degrees(start), endAngle: .degrees(start + sweep), clockwise: false)
                ctx.stroke(p, with: .color(color), lineWidth: ring, lineCap: .round)
            }
            arc(.need, -90, needsSweep)
            arc(.want, -90 + needsSweep, 360 - needsSweep)
        }
    }
}

struct GlowRing: View {
    var body: some View {
        Circle()
            .fill(RadialGradient(colors: [Color.gold.opacity(0.18), .clear], center: .center, startRadius: 0, endRadius: 120))
            .frame(width: 200, height: 200)
    }
}

struct LegendChip: View {
    let color: Color
    let label: String
    let pct: Int
    var body: some View {
        HStack(spacing: 6) {
            Circle().fill(color).frame(width: 8, height: 8)
            Text("\(label) \(pct)%")
                .font(.system(size: 12))
                .tracking(0.5)
                .foregroundStyle(Color.textSecondary)
        }
    }
}

struct StatCard: View {
    let label: String
    let value: String
    let accent: Color
    let pct: Int
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Eyebrow(label, color: .textMuted, size: 9)
            Text(value)
                .font(.system(size: CurrencyFormatter.adaptiveSize(value, base: 15), weight: .bold).monospacedDigit())
                .foregroundStyle(accent)
                .lineLimit(1)
                .minimumScaleFactor(0.6)
            GeometryReader { geo in
                Rectangle().fill(Color.divider).frame(height: 3)
                    .overlay(alignment: .leading) {
                        Rectangle().fill(accent)
                            .frame(width: geo.size.width * CGFloat(pct) / 100, height: 3)
                    }
            }
            .frame(height: 3)
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 14)
        .background(Color.surfaceCard)
        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.divider, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 16))
    }
}

extension Notification.Name {
    static let switchToLog = Notification.Name("switchToLog")
}
