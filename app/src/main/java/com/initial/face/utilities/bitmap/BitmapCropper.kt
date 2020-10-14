package com.initial.face.utilities.bitmap

import android.graphics.Bitmap
import android.graphics.Rect

class BitmapCropper {
    fun cropBitmap(input: Bitmap, rectangle: Rect): Bitmap {
        return Bitmap.createBitmap(
            input,
            rectangle.left,
            rectangle.top,
            rectangle.width(),
            rectangle.height()
        )
    }
}