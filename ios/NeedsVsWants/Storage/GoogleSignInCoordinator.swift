import Foundation

#if canImport(GoogleSignIn)
import GoogleSignIn
import UIKit

/// Wraps Google Sign-In SDK when the package is linked.
/// Wire via SPM: https://github.com/google/GoogleSignIn-iOS
@MainActor
public final class GoogleSignInCoordinator {
    public static let shared = GoogleSignInCoordinator()

    public private(set) var lastSession: AuthSession?

    public func signIn(presenting: UIViewController, supabase: SupabaseAuthClient) async -> Result<AuthSession, Error> {
        do {
            let result = try await GIDSignIn.sharedInstance.signIn(withPresenting: presenting)
            guard let idToken = result.user.idToken?.tokenString else {
                return .failure(NSError(domain: "GoogleSignIn", code: 422, userInfo: [NSLocalizedDescriptionKey: "No idToken"]))
            }
            let accessToken = result.user.accessToken.tokenString
            let exchange = await supabase.signInWithGoogleIdToken(
                idToken: idToken,
                accessToken: accessToken
            )
            if case .success(let session) = exchange {
                lastSession = session
                AuthSessionStore.shared.save(session)
            }
            return exchange
        } catch {
            return .failure(error)
        }
    }

    public func signOut() {
        GIDSignIn.sharedInstance.signOut()
        lastSession = nil
        AuthSessionStore.shared.clear()
    }
}
#else

/// Stub when GoogleSignIn package is not linked yet.
/// Add GoogleSignIn-iOS via SPM, then rebuild with canImport(GoogleSignIn).
@MainActor
public final class GoogleSignInCoordinator {
    public static let shared = GoogleSignInCoordinator()
    public private(set) var lastSession: AuthSession?

    public func signIn(presenting: Any? = nil, supabase: SupabaseAuthClient) async -> Result<AuthSession, Error> {
        .failure(NSError(
            domain: "GoogleSignIn",
            code: 501,
            userInfo: [NSLocalizedDescriptionKey: "GoogleSignIn SDK not linked. Add GoogleSignIn-iOS via SPM."]
        ))
    }

    public func signOut() {
        lastSession = nil
        AuthSessionStore.shared.clear()
    }
}
#endif

/// Simple UserDefaults-backed session store (upgrade to Keychain before production ship).
public final class AuthSessionStore {
    public static let shared = AuthSessionStore()
    private let defaults = UserDefaults.standard
    private let accessKey = "nvw.auth.access"
    private let refreshKey = "nvw.auth.refresh"
    private let emailKey = "nvw.auth.email"
    private let userIdKey = "nvw.auth.userId"

    public var session: AuthSession? {
        guard let access = defaults.string(forKey: accessKey), !access.isEmpty else { return nil }
        return AuthSession(
            accessToken: access,
            refreshToken: defaults.string(forKey: refreshKey),
            userId: defaults.string(forKey: userIdKey),
            email: defaults.string(forKey: emailKey),
            expiresAt: nil
        )
    }

    public func save(_ session: AuthSession) {
        defaults.set(session.accessToken, forKey: accessKey)
        defaults.set(session.refreshToken, forKey: refreshKey)
        defaults.set(session.email, forKey: emailKey)
        defaults.set(session.userId, forKey: userIdKey)
    }

    public func clear() {
        [accessKey, refreshKey, emailKey, userIdKey].forEach { defaults.removeObject(forKey: $0) }
    }
}
