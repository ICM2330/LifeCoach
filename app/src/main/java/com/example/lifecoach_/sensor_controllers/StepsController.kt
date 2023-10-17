package com.example.lifecoach_.sensor_controllers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat

class StepsController private constructor(): SensorEventListener {
    companion object {
        @Volatile
        private var instance: StepsController? = null

        fun getStepsController() {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = StepsController()
                    }
                }
            }
        }
    }

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    private var listeners: MutableList<() -> Unit> = mutableListOf()

    fun configureStepSensor(context: Context) {
        if (sensorManager == null) {
            sensorManager = ContextCompat.getSystemService(context, SensorManager::class.java)
            if (sensorManager != null) {
                sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
                sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    fun registerStepsListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("STEPS", "Accurracy Changed")
    }
}