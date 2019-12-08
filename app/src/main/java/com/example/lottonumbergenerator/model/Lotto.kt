package com.example.lottonumbergenerator.model

import android.os.Handler
import android.util.Log
import com.example.lottonumbergenerator.utils.OkHttpHelper
import com.example.lottonumbergenerator.utils.Responsable
import com.google.gson.Gson


data class WinNumber(val totSellamnt:String, val returnValue:String, val drwNoDate:String,
                     val firstWinamnt:Long, val drwtNo6:Int, val drwtNo4:Int, val firstPrzwnerCo:Int,
                     val drwtNo5:Int, val bnusNo:Int, val firstAccumamnt:String, var drwNo:Int, val drwtNo2:Int,
                     val drwtNo3:Int, val drwtNo1:Int)


class Lotto private constructor() {
    enum class WinGrade {
        First, Second, Third, Fourth, Fifth, None
    }

    // 이전 당첨번호 저장 테이블
    var winNumberTable = mutableMapOf<Int, List<Int>>()
    // 번호 빈도 테이블
    var frequentNumberTable = mutableMapOf<Int, MutableList<Int>>()
    // 생성 번호
    var expectedWinNumbers:MutableList<Int> = mutableListOf()


    private var currentIndex = 0
    private var toIndex = 0
    fun inquiryNumber(from : Int, to : Int, commpletion: Responsable<String>) {
        currentIndex = from
        toIndex = to

        inquiryNumber(currentIndex++, commpletion)
    }

    private fun inquiryNumber(index : Int, commpletion: Responsable<String>) {
        val url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=${index}"
        Log.d(TAG, "Url : ${url}")

        OkHttpHelper
            .Get()
            .uri(url)
            .build()
            .request(object : OkHttpHelper.Callback {
                override fun onResponse(tag: String, responseCode: Int, response: String?) {
                    Log.d(TAG, "requestBankList -> responseCode : $responseCode")
                    Log.d(TAG, "requestBankList -> response : ${response}")

                    val winNumber = Gson().fromJson(response, WinNumber::class.java)
                    putWinNumberTable(winNumber)
                    Log.d(TAG, "index : ${index}")

                    if (index >= toIndex) {
                        commpletion.onResponse(responseCode, responseCode.toString())
                    } else {
                        inquiryNumber(currentIndex++, commpletion)
                    }
                }

                override fun onError(tag: String, responseCode: Int, response: String?) {
                    Log.d(TAG, "requestBankList Error -> responseCode : $responseCode")
                    Log.d(TAG, "requestBankList Error -> response : ${response}")

                    val winNumber = WinNumber("",  "", "", 0,
                        0, 0, 0, 0,
                        0, "", 0, 0, 0,
                        0)
                    winNumber.drwNo = index
                    putWinNumberTable(winNumber)
                    Log.d(TAG, "index : ${index}")

                    if (index >= toIndex) {
                        commpletion.onResponse(responseCode, responseCode.toString())
                    } else {
                        inquiryNumber(currentIndex++, commpletion)
                    }
                }
            })
    }

    private fun putWinNumberTable(winNumber : WinNumber) {
        val winNumberList = listOf<Int>(winNumber.drwtNo1, winNumber.drwtNo2, winNumber.drwtNo3, winNumber.drwtNo4,
            winNumber.drwtNo5, winNumber.drwtNo6, winNumber.bnusNo)

        winNumberTable.put(winNumber.drwNo, winNumberList)
    }

    fun shuffle() : List<Int>  {
        expectedWinNumbers.clear()

        val numberTable = mutableMapOf<Int, Int>()
        for (i in LOTTO_MIN_VALUE..LOTTO_MAX_VALUE) {
            numberTable.put(i, i)
        }

        val shuffleCount = 0
        return shuffle(numberTable, shuffleCount)
    }

    private fun shuffle(numberTable: MutableMap<Int, Int>, count: Int) : List<Int> {
        if (count > LOTTO_MAX_COUNT) {
            Log.d(TAG, expectedWinNumbers.toString())
            return expectedWinNumbers
        }

        // 번호 뽑기 및 해당 번호 제거
        val key = (LOTTO_MIN_VALUE..LOTTO_MAX_VALUE-count).random()
        val value = numberTable.remove(key)
        Log.d(TAG, "Number: ${value}")
        expectedWinNumbers.add(value!!)
        val shuffleCount = count+1
        if (shuffleCount == LOTTO_MAX_COUNT) {
            expectedWinNumbers.sort()
        }

        Log.d(TAG, "shuffleCount: ${shuffleCount}")
        // 인덱스 기반으로 다시 구성
        val valueList = numberTable.values.toList()
        numberTable.clear()
        for (i in LOTTO_MIN_VALUE..LOTTO_MAX_VALUE-shuffleCount) {
            numberTable.put(i, valueList[i-1])
        }

        return shuffle(numberTable, shuffleCount)
    }

    fun lookup(order: Int) : WinGrade {
        val winNumbers = winNumberTable.get(order)
        val winNumbersExceptBonus = winNumbers?.slice(0..5)
        val expectedWinNumbersExceptBonus = expectedWinNumbers.slice(0..5)
        var matchCount = 0
        for (number in expectedWinNumbersExceptBonus) {
            if (winNumbersExceptBonus?.contains(number)!!) {
                matchCount += 1
            }
        }

        var isBonus = false
        if (winNumbers?.last() == expectedWinNumbers.last()) {
            isBonus = true
        }

        when(matchCount) {
            6-> return WinGrade.First
            5-> {
                if (isBonus) {
                    return WinGrade.Second
                } else {
                    return WinGrade.Third
                }
            }
            4-> return WinGrade.Fourth
            3-> return WinGrade.Fifth
            else-> return WinGrade.None
        }
    }

    fun makeupFrequentNumberTable() : MutableMap<Int, MutableList<Int>> {
        if (frequentNumberTable.size > 0) {
            return frequentNumberTable
        }

        // 횟수세
        val countTable = mutableMapOf<Int, Int>()
        val numbersList = winNumberTable.values.toList()
        for (numbers in numbersList) {
            for (number in numbers) {
                var count = countTable.get(number)
                if (count == null) {
                    count = 1
                } else {
                    count += 1
                }

                Log.d(TAG, "${number} : ${count}" )
                countTable.put(number, count)
            }
        }

        // 빈도별로 다시 구성
        val tmpTable = mutableMapOf<Int, MutableList<Int>>()
        val numberKeys = countTable.keys.toList()
        for (key in numberKeys) {
            val value = countTable.get(key)
            var numberList = tmpTable.get(value)
            if (numberList == null) {
                numberList = mutableListOf(key)
            } else {
                numberList.add(key)
            }
            tmpTable.put(value!!, numberList)
        }
        frequentNumberTable = tmpTable.toSortedMap()

        return frequentNumberTable
    }

    companion object {
        val TAG = "LotteryGenerator"

        val instance: Lotto = Lotto()

        val LOTTO_MAX_VALUE = 45

        val LOTTO_MIN_VALUE = 1

        // 0 Base
        val LOTTO_MAX_COUNT = 6
    }
}