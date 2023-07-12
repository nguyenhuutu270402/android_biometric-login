package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var tv1: TextView
    private lateinit var btn1: Button
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv1 = findViewById(R.id.tv1)
        btn1 = findViewById(R.id.btn1)

        btn1.setOnClickListener {
//            checkDeviceHasBiometric()
            createBiometricListener()
            createPromptInfo()
            biometricPrompt.authenticate(promptInfo)
        }

        executor = ContextCompat.getMainExecutor(this)


    }

    private fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        Toast.makeText(
            this@MainActivity,
            "Authenticated failed ${biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)}",
            Toast.LENGTH_SHORT
        )
            .show()
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                tv1.text = "App can authenticate using biometrics."
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                tv1.text = "No biometric features available on this device."
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                tv1.text = "Biometric features are currently unavailable."
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                tv1.text = "App can authenticate using biometrics."
            }
            else -> {
                tv1.text = "Something went wrong"
            }
        }
    }

    private fun createPromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for app")
            .setSubtitle("Log in using your biometric credential")
//            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .setNegativeButtonText("CANCEL BIOMETRIC")
            .build()
    }

    private fun createBiometricListener() {
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, errString, Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Authenticated failed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    tv1.text = "Authentication succeeded"
                    Toast.makeText(this@MainActivity, "Authenticated succeeded", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}