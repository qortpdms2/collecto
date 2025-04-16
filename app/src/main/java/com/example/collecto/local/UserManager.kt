package com.example.collecto.local

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) { //앱 안에서 사용자 로그인 상태를 저장하고 확인

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    /*저장소 연결
    스마트폰 안에 user_prefs 이름의 저장공간 하나 만듦
    비휘발성 메모리
    */

    fun signUp(email: String, password:String):Boolean { //회원가입하면 자동으로 로그인됨.
        if(prefs.contains(email)) false //중복 방지
        prefs.edit().putString(email, password).apply()
        prefs.edit().putString("logged_in", email).apply() //logged_in이라는 키에 현재 로그인한 사용자 이메일 저장
        return true
    }


    fun login(email:String, password: String):Boolean {
        val isSuccess = email =="qortpdms2@naver.com" && password=="qwer!!"
        if (isSuccess) {
            prefs.edit().putBoolean("loggedIn",true).apply()
            prefs.edit().putString("email",email).apply()
        }
        val savedPW = prefs.getString(email, null)
        return if (savedPW == password) {
            prefs.edit().putString("logged_in", email).apply()
            true
        } else false
    }

    fun logout() = prefs.edit().clear().apply()

    fun isLoggedIn() = prefs.getBoolean("loggedIn",false)

    fun getCurrentUser() = prefs.getString("logged_in",null)

    fun saveProfile(nickname:String,email:String ) {
        prefs.edit().putString("nickname",nickname).putString("email",email)
    }
    fun loadProfile():Pair<String,String> {
        val nickname = prefs.getString("nickname","")?:""
        val email = prefs.getString("email","")?:""
        return nickname to email
    }
}
