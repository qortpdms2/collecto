package com.example.collecto.local

import android.content.Context
import com.example.collecto.model.Website
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UrlManager(context:Context) {
    private val prefs = context.getSharedPreferences("url_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "urls"

    fun saveUrls(list:List<Website>) {
        val json = gson.toJson(list)
        prefs.edit().putString(key,json).apply()
    }

    fun clearUrls(){
        prefs.edit().remove(key).apply()
    }

    fun loadUrls(): List<Website> {
        return try {
            val json = prefs.getString(key, null) ?: return emptyList()
            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
            val rawList: List<Map<String, Any>> = gson.fromJson(json, type)

            rawList.map {
                val url = it["url"] as? String ?: ""
                val folder = it["folder"] as? String ?: "기타"
                Website(url, folder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}