package com.android.currencyAPP.ui.fragments.currency_converter_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyAPP.data.database.entities.Conversion
import com.android.currencyAPP.databinding.RecentConvertionsListBinding
import com.android.currencyAPP.di.activity.currency_fragment.CurrencyFragmentScope
import javax.inject.Inject

@CurrencyFragmentScope
class ConversionListAdapter @Inject constructor() :
    PagedListAdapter<Conversion, ConversionListAdapter.MyViewHolder>(diffCallback) {
    class MyViewHolder(val binding: RecentConvertionsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RecentConvertionsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.conversion = getItem(position)
    }

}

val diffCallback = object : DiffUtil.ItemCallback<Conversion>() {
    override fun areItemsTheSame(oldItem: Conversion, newItem: Conversion): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Conversion, newItem: Conversion): Boolean =
        oldItem == newItem
}

