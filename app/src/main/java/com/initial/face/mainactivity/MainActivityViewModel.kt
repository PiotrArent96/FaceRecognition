package com.initial.face.mainactivity


import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.initial.face.utilities.camera.CameraStreamHandler
import com.initial.face.utilities.ui.FaceMarker
import com.initial.face.utilities.permissions.PermissionHelper
import com.initial.face.utilities.tensorflow.TensorFlowLoader

class MainActivityViewModel : ViewModel() {

    private lateinit var cameraHandler: CameraStreamHandler

    fun handleInitialPermissions(permissionHelper: PermissionHelper){
        if(!permissionHelper.isInitialPermissionsGranted()) {
            permissionHelper.requestInitialPermissions()
        }
    }

    fun startCameraStream(activity: MainActivity, previewView: PreviewView, faceMarker: FaceMarker) {
        cameraHandler = CameraStreamHandler(
            TensorFlowLoader().getInterpreter(activity),
            ProcessCameraProvider.getInstance(activity),
            ContextCompat.getMainExecutor(activity)
        )
        cameraHandler.startStream(activity as LifecycleOwner, previewView, faceMarker)
    }
}