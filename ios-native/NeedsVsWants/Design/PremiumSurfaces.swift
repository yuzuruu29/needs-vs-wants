import SwiftUI

/// Premium surface treatments ported from Android `Components.kt`.
/// These are the brush strokes the native rewrite dropped — ink wash, gilt glow,
/// and gold crown accents that make the supermarket brand feel dimensional.
extension View {
    /// Soft vertical wash: `surface` → `surfaceRaised`. Use as screen background.
    func inkWashBackground() -> some View {
        background(
            LinearGradient(
                colors: [AppColors.surface, AppColors.surfaceRaised],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()
        )
    }

    /// Faint gold radial glow behind a hero (donut, empty diary ring).
    func giltGlow(alpha: Double = 0.16, diameter: CGFloat = 220) -> some View {
        background {
            RadialGradient(
                colors: [AppColors.gold.opacity(alpha), AppColors.gold.opacity(0)],
                center: .center,
                startRadius: 0,
                endRadius: diameter / 2
            )
            .frame(width: diameter, height: diameter)
            .allowsHitTesting(false)
        }
    }
}

/// Accent bar under a money value — single-color fill proportional to share.
/// Android `StatCard` canvas bar: track + accent segment.
struct AccentShareBar: View {
    let fraction: Double
    let accent: Color

    var body: some View {
        GeometryReader { geo in
            let clamped = max(0, min(1, fraction))
            ZStack(alignment: .leading) {
                Rectangle()
                    .fill(AppColors.divider)
                Rectangle()
                    .fill(accent)
                    .frame(width: geo.size.width * clamped)
            }
        }
        .frame(height: 3)
        .clipShape(RoundedRectangle(cornerRadius: 1.5))
        .accessibilityHidden(true)
    }
}
