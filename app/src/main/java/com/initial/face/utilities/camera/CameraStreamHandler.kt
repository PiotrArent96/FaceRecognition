package com.initial.face.utilities.camera

import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.initial.face.utilities.facerecognition.FaceProcessor
import com.initial.face.utilities.ui.FaceMarker
import org.tensorflow.lite.Interpreter
import java.util.concurrent.Executor

class CameraStreamHandler(
    private val tensorFlowInterpreter: Interpreter,
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    private val executor: Executor,
) {

    private lateinit var camera: Camera
    private val imageAnalysisTargetResolution = Size(1080, 1440)

    fun startStream(
        lifecycleOwner: LifecycleOwner, previewView: PreviewView, faceMarker: FaceMarker
    ) {
        cameraProviderFuture.addListener(Runnable {
            bindPreview(
                cameraProviderFuture.get(),
                lifecycleOwner,
                previewView,
                FaceProcessor(faceMarker)
            )
        }, executor)
    }

    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        faceProcessor: FaceProcessor
    ) {
        val preview: Preview = Preview.Builder()
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            getImageAnalysis(faceProcessor),
            preview
        )
    }

    private fun getImageAnalysis(faceProcessor: FaceProcessor): ImageAnalysis {
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(imageAnalysisTargetResolution)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer { imageProxy ->
            faceProcessor.processFaces(
                tensorFlowInterpreter,
                imageProxy,
                imageProxy.imageInfo.rotationDegrees
            )
        })

        return imageAnalysis;
    }
}