package com.aruana.currencyprice.currency

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import com.aruana.currencyprice.R
import com.aruana.currencyprice.databinding.DataBindingAdapter
import com.aruana.currencyprice.databinding.DataBindingViewHolder
import com.aruana.currencyprice.networking.CurrencyModel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val TIME_TO_WAIT_FOR_TEXT_INPUT = 900L

class CurrenciesListAdapter @Inject constructor() : DataBindingAdapter<CurrencyModel>(DiffCallback()) {

    var itemClickListener: ((Int, View) -> Unit)? = null

    var priceChangeListener: ((Int, String) -> Unit)? = null

    var startEditingPriceListener: (() -> Unit)? = null

    private val editTextChangesDisposableColection = CompositeDisposable()

    class DiffCallback : DiffUtil.ItemCallback<CurrencyModel>() {
        override fun areItemsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel) = oldItem.base == newItem.base

        override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel) =
                oldItem.base == newItem.base && oldItem.rate == newItem.rate && oldItem.price == newItem.price
    }

    override fun getItemViewType(position: Int) = R.layout.list_item

    override fun onBindViewHolder(holder: DataBindingViewHolder<CurrencyModel>, position: Int) {
        super.onBindViewHolder(holder, position)

        with(holder.itemView) {
            setOnClickListener {
                itemClickListener?.invoke(holder.adapterPosition, it)
            }

            editTextPrice.setOnFocusChangeListener { _, hasFocus ->
                setUpTextChangeListener(editTextPrice, hasFocus, holder.adapterPosition)
            }
        }
    }

    private fun setUpTextChangeListener(editText: EditText, hasFocus: Boolean, position: Int) {
        editTextChangesDisposableColection.clear()

        if (hasFocus) {
            startEditingPriceListener?.invoke()

            val disposable = editText
                    .textChanges()
                    .skip(1)
                    .debounce(TIME_TO_WAIT_FOR_TEXT_INPUT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.toString() }
                    .subscribe({
                        priceChangeListener?.invoke(position, it)
                    }, {})

            editTextChangesDisposableColection.add(disposable)
        }
    }
}