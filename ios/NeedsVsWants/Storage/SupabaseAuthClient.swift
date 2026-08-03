import Foundation

public final class SupabaseAuthClient {
    public static let shared = SupabaseAuthClient()
    
    private var supabaseUrl: String = ""
    private var anonKey: String = ""
    
    public var isConfigured: Bool {
        !supabaseUrl.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty &&
        !anonKey.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }
    
    public init(url: String = "", anonKey: String = "") {
        self.supabaseUrl = url
        self.anonKey = anonKey
    }
    
    public func sendMagicLink(email: String) async -> Result<Void, Error> {
        guard isConfigured else {
            return .failure(NSError(domain: "SupabaseAuth", code: 401, userInfo: [NSLocalizedDescriptionKey: "Supabase auth not configured"]))
        }
        
        guard let endpoint = URL(string: "\(supabaseUrl)/auth/v1/otp") else {
            return .failure(NSError(domain: "SupabaseAuth", code: 400, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"]))
        }
        
        var request = URLRequest(url: endpoint)
        request.httpMethod = "POST"
        request.setValue(anonKey, forHTTPHeaderField: "apikey")
        request.setValue("Bearer \(anonKey)", forHTTPHeaderField: "Authorization")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body: [String: Any] = ["email": email, "create_user": true]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        do {
            let (_, response) = try await URLSession.shared.data(for: request)
            if let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) {
                return .success(())
            } else {
                return .failure(NSError(domain: "SupabaseAuth", code: 500, userInfo: [NSLocalizedDescriptionKey: "Request failed"]))
            }
        } catch {
            return .failure(error)
        }
    }

    /// Exchanges a Google ID token for a Supabase session
    /// (`POST /auth/v1/token?grant_type=id_token`).
    /// - Parameters:
    ///   - idToken: Google OpenID ID token from GIDSignIn
    ///   - nonce: raw (unhashed) nonce if one was used with Google Sign-In
    ///   - accessToken: optional Google access token
    public func signInWithGoogleIdToken(
        idToken: String,
        nonce: String? = nil,
        accessToken: String? = nil
    ) async -> Result<AuthSession, Error> {
        guard isConfigured else {
            return .failure(NSError(domain: "SupabaseAuth", code: 401, userInfo: [NSLocalizedDescriptionKey: "Supabase auth not configured"]))
        }
        guard let endpoint = URL(string: "\(supabaseUrl)/auth/v1/token?grant_type=id_token") else {
            return .failure(NSError(domain: "SupabaseAuth", code: 400, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"]))
        }

        var request = URLRequest(url: endpoint)
        request.httpMethod = "POST"
        request.setValue(anonKey, forHTTPHeaderField: "apikey")
        request.setValue("Bearer \(anonKey)", forHTTPHeaderField: "Authorization")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        var body: [String: Any] = [
            "provider": "google",
            "id_token": idToken
        ]
        if let nonce, !nonce.isEmpty { body["nonce"] = nonce }
        if let accessToken, !accessToken.isEmpty { body["access_token"] = accessToken }
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            guard let httpResponse = response as? HTTPURLResponse else {
                return .failure(NSError(domain: "SupabaseAuth", code: 500, userInfo: [NSLocalizedDescriptionKey: "Invalid response"]))
            }
            guard (200...299).contains(httpResponse.statusCode) else {
                return .failure(NSError(domain: "SupabaseAuth", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: "Google sign-in exchange failed"]))
            }
            guard let session = Self.parseAuthSession(from: data) else {
                return .failure(NSError(domain: "SupabaseAuth", code: 422, userInfo: [NSLocalizedDescriptionKey: "Missing access_token in id_token response"]))
            }
            return .success(session)
        } catch {
            return .failure(error)
        }
    }

    /// Verifies an emailed OTP and returns the Supabase `access_token`.
    /// Fails closed when the response JSON has no token (never returns raw body).
    public func verifyOtp(email: String, token: String) async -> Result<String, Error> {
        guard isConfigured else {
            return .failure(NSError(domain: "SupabaseAuth", code: 401, userInfo: [NSLocalizedDescriptionKey: "Supabase auth not configured"]))
        }

        guard let endpoint = URL(string: "\(supabaseUrl)/auth/v1/verify") else {
            return .failure(NSError(domain: "SupabaseAuth", code: 400, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"]))
        }

        var request = URLRequest(url: endpoint)
        request.httpMethod = "POST"
        request.setValue(anonKey, forHTTPHeaderField: "apikey")
        request.setValue("Bearer \(anonKey)", forHTTPHeaderField: "Authorization")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "type": "email",
            "email": email,
            "token": token
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            guard let httpResponse = response as? HTTPURLResponse else {
                return .failure(NSError(domain: "SupabaseAuth", code: 500, userInfo: [NSLocalizedDescriptionKey: "Invalid response"]))
            }
            guard (200...299).contains(httpResponse.statusCode) else {
                return .failure(NSError(domain: "SupabaseAuth", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: "Verify failed"]))
            }
            guard let accessToken = Self.parseAccessToken(from: data) else {
                return .failure(NSError(domain: "SupabaseAuth", code: 422, userInfo: [NSLocalizedDescriptionKey: "Missing access_token in verify response"]))
            }
            return .success(accessToken)
        } catch {
            return .failure(error)
        }
    }

    /// Pure JSON helper — unit-testable without network.
    public static func parseAccessToken(from data: Data) -> String? {
        guard
            let obj = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
            let token = obj["access_token"] as? String,
            !token.isEmpty
        else {
            return nil
        }
        return token
    }

    /// Parses a full Supabase token response into [AuthSession].
    public static func parseAuthSession(from data: Data, now: Date = Date()) -> AuthSession? {
        guard
            let obj = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
            let access = obj["access_token"] as? String,
            !access.isEmpty
        else {
            return nil
        }
        let refresh = obj["refresh_token"] as? String
        let expiresIn = obj["expires_in"] as? Int
        let user = obj["user"] as? [String: Any]
        let userId = user?["id"] as? String
        let email = user?["email"] as? String
        let expiresAt: Date? = expiresIn.map { now.addingTimeInterval(TimeInterval($0)) }
        return AuthSession(
            accessToken: access,
            refreshToken: refresh,
            userId: userId,
            email: email,
            expiresAt: expiresAt
        )
    }
}

/// Lightweight session model for Supabase Auth (Google / OTP).
public struct AuthSession: Equatable {
    public let accessToken: String
    public let refreshToken: String?
    public let userId: String?
    public let email: String?
    public let expiresAt: Date?

    public init(
        accessToken: String,
        refreshToken: String?,
        userId: String?,
        email: String?,
        expiresAt: Date?
    ) {
        self.accessToken = accessToken
        self.refreshToken = refreshToken
        self.userId = userId
        self.email = email
        self.expiresAt = expiresAt
    }
}
