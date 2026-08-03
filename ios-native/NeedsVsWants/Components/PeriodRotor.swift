import SwiftUI

/// Crimson → crimsonDeep gradient segment control — Android Summary period bar.
struct PeriodRotor: View {
    @Binding var period: Period

    var body: some View {
        HStack(spacing: 4) {
            ForEach(Period.allCases, id: \.self) { p in
                let selected = p == period
                Button {
                    period = p
                } label: {
                    Text(p.label)
                        .font(.system(size: 12, weight: selected ? .semibold : .regular))
                        .foregroundStyle(selected ? Color.white : AppColors.textSecondary)
                        .frame(maxWidth: .infinity)
                        .frame(height: 36)
                        .background {
                            if selected {
                                LinearGradient(
                                    colors: [AppColors.crimson, AppColors.crimsonDeep],
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            }
                        }
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                }
                .buttonStyle(.plain)
                .accessibilityLabel(p.label)
                .accessibilityAddTraits(selected ? .isSelected : [])
            }
        }
        .padding(4)
        .background(AppColors.surfaceCard)
        .clipShape(RoundedRectangle(cornerRadius: AppMetrics.inputRadius))
        .overlay(
            RoundedRectangle(cornerRadius: AppMetrics.inputRadius)
                .stroke(AppColors.divider, lineWidth: 1)
        )
    }
}
