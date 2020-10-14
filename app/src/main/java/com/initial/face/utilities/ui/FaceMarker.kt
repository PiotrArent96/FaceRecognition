package com.initial.face.utilities.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.google.mlkit.vision.face.Face
import com.initial.face.R
import com.initial.face.models.FaceWithEmotion


class FaceMarker(context: Context, attrs: AttributeSet?=null): AppCompatImageView(context,attrs) {

    var faces : List<FaceWithEmotion> = ArrayList<FaceWithEmotion>()

    private val faceFramePaint = Paint()
    private val emotionDescriptionTextPaint = Paint()

    init {
        faceFramePaint.color = context.getColor(R.color.colorFaceDetectionFrame)
        faceFramePaint.strokeWidth = ResourcesCompat.getFloat(context.resources, R.dimen.face_detection_frame_width)
        faceFramePaint.style = Paint.Style.STROKE

        emotionDescriptionTextPaint.color = faceFramePaint.color
        emotionDescriptionTextPaint.strokeWidth = ResourcesCompat.getFloat(context.resources, R.dimen.emotion_description_text_width)
        emotionDescriptionTextPaint.textSize = context.resources.getDimension(R.dimen.emotion_description_text_size)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (faceWithEmotion in faces) {
            canvas?.drawRect(faceWithEmotion.face.boundingBox, faceFramePaint)
            if(faceWithEmotion.probability > 0) {
                canvas?.drawText(
                    faceWithEmotion.emotion,
                    faceWithEmotion.face.boundingBox.left.toFloat(),
                    faceWithEmotion.face.boundingBox.bottom.toFloat() + emotionDescriptionTextPaint.textSize + ResourcesCompat.getFloat(context.resources, R.dimen.emotion_description_text_margins), emotionDescriptionTextPaint
                )
            }
        }
    }
}