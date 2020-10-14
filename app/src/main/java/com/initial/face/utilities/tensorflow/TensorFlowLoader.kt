package com.initial.face.utilities.tensorflow

import android.app.Activity
import android.content.res.AssetFileDescriptor
import com.initial.face.R
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TensorFlowLoader() {
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = activity.assets.openFd(activity.getString(R.string.facial_expression_model_file_name))
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset: Long = fileDescriptor.startOffset
        val declaredLength: Long = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun getInterpreter(activity: Activity): Interpreter {
        return Interpreter(loadModelFile(activity), Interpreter.Options());
    }
}