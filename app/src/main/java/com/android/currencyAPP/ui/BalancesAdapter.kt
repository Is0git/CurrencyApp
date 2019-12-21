package com.android.currencyAPP.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.databinding.BalancesItemLayoutBinding
import com.android.currencyAPP.di.activity.MainActivityScope
import com.android.currencyAPP.di.activity.currency_fragment.CurrencyFragmentScope
import javax.inject.Inject

@CurrencyFragmentScope
class BalancesAdapter @Inject constructor() : ListAdapter<Balance, BalancesAdapter.MyViewHolder>(
    asyncDiffer
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            BalancesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: BalancesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.balance = item
        when (position) {
            1 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#b9b9b9"))
            2 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#4a90e4"))
            3 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FF0000")) // was too tired so hardcoded :(
            4 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#26263f"))
            5 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#164381"))
            6 -> holder.binding.balanceCardView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#f47521"))
        }
    }
}

val callback = object : DiffUtil.ItemCallback<Balance>() {
    override fun areItemsTheSame(oldItem: Balance, newItem: Balance): Boolean =
        oldItem.type == newItem.type

    override fun areContentsTheSame(oldItem: Balance, newItem: Balance): Boolean =
        oldItem == newItem

}

val asyncDiffer = AsyncDifferConfig.Builder<Balance>(callback).build()