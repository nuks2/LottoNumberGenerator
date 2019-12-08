package com.example.codingtest.view

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.model.Lotto
import kotlinx.android.synthetic.main.view_win_numbers_item.view.*


class PreviousWinNumberRecyclerViewAdapter()
    : RecyclerView.Adapter<PreviousWinNumberRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, ">> onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_win_numbers_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, ">> onBindViewHolder ${position}")
        val keyList = Lotto.instance.winNumberTable.keys.toList()
        val order = keyList.sorted().get(position)
        val numbers = Lotto.instance.winNumberTable.get(order)

        holder.tvOrder.text = order.toString() + "회"
        val winNumber = numbers?.slice(0..5)
        val bonus = numbers?.last()
        holder.tvWinNumbers.text = winNumber.toString() + " + Bonus(${bonus})"
    }

    override fun getItemCount(): Int {
        return Lotto.instance.winNumberTable.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvOrder: TextView = view.tv_order
        val tvWinNumbers: TextView = view.tv_win_numbers
    }

    companion object {
        val TAG = "LotteryGenerator"
    }
}
