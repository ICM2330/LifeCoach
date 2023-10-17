package com.example.lifecoach_.sensor_controllers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.Date

class MotionController private constructor(): SensorEventListener {
    companion object {
        @Volatile
        private var instance: MotionController? = null

        fun getMotionController(): MotionController {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = MotionController()
                    }
                }
            }

            return instance!!
        }
    }

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var isMoving = true
    private var lastTimeAccelerated = Date().time

    private var motionListeners = mutableListOf<() -> Unit>()
    private var noMotionListeners = mutableListOf<() -> Unit>()

    fun configureAccelerometer(context: Context) {
        if (sensorManager == null) {
            sensorManager = ContextCompat.getSystemService(context, SensorManager::class.java)
            if (sensorManager != null) {
                accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
                if (accelerometer != null) {
                    sensorManager!!.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        }
    }

    fun registerMotionListener(motionDetected: () -> Unit, noMotionDetected: () -> Unit) {
        motionListeners.add(motionDetected)
        noMotionListeners.add(noMotionDetected)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                Log.i("ACCEL", "Aceleration ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
                val threshold = 1.0f
                if (event.values[0] > threshold || event.values[1] > threshold || event.values[2] > threshold) {
                    lastTimeAccelerated = Date().time
                }

                val currentTime = Date().time
                if (currentTime-lastTimeAccelerated > 1000) {
                    Log.i("MOTION", "Not moving")
                    noMotionListeners.forEach {
                        it()
                    }
                } else {
                    Log.i("MOTION", "Moving")
                    motionListeners.forEach {
                        it()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("SENSOR", "Accelerometer Accurracy Changed")
    }
}