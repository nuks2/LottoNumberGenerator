package com.example.lottonumbergenerator.view


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil

import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.databinding.FragmentMainBinding
import com.example.lottonumbergenerator.utils.doTransaction
import com.example.lottonumbergenerator.viewmodel.LottoViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment() {
    override val commandMap: Map<String, (Any?) -> Unit> = super.commandMap.plus(mapOf(
        CMD_MAIM_SHOW_LOOKUP_RESULT to ::showLookupDialog,
        CMD_MAIM_SHOW_PREVIOUS to ::showPreviousWinNumbersFragment,
        CMD_MAIM_SHOW_FREQUENT_NUMBERS to ::showFrequentNumbersFragment,
        CMD_MAIM_SHOW_DEEP_LINK to ::showDeepLink,
        CMD_MAIM_SHOW_TOAST to ::showToast
    ))

    var viewModel: LottoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewModel = viewModel

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,">> onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG,">> onStart")
        super.onStart()

        viewModel?.isRunDeepLink()
    }

    fun showLookupDialog(command: Any?) {
        val pair = command as Pair<*, *>
        val numberMsg = pair.first as String
        val message = pair.second as String
        val builder = AlertDialog.Builder(context!!)
        with(builder)
        {
                setTitle("생성된 번호 당첨조회")
            setMessage(numberMsg + "\n\n" + message)
            setPositiveButton("OK", null)
            show()
        }
    }

    fun showDeepLink(command: Any?) {
        Log.d("DeepLink",">> showDeepLink")
        val message = command as String
        val builder = AlertDialog.Builder(context!!)
        with(builder)
        {
            setTitle("DeepLink 조회")
            setMessage(message)
            setPositiveButton("OK", null)
            show()
        }
    }

    fun showPreviousWinNumbersFragment(command: Any?) {
        val fragment = PreviousWinNumbersFragment.newInstance()
        fragmentManager?.doTransaction {
            add(R.id.cl_main_container, fragment).addToBackStack(TAG)
        }
    }

    fun showFrequentNumbersFragment(command: Any?) {
        val fragment = FrequentNumbersFragment.newInstance()
        fragment.viewModel = viewModel
        fragmentManager?.doTransaction {
            add(R.id.cl_main_container, fragment).addToBackStack(TAG)
        }
    }

    fun showToast(command: Any?) {
        val msg = command as String
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName

        val CMD_MAIM_SHOW_LOOKUP_RESULT = "CMD_MAIM_SHOW_LOOKUP_RESULT"

        val CMD_MAIM_SHOW_PREVIOUS = "CMD_MAIM_SHOW_PREVIOUS"

        val CMD_MAIM_SHOW_FREQUENT_NUMBERS = "CMD_MAIM_SHOW_FREQUENT_NUMBERS"

        val CMD_MAIM_SHOW_DEEP_LINK = "CMD_MAIM_SHOW_DEEP_LINK"

        val CMD_MAIM_SHOW_TOAST = "CMD_MAIM_SHOW_TOAST"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
