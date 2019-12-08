package com.example.lottonumbergenerator.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.model.Lotto
import com.example.lottonumbergenerator.utils.App
import com.example.lottonumbergenerator.utils.PreferenceUtil
import com.example.lottonumbergenerator.utils.Responsable
import com.example.lottonumbergenerator.view.Command
import com.example.lottonumbergenerator.view.MainFragment
import com.example.lottonumbergenerator.view.SplashFragment
import com.example.lottonumbergenerator.view.WelcomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus
import java.lang.invoke.MethodHandles


class LottoViewModel : BaseObservable() {
    @Bindable
    var expectedWinNumber: String? = ""
        set(value) {
            notifyPropertyChanged(BR.expectedWinNumber)
            field = value
        }

    @Bindable
    var order: String? = ""
        set(value) {
            field = value
        }

    fun initialize() {
        val jsonWinNumberTable = PreferenceUtil.winNumbers
        val type = object : TypeToken<MutableMap<Int, List<Int>>>(){}.type
        val winNumberTable: MutableMap<Int, List<Int>> = Gson().fromJson(jsonWinNumberTable, type)

        Lotto.instance.winNumberTable = winNumberTable

        moveToNext()
    }

    fun inquiryNumber(from : Int, to : Int) {
        Lotto.instance.inquiryNumber(from, to, object : Responsable<String> {
            override fun onResponse(responseCode: Int, response: String?) {
                Log.d(TAG, ">> onResponse")
                val test = response

                val jsonWinNumberTable = Gson().toJson(Lotto.instance.winNumberTable);
                Log.d(TAG, "jsonWinNumberTable : ${jsonWinNumberTable}")
                PreferenceUtil.winNumbers = jsonWinNumberTable

                moveToNext()
            }

            override fun onError(responseCode: Int, response: String?) {
                Toast.makeText(App.instance.applicationContext, "Please restart App because an error occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun moveToNext() {
        if (PreferenceUtil.isFirstRun!!) {
            EventBus.getDefault().post(Command(SplashFragment.CMD_SPLASH_MOVE_TO_WELCOME))
            PreferenceUtil.isFirstRun = false
        } else {
            EventBus.getDefault().post(Command(SplashFragment.CMD_SPLASH_MOVE_TO_MAIN))
        }
    }

    fun onMoveToMain() {
        EventBus.getDefault().post(Command(WelcomeFragment.CMD_WELCOME_MOVE_TO_MAIN))
    }

    fun onGenerate() {
        expectedWinNumber = Lotto.instance.shuffle().joinToString(separator = ",") + "(Bonus)"
    }

    fun onLookupWinNumber() {
        if (order.isNullOrBlank() || order?.toInt()!! > 50) {
            EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_TOAST,"잘못된 회차입니다. "))
            return
        }

        if (Lotto.instance.expectedWinNumbers.size < 7) {
            EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_TOAST,"잘못된 번호입니다 생성해 주세요"))
            return
        }

        val winGrade = Lotto.instance.lookup(order?.toInt()!!)
        val message : String
        when (winGrade) {
            Lotto.WinGrade.First -> message = "축하드립니다. 1등에 당첨되셨습니다."
            Lotto.WinGrade.Second -> message = "축하드립니다. 2등에 당첨되셨습니다."
            Lotto.WinGrade.Third -> message = "축하드립니다. 3등에 당첨되셨습니다."
            Lotto.WinGrade.Fourth -> message = "축하드립니다. 4등에 당첨되셨습니다."
            Lotto.WinGrade.Fifth -> message = "축하드립니다. 5등에 당첨되셨습니다."
            Lotto.WinGrade.None -> message = "아쉽게도 당첨이 되지 못했습니다."
        }

        val numbers = Lotto.instance.winNumberTable.get(order?.toInt()!!)
        val winNumber = numbers?.slice(0..5)
        val bonus = numbers?.last()
        val winNumberMsg = order.toString() + "회 " + winNumber.toString() + " + Bonus(${bonus})"

        EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_LOOKUP_RESULT, Pair(winNumberMsg,message)))
    }

    fun onShowPreviousWinNumber() {
        Log.d(TAG,">> onShowPreviousWinNumber")
        EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_PREVIOUS))
    }

    fun onShowFrequentNumbers() {
        Log.d(TAG,">> onShowFrequentNumbers")
        EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_FREQUENT_NUMBERS))
    }

    fun makeupFrequentNumberTable() {
        Lotto.instance.makeupFrequentNumberTable()
    }

    fun isRunDeepLink() {
        Log.d("DeepLink",">> isRunDeepLink")
        if (PreferenceUtil.runDeepLink.isNullOrBlank()) {
            return
        }

        val drwNo = PreferenceUtil.runDeepLink
        Log.d("DeepLink","drwNo : ${drwNo}")
        if (drwNo?.toInt()!! > 50) {
            EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_TOAST,"잘못된 회차입니다. "))
            return
        }

        val numbers = Lotto.instance.winNumberTable.get(drwNo.toInt())
        val winNumber = numbers?.slice(0..5)
        val bonus = numbers?.last()
        val winNumberMsg = drwNo + "회 " + winNumber.toString() + " + Bonus(${bonus})"

        Log.d("DeepLink","winNumberMsg : ${winNumberMsg}")
        EventBus.getDefault().post(Command(MainFragment.CMD_MAIM_SHOW_DEEP_LINK, winNumberMsg))
    }

    companion object {
        val TAG = "LotteryGenerator"
    }
}
