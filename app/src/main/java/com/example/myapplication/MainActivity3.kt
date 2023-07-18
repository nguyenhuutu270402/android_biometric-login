package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executor

class MainActivity3 : AppCompatActivity() {
    private lateinit var btn1: Button
    private lateinit var tv: TextView
    private val mainHandler = Handler(Looper.getMainLooper())
    private val mainExecutor = Executor { command -> mainHandler.post(command) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        btn1 = findViewById(R.id.button)
        tv = findViewById(R.id.textView1)


        btn1.setOnClickListener {
            if (isBiometricSupported(this)) {
                showBiometricPrompt()
            } else {
                // Biometric not supported on this device, handle it accordingly.
                tv.text = "Faillllllllllllllllllllllll"
            }
        }
    }


    private fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }


    private val biometricPromptCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            // Biometric authentication successful. Proceed with your application logic here.
            tv.text = "okkkkkkk"
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            // Biometric authentication error. Handle the error if needed.
        }

        override fun onAuthenticationFailed() {
            // Biometric authentication failed. Handle the failure if needed.
        }
    }


    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Place your face in front of the camera")
            .setNegativeButtonText("cann") // Set it to an empty string to hide the negative button
            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        val biometricPrompt = BiometricPrompt(this, mainExecutor, biometricPromptCallback)
        biometricPrompt.authenticate(promptInfo)
    }



}