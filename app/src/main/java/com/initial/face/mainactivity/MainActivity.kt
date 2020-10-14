package com.initial.face.mainactivity

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.initial.face.databinding.ActivityMainBinding
import com.initial.face.utilities.base.BaseActivity
import com.initial.face.utilities.permissions.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

class MainActivity : BaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: View
    private val permissionHelper = PermissionHelper(this)

    private val loaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    onLibrariesLoaded()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }


    override fun createActivityView(): View {
        binding = ActivityMainBinding.inflate(LayoutInflater.from(applicationContext)).root
        return binding
    }

    override fun onActivityReady() {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback)
        } else {
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    private fun onLibrariesLoaded() {
        viewModel.handleInitialPermissions(permissionHelper)
        if (permissionHelper.isInitialPermissionsGranted()) {
            initView()
        }
    }

    private fun initView() {
        viewModel.startCameraStream(this, cameraView, faceMarker)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHelper.CAMERA_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            } else {
                initView()
            }
        }
    }
}
