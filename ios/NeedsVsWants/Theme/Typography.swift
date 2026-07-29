import SwiftUI

/// Display + body type. Android uses Playfair Display SC (serif, bold) for
/// titles and Inter for body. On iOS we use the system serif fallback; drop in
/// a bundled Playfair Display SC + Inter later if licensing allows (swap the
/// `Font.custom(...)` calls here — nothing else needs to change).
enum AppFont {
    static let hero     = Font.system(size: 30, weight: .bold, design: .serif)
    static let title    = Font.system(size: 26, weight: .bold, design: .serif)
    static let headline = Font.system(size: 20, weight: .bold, design: .serif)
    static let body     = Font.system(size: 15)
    static let bodyMedium = Font.system(size: 14)
    static let label    = Font.system(size: 12, weight: .semibold)
}
