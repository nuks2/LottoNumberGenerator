package com.example.codingtest.view

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.model.Lotto
import kotlinx.android.synthetic.main.view_frequent_numbers_item.view.*
import kotlinx.android.synthetic.main.view_win_numbers_item.view.*


class FrequentNumberRecyclerViewAdapter()
    : RecyclerView.Adapter<FrequentNumberRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, ">> onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_frequent_numbers_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, ">> onBindViewHolder ${position}")
        val keyList = Lotto.instance.frequentNumberTable.keys.toList().sortedDescending()
        val count = keyList.get(position)
        val numberList = Lotto.instance.frequentNumberTable.get(count)

        holder.tvFrequentNumbers.text = "${count}번 : ${numberList?.sorted().toString()}"
    }

    override fun getItemCount(): Int {
        return Lotto.instance.frequentNumberTable.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvFrequentNumbers: TextView = view.tv_frequent_numbers
    }

    companion object {
        val TAG = "LotteryGenerator"
    }
}
