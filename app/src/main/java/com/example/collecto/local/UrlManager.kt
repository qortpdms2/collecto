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

    fun loadUrls() : List<Website> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<Website>() {}.type
        return gson.fromJson(json, type)
    }
}