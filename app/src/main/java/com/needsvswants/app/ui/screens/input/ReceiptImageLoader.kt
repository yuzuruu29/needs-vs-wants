package com.needsvswants.app.ui.screens.input

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import kotlin.math.max
import kotlin.math.roundToInt

/** Decodes receipt images to a bounded size suitable for on-device OCR. */
object ReceiptImageLoader {
    private const val MAX_DIMENSION = 2400

    fun decode(contentResolver: ContentResolver, uri: Uri): Bitmap {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            decodeWithImageDecoder(contentResolver, uri)
        } else {
            decodeWithBitmapFactory(contentResolver, uri)
        }
        return bitmap?.let { normalizeOrientation(contentResolver, uri, it) }
            ?: throw IOException("The selected image could not be decoded")
    }

    // The decode call sites below need API 28; [decode] only routes here past
    // the SDK_INT >= P check, which @RequiresApi makes visible to lint.
    @RequiresApi(Build.VERSION_CODES.P)
    private fun decodeWithImageDecoder(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        val source = ImageDecoder.createSource(contentResolver, uri)
        return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            val scale = minOf(1f, MAX_DIMENSION.toFloat() / max(info.size.width, info.size.height))
            decoder.setTargetSize(
                (info.size.width * scale).roundToInt().coerceAtLeast(1),
                (info.size.height * scale).roundToInt().coerceAtLeast(1)
            )
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
    }

    @Suppress("DEPRECATION")
    private fun decodeWithBitmapFactory(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, bounds)
        }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSize(bounds.outWidth, bounds.outHeight)
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }
    }

    private fun sampleSize(width: Int, height: Int): Int {
        var sample = 1
        while (max(width / sample, height / sample) > MAX_DIMENSION * 2) sample *= 2
        return sample
    }

    private fun normalizeOrientation(contentResolver: ContentResolver, uri: Uri, bitmap: Bitmap): Bitmap {
        val orientation = runCatching {
            contentResolver.openInputStream(uri)?.use { ExifInterface(it).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) } ?: ExifInterface.ORIENTATION_NORMAL
        }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix().apply {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> postScale(1f, -1f)
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    postScale(-1f, 1f)
                    postRotate(270f)
                }
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    postScale(-1f, 1f)
                    postRotate(90f)
                }
            }
        }
        if (matrix.isIdentity) return bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            .also { if (it !== bitmap) bitmap.recycle() }
    }

    fun clearCache(cacheDir: java.io.File) {
        cacheDir.resolve("receipt-images").listFiles()?.forEach { it.delete() }
    }
}
