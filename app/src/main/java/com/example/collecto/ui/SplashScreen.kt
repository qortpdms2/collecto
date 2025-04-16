package com.example.collecto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)//2초
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F6FA)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = "Collecto",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color=Color(0xFF2D3436)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "세상 모든 링크를 한 곳에✨",
                fontSize = 16.sp,
                color = Color(0xFF636E72)
            )
        }
    }
}