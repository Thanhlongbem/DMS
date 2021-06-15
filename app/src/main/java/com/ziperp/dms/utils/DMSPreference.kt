package com.ziperp.dms.utils

import android.content.Context
import android.content.SharedPreferences
import com.ziperp.dms.App

class DMSPreference {

    companion object {
        val SP_NAME = "dms_sp"

        fun getBoolean(key: String, defaultValue: Boolean): Boolean {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return preferences.getBoolean(key, defaultValue)
        }

        fun setBoolean(key: String, defaultValue: Boolean) {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean(key, defaultValue)
            editor.apply()
        }

        fun getInt(key: String, defaultValue: Int): Int {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return preferences.getInt(key, defaultValue)
        }

        fun setInt(key: String, defaultValue: Int) {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putInt(key, defaultValue)
            editor.apply()
        }

        fun setLong(key: String, defaultValue: Long) {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putLong(key, defaultValue)
            editor.apply()
        }

        fun getLong(key: String, defaultValue: Long): Long {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return preferences.getLong(key, defaultValue)
        }

        fun getString(key: String, defaultValue: String = ""): String {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return preferences.getString(key, defaultValue) ?: ""
        }

        fun setString(key: String, value: String) {
            val preferences = App.shared().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        private fun clearPrefs(prefName: String) {
            val preferences = App.shared().getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.commit()
        }

        fun getSharePref(context: Context, prefName: String, mode: Int = Context.MODE_PRIVATE): SharedPreferences? {
            return context.getSharedPreferences(prefName, mode)
        }

    }

}