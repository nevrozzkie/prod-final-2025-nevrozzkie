package utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

val ByteArray?.bitmap: Bitmap?
    get() = this?.let { BitmapFactory.decodeByteArray(this, 0, this.size) }

// care of "Failed to create image decoder with message 'unimplemented'"
val ByteArray?.isValid: Boolean
    get() = this.bitmap != null

val ByteArray?.validated: ByteArray?
    get() = if(isValid) this else null


fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}