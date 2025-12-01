package com.cs407.lab09

/**
 * Represents a ball that can move.
 *
 * Constructor parameters:
 * - backgroundWidth: the width of the background, of type Float
 * - backgroundHeight: the height of the background, of type Float
 * - ballSize: the width/height of the ball, of type Float
 */
class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * Implements linear acceleration model from Lab 9 manual.
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if (isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        // --- POSITION UPDATE (Equation 2) ---
        // l = v0 * t + (1/6) * t^2 * (3a0 + a1)
        posX += (velocityX * dT) + (dT * dT / 6f) * (3 * accX + xAcc)
        posY += (velocityY * dT) + (dT * dT / 6f) * (3 * accY + yAcc)

        // --- VELOCITY UPDATE (Equation 1) ---
        // v1 = v0 + 0.5 * (a1 + a0) * t
        velocityX += 0.5f * (xAcc + accX) * dT
        velocityY += 0.5f * (yAcc + accY) * dT

        // Update stored acceleration for the next frame
        accX = xAcc
        accY = yAcc

        checkBoundaries()
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary are set to 0.
     */
    fun checkBoundaries() {
        // Right Boundary
        if (posX + ballSize > backgroundWidth) {
            posX = backgroundWidth - ballSize
            velocityX = 0f
            accX = 0f
        }
        // Left Boundary
        else if (posX < 0) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        // Bottom Boundary
        if (posY + ballSize > backgroundHeight) {
            posY = backgroundHeight - ballSize
            velocityY = 0f
            accY = 0f
        }
        // Top Boundary
        else if (posY < 0) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }
    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        posX = (backgroundWidth - ballSize) / 2
        posY = (backgroundHeight - ballSize) / 2
        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f
        isFirstUpdate = true
    }
} //done milestone 1