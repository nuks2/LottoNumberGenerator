package com.example.lottonumbergenerator.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.utils.PreferenceUtil
import com.example.lottonumbergenerator.utils.doTransaction


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            Log.d("DeepLink", uri.toString())
            val order = uri?.getQueryParameter("drwNo")
            Log.d("DeepLink", order!!)
            PreferenceUtil.runDeepLink = order
        } else {
            PreferenceUtil.runDeepLink = null
        }
    }
}
