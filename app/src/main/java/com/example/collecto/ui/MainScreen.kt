package com.example.collecto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collecto.firebase.AuthManager

@Composable
fun MainScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 상단: 환영 메시지
        Column {
            Text(
                text = "Collecto에 오신 걸 환영해요 👋",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "즐겨찾는 웹사이트를 손쉽게 저장하고 정리해보세요.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )
        }

        // 중간: 기능 버튼 (예시)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MainFeatureButton("웹사이트 추가하기") {
                // TODO: 웹사이트 추가 화면으로 이동
            }

            MainFeatureButton("폴더 보기") {
                // TODO: 폴더 화면으로 이동
            }

            MainFeatureButton("태그로 찾기") {
                // TODO: 태그 검색 화면으로 이동
            }
        }

        // 하단: 로그아웃 버튼
        Button(
            onClick = {
                AuthManager.logout()
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = "로그아웃",
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MainFeatureButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        )
    }
}
