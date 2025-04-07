package com.example.collecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.collecto.firebase.AuthManager
import com.example.collecto.ui.AuthScreen
import com.example.collecto.ui.MainScreen
import com.example.collecto.ui.theme.CollectoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectoTheme {
                var loggedIn by remember { mutableStateOf(AuthManager.isLoggedIn()) }

                if (loggedIn) {
                    MainScreen(onLogout = { loggedIn = false })
                } else {
                    AuthScreen(onLoginSuccess = { loggedIn = true })
                }
            }
        }
    }
}
