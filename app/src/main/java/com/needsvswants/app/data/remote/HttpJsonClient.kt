package com.needsvswants.app.data.remote

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
        try {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            headers.forEach { (k, v) -> conn.setRequestProperty(k, v) }
            if (body != null) {
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Content-Length", body.toByteArray().size.toString())
                conn.outputStream.use { it.write(body.toByteArray()) }
            }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val text = stream?.bufferedReader()?.use(BufferedReader::readText) ?: ""
            conn.disconnect()
            if (code in 200..299) Result.success(text) else Result.failure(RuntimeException("HTTP $code: $text"))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}