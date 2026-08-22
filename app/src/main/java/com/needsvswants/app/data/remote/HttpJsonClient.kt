package com.needsvswants.app.data.remote

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Minimal, zero-dependency JSON-over-HTTP helper used by the Supabase seams.
 *
 * Uses only the JDK `java.net` stack (no third-party HTTP client) so it stays
 * resolvable in the offline build. Never invoked unless [SupabaseConfig.enabled]
 * is true — network calls are guarded by the callers.
 */
internal object HttpJsonClient {

    /** Returns [Result.success] with the response body on 2xx, [Result.failure] otherwise. */
    suspend fun request(
        url: String,
        method: String = "GET",
        headers: Map<String, String> = emptyMap(),
        body: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        var conn: HttpURLConnection? = null
        try {
            conn = URL(url).openConnection() as HttpURLConnection
            val connection = conn
            connection.requestMethod = method
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            headers.forEach { (k, v) -> connection.setRequestProperty(k, v) }
            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Content-Length", body.toByteArray().size.toString())
                connection.outputStream.use { it.write(body.toByteArray()) }
            }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val text = stream?.bufferedReader()?.use(BufferedReader::readText) ?: ""
            if (code in 200..299) Result.success(text) else Result.failure(RuntimeException("HTTP $code: $text"))
        } catch (e: CancellationException) {
            // Cancellation must propagate — never convert it into a failure result.
            throw e
        } catch (t: Throwable) {
            Result.failure(t)
        } finally {
            // Release the socket on every path (success, error body, exception).
            conn?.disconnect()
        }
    }
}