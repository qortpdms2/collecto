package com.example.collecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.collecto.local.UserManager
import com.example.collecto.ui.AuthScreen
import com.example.collecto.ui.MainScreen
import com.example.collecto.ui.theme.CollectoTheme

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectoTheme {
                val context = LocalContext.current
                val userManager = remember {UserManager(context)}
                var loggedIn by remember { mutableStateOf(userManager.isLoggedIn()) }

                if(loggedIn) {
                    MainScreen(onLogout={
                        userManager.logout()
                        loggedIn=false
                    })
                } else AuthScreen(onLoginSuccess = {loggedIn = true})
            }
        }
    }
}
