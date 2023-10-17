package com.example.lifecoach_.sensor_controllers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.lifecoach_.model.internal.StepCounter

class StepsController private constructor(): SensorEventListener {
    companion object {
        @Volatile
        private var instance: StepsController? = null

        fun getStepsController(): StepsController {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = StepsController()
                    }
                }
            }
            return instance!!
        }
    }

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    private var stepCount: Int? = null
    private var listeners: MutableList<StepCounter> = mutableListOf()

    fun configureStepSensor(context: Context) {
        if (sensorManager == null) {
            Log.i("STEPS", "Getting Sensor Manager")
            sensorManager = ContextCompat.getSystemService(context, SensorManager::class.java)
            if (sensorManager != null) {
                Log.i("STEPS", "Got Sensor Manager. Configuring Sensor")
                sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
                Log.i("STEPS", "Registering listener")
                sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                Log.i("STEPS", "Listener Registered")
            }
        }
    }

    fun registerStepsListener(listener: (steps: Int) -> Unit) {
        Log.i("STEPS", "Added Step Counter to Controller")
        listeners.add(StepCounter(listener))
    }

    fun unregisterListener(listener: (steps: Int) -> Unit) {
        listeners.remove(StepCounter(listener))
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            Log.i("STEPS", "Sensor Change Detected")
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                Log.i("STEPS", "Sensor is Step Counter")
                stepCount = event.values[0].toInt()
                listeners.forEach {
                    if (it.prevStepsCount == null) {
                        it.prevStepsCount = stepCount
                    }
                    it.listener(stepCount!! - it.prevStepsCount!!)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("STEPS", "Accurracy Changed")
    }
}