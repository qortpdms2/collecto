package com.example.collecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.collecto.local.ThemeManager
import com.example.collecto.local.UrlManager
import com.example.collecto.local.UserManager
import com.example.collecto.model.Website
import com.example.collecto.ui.*
import com.example.collecto.ui.theme.CollectoTheme

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectoTheme {
                val context = LocalContext.current
                val userManager = remember {UserManager(context)}
                val urlManager = remember { UrlManager(context) }
                var loggedIn by remember { mutableStateOf(userManager.isLoggedIn()) }
                val websites = remember { mutableStateListOf<Website>() }
                var selectedFolder by remember { mutableStateOf("") }
                var showPopup by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf("main") }
                var showSplash by remember { mutableStateOf(true) }
                val themeManager = remember { ThemeManager(context) }

                LaunchedEffect(Unit) {
                    AppCompatDelegate.setDefaultNightMode(
                        if (themeManager.isDarkMode()) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    websites.clear()
                    websites.addAll(urlManager.loadUrls())
                }

                if (showPopup) {
                    LinkInputDialog(
                        onDismiss = {showPopup=false},
                        onSave = {website ->
                            websites.add(website)
                            urlManager.saveUrls(websites)
                        }
                    )
                }
                if(showSplash) {
                    SplashScreen(onSplashFinished = {
                        showSplash=false
                    })
                } else{
                    when {
                        !loggedIn -> AuthScreen(onLoginSuccess = {
                            loggedIn=true
                            currentScreen="main"
                        })
                        currentScreen == "main"-> {
                            val folderList = websites.map { it.folder }.distinct()
                            MainScreen(
                                websites=if(selectedFolder.isBlank()) websites else websites.filter { it.folder==selectedFolder },
                                folders = folderList,
                                selectedFolder = selectedFolder,
                                onSelectFolder = {selectedFolder=it},
                                onAddLinkClick = {showPopup = true},
                                onItemClick = {},
                                onItemDelete = {site ->
                                    websites.remove(site)
                                    urlManager.saveUrls(websites)
                                },
                                onProfileclick = {
                                    currentScreen="mypage"
                                }
                            )
                        }
                        currentScreen=="mypage" -> {
                            MyPageScreen(
                                onBack = { currentScreen = "main"},
                                onLogout = {
                                    userManager.logout()
                                    loggedIn=false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}