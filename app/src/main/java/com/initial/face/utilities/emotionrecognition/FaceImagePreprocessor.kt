package com.initial.face.utilities.emotionrecognition

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class FaceImagePreprocessor() {

    private val PREPROCESS_OUTPUT_HEIGHT = 64.0
    private val PREPROCESS_OUTPUT_WIDTH = 64.0
    private val PREPROCESS_CORE_DEVIDE_SCALAR_PARAMETER = 127.5

    fun preprocess(input: Bitmap): Array<Array<Array<FloatArray>>> {
        val imageGrab = Mat()
        Utils.bitmapToMat(input, imageGrab)
        val mat = Mat()
        Imgproc.cvtColor(imageGrab, mat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.resize(mat, mat, Size(PREPROCESS_OUTPUT_HEIGHT, PREPROCESS_OUTPUT_WIDTH), -1.0, -1.0, Imgproc.INTER_AREA)
        mat.convertTo(mat, CvType.CV_32F)
        Core.divide(mat, Scalar(PREPROCESS_CORE_DEVIDE_SCALAR_PARAMETER, PREPROCESS_CORE_DEVIDE_SCALAR_PARAMETER, PREPROCESS_CORE_DEVIDE_SCALAR_PARAMETER), mat)
        Core.subtract(mat, Scalar(1.0, 1.0, 1.0), mat)
        return faceImageInputMatToArray(mat);
    }

    private fun faceImageInputMatToArray(mat: Mat): Array<Array<Array<FloatArray>>> {
        return Array(1) {
            Array(PREPROCESS_OUTPUT_HEIGHT.toInt()) { i ->
                Array(PREPROCESS_OUTPUT_WIDTH.toInt()) { j ->
                    val floatArray = FloatArray(1)
                    floatArray[0] = mat[i, j][0].toFloat()
                    floatArray
                }
            }
        }
    }
}