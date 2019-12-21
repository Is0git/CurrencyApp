package com.android.currencyAPP.ui

import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.currencyAPP.R
import com.android.currencyAPP.databinding.ActivityMainBinding
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.BASE_CURRENCY
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.main_fragment_container)
//        setupTopAppBar() // toolbar is not needed for now

    }


//    private fun setupTopAppBar() {
//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.setupWithNavController(navController)
//    }

}
