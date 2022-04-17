package com.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val NAME = "ChatApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreference: SharedPreferences
    fun init(context: Context) {
        sharedPreference = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var signUp: Boolean?
        get() = sharedPreference.getBoolean("signUp", false)
        set(value) = sharedPreference.edit {
            it.putBoolean("signUp", value!!)
        }
    var userExist: Boolean?
        get() = sharedPreference.getBoolean("userExist", false)
        set(value) = sharedPreference.edit {
            it.putBoolean("userExist", value!!)
        }
}