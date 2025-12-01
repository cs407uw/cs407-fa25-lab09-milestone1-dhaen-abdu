package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    // Expose the ball's position as a StateFlow
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(fieldWidth, fieldHeight, ballSizePx)
            ball?.let { b ->
                _ballPosition.value = Offset(b.posX, b.posY)
            }
        }
    }

    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            if (lastTimestamp != 0L) {
                // Calculate time difference (dT) in seconds
                // event.timestamp is in nanoseconds (1e-9 conversion)
                val dT = (event.timestamp - lastTimestamp) / 1_000_000_000f

                // Map Sensor Coordinates to Screen Coordinates
                // Screen X = Sensor X
                // Screen Y = -Sensor Y (Inverted)
                val ACCELERATION_SCALE = 50f
                val xAcc = -event.values[0] * ACCELERATION_SCALE
                val yAcc = event.values[1] * ACCELERATION_SCALE

                currentBall.updatePositionAndVelocity(xAcc, yAcc, dT)

                _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
            }

            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        ball?.reset()
        ball?.let { b ->
            _ballPosition.update { Offset(b.posX, b.posY) }
        }
        lastTimestamp = 0L
    }
}