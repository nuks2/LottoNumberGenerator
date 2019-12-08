package com.example.lottonumbergenerator.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lottonumbergenerator.R
import com.example.lottonumbergenerator.utils.PreferenceUtil
import com.example.lottonumbergenerator.utils.doTransaction
import com.example.lottonumbergenerator.viewmodel.LottoViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : BaseFragment() {
    override val commandMap: Map<String, (Any?) -> Unit> = super.commandMap.plus(mapOf(
        CMD_SPLASH_MOVE_TO_WELCOME to ::showWelcomeFragment,
        CMD_SPLASH_MOVE_TO_MAIN to ::showMainFragment
    ))

    private var param: String? = null
    val viewModel: LottoViewModel = LottoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        if (PreferenceUtil.winNumbers.isNullOrBlank()) {
            viewModel.inquiryNumber(1, 50)
        } else {
            viewModel.initialize()
        }
    }

    fun showWelcomeFragment(command: Any?) {
        val fragment = WelcomeFragment.newInstance()
        fragment.viewModel = viewModel
        showFragment(fragment)
    }

    fun showMainFragment(command: Any?) {
        val fragment = MainFragment.newInstance()
        fragment.viewModel = viewModel
        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager?.doTransaction {
            add(R.id.cl_main_container, fragment)
        }
    }

    companion object {
        val TAG = SplashFragment::class.java.simpleName

        val CMD_SPLASH_MOVE_TO_WELCOME = "CMD_SPLASH_MOVE_TO_WELCOME"
        val CMD_SPLASH_MOVE_TO_MAIN = "CMD_SPLASH_MOVE_TO_MAIN"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SplashFragment.
         */
        @JvmStatic
        fun newInstance(param: String) = SplashFragment()
    }
}
