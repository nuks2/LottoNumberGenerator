package com.example.lottonumbergenerator.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingtest.view.PreviousWinNumberRecyclerViewAdapter

import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.databinding.FragmentPreviousWinNumbersBinding


/**
 * A simple [Fragment] subclass.
 * Use the [PreviousWinNumbersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreviousWinNumbersFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPreviousWinNumbersBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_previous_win_numbers, container, false)

        binding.rvWinNumberList.layoutManager = LinearLayoutManager(context)
        binding.rvWinNumberList.adapter = PreviousWinNumberRecyclerViewAdapter()

        return binding.getRoot()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PreviousWinNumbersFragment.
         */
        @JvmStatic
        fun newInstance() = PreviousWinNumbersFragment()
    }
}
