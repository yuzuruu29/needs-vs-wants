import SwiftUI

/// Short gold rule under section titles.
struct GiltRule: View {
    var width: CGFloat = 32
    var height: CGFloat = 1.5

    var body: some View {
        Rectangle()
            .fill(Color.gold)
            .frame(width: width, height: height)
    }
}
