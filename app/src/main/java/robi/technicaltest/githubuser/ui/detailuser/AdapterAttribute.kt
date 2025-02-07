package robi.technicaltest.githubuser.ui.detailuser

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import robi.technicaltest.baseapplication.BaseRecyclerViewAdapter
import robi.technicaltest.githubuser.databinding.ItemAttributeBinding

class AdapterAttribute(items: MutableList<Pair<Drawable, String>>): BaseRecyclerViewAdapter<ItemAttributeBinding, Pair<Drawable, String>>(items) {
    override fun bindViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemAttributeBinding {
        return ItemAttributeBinding.inflate(inflater, parent, false)
    }

    override fun onBind(holder: BaseViewHolder<ItemAttributeBinding>, item: Pair<Drawable, String>, position: Int) {
        val context = holder.itemView.context
        holder.binding.tvLabel.text = item.second
        Glide.with(context).load(item.first).into(holder.binding.ivIcon)
    }
}