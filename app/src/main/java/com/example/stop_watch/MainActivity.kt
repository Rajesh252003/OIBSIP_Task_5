package com.example.stop_watch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updatedTime: Long = 0L
    private var isRunning = false
    private lateinit var handler: Handler
    private lateinit var timeDisplay: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        timeDisplay = findViewById(R.id.timeDisplay)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        handler = Handler()

        // Start button
        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(updateTimerThread, 0)
                isRunning = true
                resetButton.isEnabled = false // Disable reset button when running
            }
        }

        // Stop button
        stopButton.setOnClickListener {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                isRunning = false
                resetButton.isEnabled = true // Enable reset button when stopped
            }
        }

        // Reset button
        resetButton.setOnClickListener {
            timeSwapBuff = 0L
            timeInMilliseconds = 0L
            updatedTime = 0L
            handler.removeCallbacks(updateTimerThread)
            timeDisplay.text = "00:00:00:000"
            isRunning = false
        }
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            val secs = (updatedTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60
            val milliseconds = (updatedTime % 1000).toInt()

            timeDisplay.text = String.format("%02d:%02d:%02d:%03d", hrs, mins % 60, secs % 60, milliseconds)

            handler.postDelayed(this, 0)
        }
    }
}
