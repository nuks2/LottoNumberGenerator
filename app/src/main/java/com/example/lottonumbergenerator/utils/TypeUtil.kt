package com.example.lottonumbergenerator.utils

import android.content.Context


fun Int.toResourceString(): String? {
    return App.instance.context().getString(this)
}



