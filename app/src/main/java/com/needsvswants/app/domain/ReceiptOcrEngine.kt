package com.needsvswants.app.domain

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun interface ReceiptOcrProcessor {
    suspend fun recognizeReceipt(bitmap: Bitmap): Result<ReceiptScanResult>
}

/**
 * On-device OCR engine bridging Google ML Kit Text Recognition with [ReceiptParser].
 * Processing runs 100% locally on the device with zero cloud requests.
 */
@Singleton
class ReceiptOcrEngine @Inject constructor() : ReceiptOcrProcessor {

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    override suspend fun recognizeReceipt(bitmap: Bitmap): Result<ReceiptScanResult> = withContext(Dispatchers.Default) {
        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val ocrText = suspendCancellableCoroutine { continuation ->
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        if (continuation.isActive) {
                            continuation.resume(visionText.text)
                        }
                    }
                    .addOnFailureListener { exception ->
                        if (continuation.isActive) {
                            continuation.resumeWithException(exception)
                        }
                    }
            }
            val scanResult = ReceiptParser.parse(ocrText)
            Result.success(scanResult)
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
