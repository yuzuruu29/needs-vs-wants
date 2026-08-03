import Foundation
import StoreKit

public enum StoreKitResult {
    case success
    case pending
    case failed
    case unavailable
}

@MainActor
public final class StoreKitManager: ObservableObject {
    public static let shared = StoreKitManager()
    
    @Published public private(set) var isPro: Bool = false
    @Published public private(set) var isMax: Bool = false
    @Published public private(set) var isBusy: Bool = false
    
    private let trialProductId = "com.needsvswants.pro.trial3day"
    private let monthlyProductId = "com.needsvswants.pro.monthly"
    private let maxMonthlyProductId = "com.needsvswants.max.monthly"
    
    public init() {}
    
    public func startTrial() async -> StoreKitResult {
        return await purchase(productId: trialProductId)
    }
    
    public func purchaseMonthly() async -> StoreKitResult {
        return await purchase(productId: monthlyProductId)
    }
    
    public func purchaseMaxMonthly() async -> StoreKitResult {
        let res = await purchase(productId: maxMonthlyProductId)
        if res == .success {
            self.isMax = true
        }
        return res
    }
    
    public func purchase(productId: String) async -> StoreKitResult {
        isBusy = true
        defer { isBusy = false }
        
        guard SKPaymentQueue.canMakePayments() else {
            return .unavailable
        }
        
        do {
            let products = try await Product.products(for: [productId])
            guard let product = products.first else {
                return .unavailable
            }
            
            let result = try await product.purchase()
            switch result {
            case .success(let verification):
                switch verification {
                case .verified(let transaction):
                    await transaction.finish()
                    self.isPro = true
                    return .success
                case .unverified:
                    return .failed
                }
            case .userCancelled:
                return .failed
            case .pending:
                return .pending
            @unknown default:
                return .failed
            }
        } catch {
            return .unavailable
        }
    }
    
    public func restorePurchases() async -> StoreKitResult {
        isBusy = true
        defer { isBusy = false }
        
        for await result in Transaction.currentEntitlements {
            if case .verified(let transaction) = result {
                if transaction.revocationDate == nil {
                    self.isPro = true
                    return .success
                }
            }
        }
        return .unavailable
    }
}
