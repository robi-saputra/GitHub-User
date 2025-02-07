package robi.technicaltest.githubuser.ui.listusers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import robi.technicaltest.baseapplication.BaseRecyclerViewAdapter
import robi.technicaltest.baseapplication.utils.formatNumber
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.ItemUserBinding
import robi.technicaltest.networks.data.User

class Adapter(items: MutableList<User>): BaseRecyclerViewAdapter<ItemUserBinding, User>(items) {
    var callback: AdapterCallback? = null

    interface AdapterCallback {
        fun onItemClick(user: User)
    }

    override fun bindViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemUserBinding {
        return ItemUserBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(holder: BaseViewHolder<ItemUserBinding>, item: User, position: Int) {
        val context = holder.itemView.context
        Glide.with(holder.itemView.context).load(item.avatar_url).into(holder.binding.ivAvatar)
        val name = "${item.name} ${item.login}"
        if (!item.name.isNullOrBlank()) holder.binding.tvAccountName.text = item.name else holder.binding.tvAccountName.visibility = View.GONE
        if (!item.login.isNullOrBlank()) holder.binding.tvUsername.text = item.login else holder.binding.tvUsername.visibility = View.GONE

        val iconLocation = ContextCompat.getDrawable(context, R.drawable.ic_location)
        val iconFollower = ContextCompat.getDrawable(context, R.drawable.ic_follower)
        val iconRepository = ContextCompat.getDrawable(context, R.drawable.ic_repository)

        if (!item.location.isNullOrBlank()) {
            val location = TextView(context)
            location.text = item.location ?: ""
            location.setPadding(8, 8, 8, 8)
            location.setCompoundDrawablesWithIntrinsicBounds(iconLocation, null, null, null)
            location.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(location)
        }

        if (item.followers>=0) {
            val followers = TextView(context)
            followers.text = "${item.followers.formatNumber()} followers"
            followers.setPadding(8, 8, 8, 8)
            followers.setCompoundDrawablesWithIntrinsicBounds(iconFollower, null, null, null)
            followers.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(followers)
        }

        if (item.following>=0) {
            val following = TextView(context)
            following.text = "• ${item.following.formatNumber()} following"
            following.setPadding(8, 8, 8, 8)
            holder.binding.flexboxLayout.addView(following)
        }

        if (item.following>0) {
            val repo = TextView(context)
            repo.text = "${item.following.formatNumber()} public repos"
            repo.setPadding(8, 8, 8, 8)
            repo.setCompoundDrawablesWithIntrinsicBounds(iconRepository, null, null, null)
            repo.compoundDrawablePadding = 16
            holder.binding.flexboxLayout.addView(repo)
        }

        holder.binding.root.setOnClickListener {
            callback?.onItemClick(item)
        }
    }
}