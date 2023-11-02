package com.narehase.runa

import android.content.res.AssetManager
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.*
import org.tensorflow.lite.InterpreterApi.create
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


class ModelHelper(
    val luna: Luna,
    val assetManager: AssetManager
) {
    lateinit var inter:InterpreterApi

    init {
        setModel()
    }
    fun setModel() {
        val options = InterpreterApi.Options()
        options.numThreads = 3

        options.setNumThreads(3)
        options.useNNAPI = true

        val input = listOf(1, 2);
        val output = listOf(1);
        inter = create(loadModelFile(), options)

        val inputTensor = inter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        val modelInputChannel = inputShape[0]
        val modelInputWidth = inputShape[1]

        val outputTensor = inter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        val modelOutputClasses = outputShape[1]
        Log.d("Model Info", "| InPut | ${inputTensor.shape().toList()}, ${inputTensor.dataType()} |  | OutPut | ${outputShape.toList()}, ${outputTensor.dataType()} | <--|")
        luna.connected(true)
    }

    fun Anyway() {
        val datas = listOf<Int>(0, 100)
        val input = Array(1) { IntArray(2) { 1 } }
        val result = Array(1) { FloatArray(1) { 0f } }
        input[0][0] = datas[0]
        input[0][1] = datas[1]

        var inferenceTime = SystemClock.uptimeMillis()

        inter.run(input, result)

        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        luna.onResult(result, inferenceTime)
    }


    private fun loadModelFile(): ByteBuffer {
        val assetFileDescriptor = assetManager.openFd(modelPath)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    interface Luna{
        fun onResult(
            results: Array<FloatArray>,
            inferenceTime: Long
        ){
        }
        fun connected(tf:Boolean) {
        }
    }
    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
        const val modelPath = "tf.tflite"

        private const val TAG = "LUNA"
    }
}
