package com.android.currencyAPP.ui.fragments.currency_converter_fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.annotation.SuppressLint
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.android.currencyAPP.R
import com.android.currencyAPP.databinding.ConverterFragmentBinding
import com.android.currencyAPP.ui.MainActivity
import com.android.currencyAPP.ui.MainActivityViewModel
import com.android.currencyAPP.util.ConverterAction
import com.android.currencyAPP.util.ConverterState
import com.android.currencyAPP.util.DialogBuilder.showDialog
import com.android.currencyAPP.util.FocusHelper.hideKeyboard
import com.android.currencyAPP.util.ImageLoaderHelper
import com.android.currencyAPP.util.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class CurrencyConverterFragment : DaggerFragment(), AdapterView.OnItemSelectedListener,
    NestedScrollView.OnScrollChangeListener {
    lateinit var binding: ConverterFragmentBinding
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var conversionsAdapter: ConversionListAdapter
    lateinit var currencyViewModel: CurrencyFragmentViewModel

    var sendSpinnerAdapter: ArrayAdapter<String>? = null

    var receiveSpinnerAdapter: ArrayAdapter<String>? = null

    var spinningAnimation: ObjectAnimator? = null
    var alphaAnimation: ObjectAnimator? = null
    var convertAnimation = AnimatorSet()

    lateinit var activityViewModel: MainActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModels()
        setupBinding(inflater, container)
        handleObservers()
        handleReceiver()
        setAnimations()
        setDecorates()
        spinnerListeners()
        binding.nestedScrollView.setOnClickListener { }


        return binding.root

    }


    private fun setupViewModels() {
        currencyViewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(CurrencyFragmentViewModel::class.java)

        activityViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(MainActivityViewModel::class.java)
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = ConverterFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            currencyVm = currencyViewModel
            latestConversionsList.adapter = conversionsAdapter
            exchangeButton.setOnClickListener { convert() }
            nestedScrollView.setOnScrollChangeListener(this@CurrencyConverterFragment)


        }
    }


    private fun spinnerListeners() {
        binding.apply {
            firstCurrencyPicker.onItemSelectedListener = this@CurrencyConverterFragment
            secondCurrencyPicker.onItemSelectedListener = this@CurrencyConverterFragment
        }
    }

    private fun convert() {
        hideKeyboard(activity!!, binding.firstCurrencyEditText)

        binding.apply {
            if (!firstCurrencyEditText.text.isNullOrBlank() && firstCurrencyPicker.selectedItem.toString() != secondCurrencyPicker.selectedItem.toString() && firstCurrencyEditText.text.toString().toDouble() != 0.0) {
                val firstSelectedItem = firstCurrencyPicker.selectedItem.toString()
                val secondSelectedItem = secondCurrencyPicker.selectedItem.toString()
                currencyViewModel.manageAction(
                    firstSelectedItem,
                    secondSelectedItem,
                    firstCurrencyEditText.text.toString().toDouble(),
                    ConverterAction.Convert
                )
            }
        }

    }

    private fun handleReceiver() {
        binding.apply {
            firstCurrencyEditText.addTextChangedListener {
                updateReceiver(it)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateReceiver(editable: Editable?) {
        binding.apply {
            if (firstCurrencyPicker.selectedItem == null || secondCurrencyPicker.selectedItem == null) return
            if (firstCurrencyPicker.selectedItem.toString() == secondCurrencyPicker.selectedItem.toString() || firstCurrencyEditText.text.isNullOrBlank()) {
                currencyViewModel.resetValues()
                return
            }
            val value = if (!editable.isNullOrBlank()) editable.toString().toDouble() else 0.00
            val firstSelectedItem = firstCurrencyPicker.selectedItem.toString()
            val secondSelectedItem = secondCurrencyPicker.selectedItem.toString()
            currencyViewModel.manageAction(
                firstSelectedItem,
                secondSelectedItem,
                value,
                ConverterAction.PeekReceive
            )
        }
    }


    private fun handleObservers() {
        activityViewModel.balancesLiveData.observe(
            viewLifecycleOwner,
            Observer { listOfBalances ->
                if (listOfBalances.isNotEmpty())  //Setup spinners data/adapters
                    lifecycleScope.launch(Dispatchers.Main) {
                        val mapped =
                            withContext(Dispatchers.Default) { listOfBalances.map { it.type } }
                        launch(Dispatchers.Main) { setSpinnersAdapters(mapped) }
                    }
            })



        currencyViewModel.convertStates.observe(viewLifecycleOwner, Observer {
            //Conversions states observer
            when (it) {

                is ConverterState.OnConvertStart -> {
                    binding.exchangeButton.isClickable = false
                    blinkAnimation(binding.secondValueText).start()
                    blinkAnimation(binding.feeCostText).start()
                    blinkAnimation(binding.totalCostText).start()
                    playConvertAnimation()
                }
                is ConverterState.OnBalanceZero -> {
                    currencyViewModel.resetValues() // set all values to zero
                }

                is ConverterState.OnConvertFinish -> {

                    // reset  state cause livedata is triggered after configurations changes, and after convert it will call complete state again

                    binding.latestConversionsList.smoothScrollToPosition(0)
                    showDialog(
                        activity!!,
                        getString(R.string.success),
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
                            getString(
                                R.string.conversion_success,
                                it.sendValue,
                                it.fromType,
                                it.receiveValue,
                                it.toType,
                                it.feeCost,
                                it.fromType
                            ), 0
                        ) else Html.fromHtml(
                            getString(
                                R.string.conversion_success,
                                it.sendValue,
                                it.fromType,
                                it.receiveValue,
                                it.toType,
                                it.feeCost,
                                it.fromType
                            )
                        )

                    )
                    currencyViewModel.resetConvertState()
                    binding.exchangeButton.isClickable = true
                    playConvertAnimation()
                    firstCurrencyEditText.text?.clear()
                    binding.latestConversionsList.requestLayout()
                }
            }
        })

        currencyViewModel.conversionList.observe(viewLifecycleOwner, Observer {
            binding.countText.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
                    getString(
                        R.string.count,
                        it?.size
                    ), 0
                )
                else Html.fromHtml(getString(R.string.count, it?.size))

            conversionsAdapter.submitList(it)
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


    private fun playConvertAnimation() = convertAnimation.apply {
        if (isRunning) cancel() else start()
    }


    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {  //Images changing handle when select different picker items
        if (view == null) return

        val drawableId = resources.getIdentifier(
            "${(view as TextView).text.toString().toLowerCase(
                Locale.ROOT
            )}flag", "drawable", activity!!.packageName
        )
        val flagImageView = when (parent?.id) {
            R.id.firstCurrencyPicker -> binding.firstCurrencyImage
            R.id.secondCurrencyPicker -> binding.secondCurrencyImage
            else -> return
        }

        ImageLoaderHelper.loadFromDrawableId(drawableId, flagImageView)
        updateReceiver(binding.firstCurrencyEditText.editableText)
    }


    private fun setSpinnersAdapters(currenciesTypeList: List<String>) {
        if (sendSpinnerAdapter == null || currenciesTypeList.size != sendSpinnerAdapter?.count) { // only update spinner if items count is not the same with actual data
            sendSpinnerAdapter = ArrayAdapter(
                activity!!,
                R.layout.spinner_layout,
                currenciesTypeList
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.firstCurrencyPicker.adapter = this
            }

            receiveSpinnerAdapter = ArrayAdapter(
                activity!!,
                R.layout.spinner_layout,
                currenciesTypeList
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.secondCurrencyPicker.adapter = this
            }

        }

    }

    private fun setAnimations() {
        spinningAnimation =
            ObjectAnimator.ofFloat(binding.spinIcon, View.ROTATION, 360f).apply {
                repeatCount = INFINITE
            }
        alphaAnimation =
            ObjectAnimator.ofFloat(binding.exchangeButton, "alpha", 1f, 0.6f, 1f).apply {
                repeatCount = INFINITE
            }
        convertAnimation.apply {
            playTogether(spinningAnimation, alphaAnimation)
            duration = 1000
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(
                "selected1",
                binding.firstCurrencyPicker.selectedItemPosition
            )  // Save selected items in spinners
            putInt("selected2", binding.secondCurrencyPicker.selectedItemPosition)
            putFloat("scrollX", binding.nestedScrollView.x)
            putFloat("scrollY", binding.nestedScrollView.y)
        }
        super.onSaveInstanceState(outState)
    }

    private fun setDecorates() {
        LinearGradient(
            0f, 0f, 300f, 0f,
            ContextCompat.getColor(activity!!,R.color.colorAccent),
            ContextCompat.getColor(activity!!, R.color.colorPrimaryVariant),
            Shader.TileMode.CLAMP
        ).also { binding.recentConversions.paint.shader = it }
    }


    private fun blinkAnimation(view: View) =
        ObjectAnimator.ofFloat(view, "alpha", 1f, 0.4f, 1f).apply {
            // need to work on this anim

            duration = 2500

        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            binding.firstCurrencyPicker.setSelection(it.getInt("selected1"))  // set selections after configuration changes
            binding.secondCurrencyPicker.setSelection(it.getInt("selected2"))
            binding.firstCurrencyPicker.refreshDrawableState()

        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        (activity!! as MainActivity).binding.constraintLayout.updateState()
        if (binding.firstCurrencyEditText.isFocused) binding.firstCurrencyEditText.apply {
            clearFocus()

            hideKeyboard(
                this@CurrencyConverterFragment.activity!!,
                this
            ) // lose focus and hide keyboard on click
        }

    }

}


