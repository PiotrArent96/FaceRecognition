package com.initial.face.models

import com.google.mlkit.vision.face.Face

data class FaceWithEmotion(val face: Face, val emotion: String, val probability: Float)