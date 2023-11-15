package com.example.lifecoach_.controllers.sensor_controllers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat

class ThemeController private constructor(): SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var darkMode = false
    private var darkListeners: MutableList<() -> Unit> = mutableListOf()
    private var lightListeners: MutableList<() -> Unit> = mutableListOf()

    companion object {
        @Volatile
        private var instance: ThemeController? = null

        fun getThemeController(): ThemeController {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ThemeController()
                    }
                }
            }

            return instance!!
        }
    }

    fun configureLightSensor(context: Context) {
        if (sensorManager == null) {
            sensorManager = ContextCompat.getSystemService(context, SensorManager::class.java)
            sensorManager?.let {
                synchronized(it) {
                    if (sensorManager != null) {
                        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
                        Log.i("SENSOR", "Registering Sensor Listener")
                        if (lightSensor != null) {
                            sensorManager!!.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
                        }
                    }
                }
            }
        }
    }

    fun registerThemeModeListeners(enableDarkMode: () -> Unit, disableDarkMode: () -> Unit) {
        darkListeners.add(enableDarkMode);
        lightListeners.add(disableDarkMode)
    }

    fun unregisterThemeModeListeners(enableDarkMode: () -> Unit, disableDarkMode: () -> Unit) {
        darkListeners.remove(enableDarkMode)
        lightListeners.remove(disableDarkMode)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                Log.i("SENSOR", "Sensor Light: " + event.values[0])
                if (event.values[0] < 100) {
                    if (!darkMode) {
                        darkMode = true
                        darkListeners.forEach {
                            it()
                        }
                    }
                } else {
                    if (darkMode) {
                        darkMode = false
                        lightListeners.forEach {
                            it()
                        }
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("SENSOR", "Sensor Accurracy Changed")
    }
}