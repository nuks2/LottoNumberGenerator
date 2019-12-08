package com.example.lottonumbergenerator.utils

interface Responsable<T> {
    fun onResponse(responseCode: Int, response: T?)
    fun onError(responseCode: Int, response: T?)
}