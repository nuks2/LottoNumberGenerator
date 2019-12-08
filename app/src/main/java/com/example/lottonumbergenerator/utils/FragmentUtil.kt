package com.example.lottonumbergenerator.utils


inline fun androidx.fragment.app.FragmentManager.doTransaction(func: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) {
    beginTransaction().func().commit()
}


