package br.com.concrete.tentacle.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.compress(quality: Int): ByteArray {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, quality, bos)

    return bos.toByteArray()
}