package robi.technicaltest.baseapplication

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter <VB : ViewBinding, T>(
    var items: MutableList<T>
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VB>>() {
    val TAG = this::class.java.simpleName

    abstract fun bindViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    abstract fun onBind(holder: BaseViewHolder<VB>, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindViewBinding(inflater, parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        onBind(holder, items[position], position)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun initItems(newItems: MutableList<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: MutableList<T>) {
        items.addAll(newItems)
        Log.d(TAG, "items:: ${items.size} of ${getItemCount()-1}")
        notifyItemInserted(getItemCount()-1)
    }

    fun updateItemsInRange(newItems: MutableList<T>) {
        items.addAll(newItems)
        notifyItemRangeInserted(items.size - newItems.size, newItems.size)
    }

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}