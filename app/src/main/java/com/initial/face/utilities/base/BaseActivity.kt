package com.initial.face.utilities.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createActivityView())
        onActivityReady()
    }

    abstract fun createActivityView(): View
    abstract fun onActivityReady()
}
