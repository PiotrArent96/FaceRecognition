package com.initial.face.utilities.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager

class PermissionHelper(private val activity: Activity) {

    companion object {
        const val CAMERA_REQUEST_CODE = 1;

        private val INITIAL_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    fun requestInitialPermissions() {
        activity.requestPermissions(INITIAL_PERMISSIONS, CAMERA_REQUEST_CODE)
    }

    fun isInitialPermissionsGranted(): Boolean {
        var permissionGranted = true
        activity.applicationContext?.let {
            for (permission in INITIAL_PERMISSIONS) {
                permissionGranted =
                    permissionGranted && it.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return permissionGranted
    }

}