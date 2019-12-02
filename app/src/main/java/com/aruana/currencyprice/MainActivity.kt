package com.aruana.currencyprice

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.aruana.currencyprice.currency.CurrencyViewModel
import com.aruana.currencyprice.databinding.ActivityMainBinding
import com.aruana.currencyprice.di.createViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = createViewModel { (application as MyApplication).appComponent.viewModel }

        super.onCreate(savedInstanceState)

        setUpUi(firstLaunch = savedInstanceState == null)
    }

    private fun setUpUi(firstLaunch: Boolean) {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (firstLaunch) {
            viewModel.updateCurrencyRateList()
        }

        viewModel.state.observe(this, Observer { state ->
            handleState(state)
        })

        setUpScrollToFirstListItemWhenCurrencyListIsUpdatedObserver()
        setUpListeners()
    }

    private fun handleState(state: CurrencyViewModel.State?) {
        when (state) {
            CurrencyViewModel.State.LOADING -> goToLoadingState()
            CurrencyViewModel.State.FINISHED_LOADING -> goToFinishedLoadingState()
            else -> progressBar.visibility = View.GONE
        }
    }

    private fun setUpListeners() {
        viewModel.adapter.itemClickListener = { index, _ ->
            viewModel.changeBaseCurrency(index)
        }

        viewModel.adapter.priceChangeListener = { index, newText ->
            removeFocusFromPriceEditText()
            viewModel.calculateNewPrices(index, newText)
        }

        viewModel.adapter.startEditingPriceListener = {
            viewModel.stopRequestingNewCurrencyData()
        }
    }

    private fun removeFocusFromPriceEditText() {
        parentLayout.requestFocus()
        closeSoftKeyboard()
    }

    private fun goToFinishedLoadingState() {
        hideProgressBar()
        enableUiInteraction()
    }

    private fun goToLoadingState() {
        disableUiInteraction()
        showProgressBar()
    }

    private fun enableUiInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun disableUiInteraction() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun setUpScrollToFirstListItemWhenCurrencyListIsUpdatedObserver() {
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                recyclerView.scrollToPosition(0)
            }
        })
    }

    private fun closeSoftKeyboard() {
        try {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error closing keyboard")
        }
    }
}