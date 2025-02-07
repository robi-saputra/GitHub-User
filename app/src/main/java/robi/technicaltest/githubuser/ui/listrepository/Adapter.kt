package robi.technicaltest.githubuser.ui.listrepository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import robi.technicaltest.baseapplication.BaseRecyclerViewAdapter
import robi.technicaltest.baseapplication.utils.formatNumber
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.ItemRepositoryBinding
import robi.technicaltest.networks.data.Repository

class Adapter(items: MutableList<Repository>): BaseRecyclerViewAdapter<ItemRepositoryBinding, Repository>(items) {
    var callback: AdapterCallback? = null

    interface AdapterCallback {
        fun onItemClick(repository: Repository)
    }

    override fun bindViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRepositoryBinding {
        return ItemRepositoryBinding.inflate(inflater, parent, false)
    }

    override fun onBind(holder: BaseViewHolder<ItemRepositoryBinding>, item: Repository, position: Int) {
        val context = holder.itemView.context
        holder.binding.tvRepositoryName.text = item.name
        holder.binding.tvLabel.text = if (item.private) "Private" else "Public"
        holder.binding.tvRepositoryDescription.text = item.description
        if (item.description.isNullOrBlank()) holder.binding.tvRepositoryDescription.visibility = View.GONE
        val iconLang = ContextCompat.getDrawable(context, R.drawable.ic_code)
        val iconStargazers = ContextCompat.getDrawable(context, R.drawable.ic_stargazer)
        val iconFork = ContextCompat.getDrawable(context, R.drawable.ic_fork)

        if (!item.language.isNullOrBlank()) {
            val language = TextView(context)
            language.text = item.language ?: ""
            language.setPadding(8, 8, 8, 8)
            language.setCompoundDrawablesWithIntrinsicBounds(iconLang, null, null, null)
            language.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(language)
        }
        if (item.stargazers_count>=0) {
            val stargazer = TextView(context)
            stargazer.text = item.stargazers_count.formatNumber()
            stargazer.setPadding(8, 8, 8, 8)
            stargazer.setCompoundDrawablesWithIntrinsicBounds(iconStargazers, null, null, null)
            stargazer.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(stargazer)
        }
        if (item.forks_count>=0) {
            val fork = TextView(context)
            fork.text = item.forks_count.formatNumber()
            fork.setPadding(8, 8, 8, 8)
            fork.setCompoundDrawablesWithIntrinsicBounds(iconFork, null, null, null)
            fork.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(fork)
        }

        holder.binding.root.setOnClickListener {
            callback?.onItemClick(item)
        }
    }
}