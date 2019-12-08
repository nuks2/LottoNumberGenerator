package com.example.lottonumbergenerator.utils

import android.content.SharedPreferences
import com.example.lottonumbergenerator.R


object PreferenceUtil {
    private val preferences: SharedPreferences = App.instance.context().getSharedPreferences(R.string.app_name.toResourceString(), 0)

    private const val PREF_FIRST_RUN = "PREF_FIRST_RUN"
    private const val PREF_WIN_NUMBERS = "PREF_WIN_NUMBERS"
    private const val PREF_RUN_DEEPLINK = "PREF_RUN_DEEPLINK"

    var isFirstRun: Boolean?
        get() = get(PREF_FIRST_RUN, true)
        set(value) = set(PREF_FIRST_RUN, value)

    var runDeepLink: String?
        get() = get(PREF_RUN_DEEPLINK, "")
        set(value) = set(PREF_RUN_DEEPLINK, value)

    var winNumbers: String?
        get() = get(PREF_WIN_NUMBERS, "")
        set(value) = set(PREF_WIN_NUMBERS, value)


    private fun set(key: String, value: Any?) {
        when (value) {
            is String? -> preferences.edit().putString(key, value).apply()
            is Int -> preferences.edit().putInt(key, value).apply()
            is Boolean -> preferences.edit().putBoolean(key, value).apply()
            is Float -> preferences.edit().putFloat(key, value).apply()
            is Long -> preferences.edit().putLong(key, value).apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preferences.getString(key, defaultValue as? String) as T?
            Int::class -> preferences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preferences.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preferences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preferences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}
