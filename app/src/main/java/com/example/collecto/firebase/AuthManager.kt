package com.example.collecto.firebase

import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    //firebase에서 제공하는 로그인 객체.
    //로그인/회원가입 요청을 보낼 수 있음

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // 회원가입 성공 후 바로 로그인 시도 (Compose가 상태 변화 감지하게)
                    login(email, password, onResult)
                } else {
                    onResult(false, it.exception?.message)
                }
            }
    }


    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        //성공,실패에 따라 onResult를 실행시켜줘서 ui에 알려줌
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) onResult(true,null)
                else onResult(false, it.exception?.message)
            }
    }

    //로그아웃
    fun logout() = auth.signOut()

    //현재 로그인된 유저가 있는지 확인
    fun isLoggedIn() = auth.currentUser != null
}