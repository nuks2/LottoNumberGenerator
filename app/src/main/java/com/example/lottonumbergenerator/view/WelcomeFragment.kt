package com.example.lottonumbergenerator.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.databinding.FragmentWelcomeBinding
import com.example.lottonumbergenerator.utils.doTransaction
import com.example.lottonumbergenerator.viewmodel.LottoViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : BaseFragment() {
    override val commandMap: Map<String, (Any?) -> Unit> = super.commandMap.plus(mapOf(
        CMD_WELCOME_MOVE_TO_MAIN to ::showMainFragment
    ))

    var viewModel: LottoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWelcomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.viewModel = viewModel

        return binding.getRoot()
    }

    fun showMainFragment(command: Any?) {
        val fragment = MainFragment.newInstance()
        fragment.viewModel = viewModel
        fragmentManager?.doTransaction {
            add(R.id.cl_main_container, fragment)
        }
    }

    companion object {
        val TAG = WelcomeFragment::class.java.simpleName

        val CMD_WELCOME_MOVE_TO_MAIN = "CMD_WELCOME_MOVE_TO_MAIN"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WelcomeFragment.
         */
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }
}
