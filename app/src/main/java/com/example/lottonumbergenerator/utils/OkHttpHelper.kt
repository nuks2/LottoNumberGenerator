package com.example.lottonumbergenerator.utils

import android.util.Log
import androidx.annotation.NonNull
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class OkHttpHelper private constructor(protocol: Protocol?) {
    interface Callback {
        fun onResponse(tag: String, responseCode: Int, response: String?)
        fun onError(tag: String, responseCode: Int, response: String?)
    }

    class Protocol {
        var request: Request? = null
    }

    var protocol: Protocol? = null
        private set

    init {
        this.protocol = protocol
    }

    open class Base {
        var requestBuilder: Request.Builder = Request.Builder()
            protected set
    }

    class Get : Base() {
        var url: String? = null
            private set

        fun uri(@NonNull interfaceUri: String): OkHttpHelper.Get {
            this.url = interfaceUri
            return this
        }

        fun parameter(@NonNull params: Map<String, String>): OkHttpHelper.Get {
            val urlBuilder = HttpUrl.parse(this.url!!)!!.newBuilder()
            for (param in params) {
                urlBuilder.addQueryParameter(param.key, param.value)
            }

            this.url = urlBuilder.build().toString()
            return this
        }

        fun build(): OkHttpHelper {
            val getRequest = requestBuilder.url(url!!).build()
            val protocol = Protocol().apply {
                request = getRequest
            }

            return OkHttpHelper(protocol)
        }
    }



    fun request(callback: Callback, tag: String = "",
                connectTimeout: Long = 10000L, writeTimeout: Long = 10000L, readTimeout: Long = 10000L) {


        val HttpClient = OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS).build()
        Log.d(TAG, "request url : " + protocol?.request?.url()?.url())

        HttpClient.newCall(protocol?.request!!).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

                Log.d(TAG, ">> onResponse: " + response.code())
                val result = response.body()?.string();
                Log.d(TAG, ">> result: " + result)
                if (response.code() == 200) {
                    callback.onResponse(tag, response.code(), result)
                } else {
                    callback.onError(tag, response.code(), result)
                }
            }
        })
    }

    companion object {
        val TAG = OkHttpHelper::class.java.simpleName
        val NETWORK_CONNECT_TIMEOUT_ERROR = 599
    }
}