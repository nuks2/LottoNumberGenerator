package com.example.lottonumbergenerator.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingtest.view.FrequentNumberRecyclerViewAdapter

import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.databinding.FragmentFrequentNumbersBinding
import com.example.lottonumbergenerator.viewmodel.LottoViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FrequentNumbersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FrequentNumbersFragment : Fragment() {
    var viewModel: LottoViewModel? = null
    val adapter = FrequentNumberRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentFrequentNumbersBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_frequent_numbers, container, false)

        binding.rvFrequentNumberList.layoutManager = LinearLayoutManager(context)
        binding.rvFrequentNumberList.adapter = adapter

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.makeupFrequentNumberTable()
        adapter.notifyDataSetChanged()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FrequentNumbersFragment.
         */
        @JvmStatic
        fun newInstance() = FrequentNumbersFragment()
    }
}
