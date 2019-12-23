package com.android.currencyAPP.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.currencyAPP.R
import com.android.currencyAPP.databinding.ActivityMainBinding
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.BASE_CURRENCY
import com.android.currencyAPP.util.LoadingStates
import com.android.currencyAPP.util.ViewModelFactory
import com.android.currencyAPP.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@Suppress("DEPRECATION")
class MainActivity : DaggerAppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var balancesAdapter: BalancesAdapter

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        navController = findNavController(R.id.main_fragment_container)
//        setupTopAppBar() // toolbar is not needed for now
        setupViewPagerWithTabs()
        handleObservers()
        binding.sparkview.adapter = SparkViewAdapter()

    }

    private fun setupViewPagerWithTabs() {
        binding.balancesViewPager.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = balancesAdapter
            TabLayoutMediator(binding.tabLayout, binding.balancesViewPager, true) { tab, position ->
                tab.text = balancesAdapter.currentList[position].type
            }.attach()
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.getBoolean("isTransitionEnd")) binding.constraintLayout.transitionToEnd()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isTransitionEnd", binding.constraintLayout.currentState == R.id.end)
        super.onSaveInstanceState(outState)

    }
//    private fun setupTopAppBar() {
//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.setupWithNavController(navController)
//    }

    private fun handleObservers() {
        viewModel.balancesLiveData.observe(this, Observer { balanceList ->
            balancesAdapter.submitList(balanceList)
            binding.balanceValue.text = Html.fromHtml(
                getString(
                    R.string.balanceEur,
                    balanceList.find { it.type == BASE_CURRENCY }?.balanceValue
                )
            )
        })

        viewModel.loadingStates.observe(this, Observer {
            when(it) {
                LoadingStates.START -> showLoadingScreen()
                LoadingStates.FAILED -> hideLoadingScreen()
                LoadingStates.FINISH -> hideLoadingScreen()
                else -> hideLoadingScreen()
            }
        })
    }

    private fun showLoadingScreen() {
        binding.progressBarContainer.visibility = View.VISIBLE
        binding.spinKit.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        binding.progressBarContainer.visibility = View.INVISIBLE
        binding.spinKit.visibility = View.INVISIBLE
    }
}
