package com.initial.face.utilities.facerecognition

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.initial.face.models.FaceWithEmotion
import com.initial.face.utilities.bitmap.BitmapCropper
import com.initial.face.utilities.bitmap.YuvToRgbConverter
import com.initial.face.utilities.emotionrecognition.FaceImagePreprocessor
import com.initial.face.utilities.emotionrecognition.RecognizedEmotions
import com.initial.face.utilities.ui.FaceMarker
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class FaceProcessor(faceMarker: FaceMarker) {

    private val faceMarker = faceMarker

    private val faceDetectionOptions = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
    private val detector = FaceDetection.getClient(faceDetectionOptions)

    private val IMAGE_CORRECTION_ROTATION = 90f


    @SuppressLint("UnsafeExperimentalUsageError")
    fun processFaces(faceInterpreter: Interpreter, imageProxy: ImageProxy, rotation: Int) {
        imageProxy.image?.let {
            detector.process(InputImage.fromMediaImage(it, rotation))
                .addOnSuccessListener { faces ->
                    val facesWithEmotions = ArrayList<FaceWithEmotion>()
                    val image = correctImageRotation(
                        imageProxy.convertImageProxyToBitmap(),
                        IMAGE_CORRECTION_ROTATION
                    )
                    image?.let {
                        for (face in faces) {
                            face.boundingBox.adjustBoundsTo(image)
                            val croppedFace = BitmapCropper().cropBitmap(image, face.boundingBox)
                            val preprocessedFace = FaceImagePreprocessor().preprocess(croppedFace)
                            val faceInterpreterOutputBuffer =
                                FloatBuffer.allocate(RecognizedEmotions.getRecognizedEmotionAmount())
                            faceInterpreter.run(preprocessedFace, faceInterpreterOutputBuffer)
                            var mostProbableEmotionProbability = 0f
                            var mostProbableEmotionIndex = 0
                            for (i in 0 until RecognizedEmotions.getRecognizedEmotionAmount()) {
                                if (mostProbableEmotionProbability < faceInterpreterOutputBuffer.get(
                                        i
                                    )
                                ) {
                                    mostProbableEmotionProbability =
                                        faceInterpreterOutputBuffer.get(i)
                                    mostProbableEmotionIndex = i
                                }
                            }
                            facesWithEmotions.add(
                                FaceWithEmotion(
                                    face,
                                    RecognizedEmotions.getEmotionNameByIndex(
                                        mostProbableEmotionIndex
                                    ),
                                    mostProbableEmotionProbability
                                )
                            )
                        }
                    }
                    faceMarker.faces = facesWithEmotions
                    faceMarker.invalidate()
                    imageProxy.close()
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

    private fun correctImageRotation(image: Bitmap?, rotation: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(rotation)
        image?.let {
            return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, false)
        }
        return null
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    fun ImageProxy.convertImageProxyToBitmap(): Bitmap? {
        val image = this.image
        image?.let {
            val bmp = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            YuvToRgbConverter(faceMarker.context).yuvToRgb(image, bmp)
            return bmp
        }
        return null
    }

    private fun Rect.adjustBoundsTo(image: Bitmap) {
        if (this.left <= 0) {
            this.left = 0;
        }
        if (this.top <= 0) {
            this.top = 0
        }
        if (this.right >= image.width) {
            this.right = image.width
        }
        if (this.bottom >= image.height) {
            this.bottom = image.height
        }
    }
}