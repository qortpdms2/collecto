package com.example.collecto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collecto.local.UserManager
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) { //onLoginSuccess는 로그인 성공 시 할 행동을 전달 받음.
    val focusRequester = remember {FocusRequester()}
    val context = LocalContext.current//지금 이 화면이 실행되고 있는 앱의 환경(context)를 들고옴
    val userManager = remember { UserManager(context) }//context로 usermanager를 만듦
    var email by remember { mutableStateOf("") }//사용자 입력 이메일 값 저장
    var password by remember{ mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }//true-login/false-signup
    var message by remember { mutableStateOf<String?>(null) }//안내메세지

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
    }


    Column (
        modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if(isLoginMode) "로그인" else "회원가입",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            textAlign = TextAlign.Start
        )

        OutlinedTextField(
            value =email,
            onValueChange = {email = it},
            label =  { Text("이메일") },
            singleLine = true,//한줄만 입력하도록
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next //키보드 엔터키 대신 다음 버튼이 뜨게 함.
            ),
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label =  {Text("비밀번호")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
                onClick = {
                    if (isLoginMode) {
                        val success = userManager.login(email, password)
                        message = if (success) {
                            onLoginSuccess()
                            null
                        } else {
                            "이메일 또는 비밀번호가 일치하지 않습니다."
                        }
                    } else {
                        val success = userManager.signUp(email, password)
                        message = if (success) {
                            onLoginSuccess()
                            null
                        } else {
                            "이미 가입된 이메일입니다."
                        }
                    }
                },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
                shape = RoundedCornerShape(12.dp)
        ) {
                Text(
                    text = if(isLoginMode) "로그인하기" else "회원가입하기",
                    style =  MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(
                text = if (isLoginMode) "계정이 없으신가요? 회원가입" else "이미 계정이 있으신가요? 로그인",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        message?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
            }
        }
    }
