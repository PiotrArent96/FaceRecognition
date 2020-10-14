package com.initial.face.utilities.emotionrecognition

enum class RecognizedEmotions {
    ANGRY, DISGUST, FEAR, HAPPY, SAD, SURPRISE, NEUTRAL;

    companion object {
        fun getEmotionNameByIndex(i: Int): String {
            return values()[i].name
        }

        fun getRecognizedEmotionAmount(): Int {
            return values().size
        }
    }
}