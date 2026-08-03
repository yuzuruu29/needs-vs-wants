import SwiftUI

public struct PaywallView: View {
    @Environment(\.dismiss) private var dismiss
    @StateObject private var storeKit = StoreKitManager.shared
    @State private var statusMessage: String = "Tap start to begin the 3-day free trial."
    
    public init() {}
    
    public var body: some View {
        ZStack {
            Color(red: 0.98, green: 0.98, blue: 0.97)
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                HStack {
                    Text("PRO  ·  PREMIUM")
                        .font(.caption)
                        .fontWeight(.bold)
                        .foregroundColor(Color(red: 0.91, green: 0.66, blue: 0.16))
                    Spacer()
                    Button(action: { dismiss() }) {
                        Image(systemName: "xmark")
                            .foregroundColor(.secondary)
                    }
                }
                .padding(.top, 16)
                
                Image(systemName: "crown.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 44, height: 44)
                    .foregroundColor(Color(red: 0.78, green: 0.06, blue: 0.18))
                    .padding(16)
                    .background(Color(red: 0.78, green: 0.06, blue: 0.18).opacity(0.12))
                    .clipShape(Circle())
                
                Text("Needs vs. Wants Pro")
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("Remove free-plan limits and keep every habit at your fingertips.")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                
                VStack(alignment: .leading, spacing: 12) {
                    BenefitRow(title: "Unlimited diary", detail: "No more 20-entry cap on your log.")
                    BenefitRow(title: "Keep all history", detail: "Free tier only retains the last 35 days.")
                    BenefitRow(title: "Full period analytics", detail: "Summary across all time, not just this week.")
                }
                .padding(.vertical, 8)
                
                VStack(spacing: 8) {
                    Text("3-DAY FREE TRIAL")
                        .font(.caption2)
                        .fontWeight(.bold)
                        .foregroundColor(Color(red: 0.91, green: 0.66, blue: 0.16))
                    
                    Text("≈ US$4.99 / month after trial")
                        .font(.headline)
                }
                .padding()
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .cornerRadius(14)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .stroke(Color(red: 0.91, green: 0.66, blue: 0.16).opacity(0.5), lineWidth: 1)
                )
                
                Spacer()
                
                Button(action: {
                    Task {
                        let res = await storeKit.startTrial()
                        handleResult(res)
                    }
                }) {
                    Text(storeKit.isPro ? "You're Pro" : "Start Pro Trial (₱249/mo)")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(Color(red: 0.78, green: 0.06, blue: 0.18))
                        .cornerRadius(14)
                }
                .disabled(storeKit.isPro || storeKit.isBusy)
                
                Button(action: {
                    Task {
                        let res = await storeKit.purchaseMaxMonthly()
                        handleResult(res)
                    }
                }) {
                    Text(storeKit.isMax ? "You're Max" : "Upgrade to Max (₱499/mo · AI Advisor)")
                        .font(.subheadline)
                        .fontWeight(.bold)
                        .foregroundColor(Color(red: 0.78, green: 0.06, blue: 0.18))
                        .frame(maxWidth: .infinity)
                        .frame(height: 48)
                        .background(Color.white)
                        .cornerRadius(14)
                        .overlay(
                            RoundedRectangle(cornerRadius: 14)
                                .stroke(Color(red: 0.91, green: 0.66, blue: 0.16), lineWidth: 1.5)
                        )
                }
                .disabled(storeKit.isMax || storeKit.isBusy)
                
                Text(statusMessage)
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            .padding(.horizontal, 24)
        }
    }
    
    private func handleResult(_ res: StoreKitResult) {
        switch res {
        case .success: statusMessage = "Welcome to Pro!"
        case .pending: statusMessage = "Payment processing..."
        case .failed: statusMessage = "Payment didn't go through."
        case .unavailable: statusMessage = "StoreKit is unavailable on this environment."
        }
    }
}

private struct BenefitRow: View {
    let title: String
    let detail: String
    
    var body: some View {
        HStack(spacing: 12) {
            Circle()
                .fill(Color(red: 0.91, green: 0.66, blue: 0.16))
                .frame(width: 8, height: 8)
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(.subheadline).fontWeight(.semibold)
                Text(detail).font(.caption).foregroundColor(.secondary)
            }
        }
    }
}
