package com.example.lifecoach_.sensor_controllers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat

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

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                Log.i("ACCEL", "Aceleration ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("SENSOR", "Accelerometer Accurracy Changed")
    }
}