package com.initial.face.utilities.emotionrecognition

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class FaceImagePreprocessor() {
    fun preprocess(input: Bitmap): Array<Array<Array<FloatArray>>> {
        val imageGrab = Mat()
        Utils.bitmapToMat(input, imageGrab)
        val mat = Mat()
        Imgproc.cvtColor(imageGrab, mat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.resize(mat, mat, Size(64.0, 64.0), -1.0, -1.0, Imgproc.INTER_AREA)
        mat.convertTo(mat, CvType.CV_32F)
        Core.divide(mat, Scalar(127.5, 127.5, 127.5), mat)
        Core.subtract(mat, Scalar(1.0, 1.0, 1.0), mat)
        return faceImageInputMatToArray(mat);
    }

    private fun faceImageInputMatToArray(mat: Mat): Array<Array<Array<FloatArray>>> {
        return Array(1) {
            Array(64) { i ->
                Array(64) { j ->
                    val floatArray = FloatArray(1)
                    floatArray[0] = mat[i, j][0].toFloat()
                    floatArray
                }
            }
        }
    }
}