package robi.technicaltest.githubuser.ui.detailuser

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import robi.technicaltest.baseapplication.BaseFragment
import robi.technicaltest.baseapplication.utils.formatNumber
import robi.technicaltest.githubuser.MainActivity
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.FragmentDetailUserBinding
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.data.User

@AndroidEntryPoint
class DetailUserFragment: BaseFragment<FragmentDetailUserBinding>() {
    private lateinit var adapterAttribute: AdapterAttribute
    private lateinit var adapterRepository: AdapterRepository
    private val viewModel: DetailUserViewModel by activityViewModels()
    private var user: User? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailUserBinding {
        user = requireArguments().getParcelable("User")
        return FragmentDetailUserBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewModel()
        adapterAttribute = AdapterAttribute(mutableListOf())
        adapterRepository = AdapterRepository(mutableListOf())
        binding.rvAttribute.adapter = adapterAttribute
        if (user!=null) {
            val attributes = mutableListOf<Pair<Drawable, String>>()
            if (user!!.followers>=0) {
                val following = if (user!!.following>=0) "${user!!.following.formatNumber()} following" else ""
                val followers = if (user!!.followers>=0) "${user!!.followers.formatNumber()} followers" else ""
                val encode = "$followers • $following"
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_follower)!!, encode))
            }
            if (!user!!.company.isNullOrBlank()) {
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_company)!!, user!!.company!!))
            }
            if (!user!!.location.isNullOrBlank()) {
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)!!, user!!.location!!))
            }
            if (!user!!.blog.isNullOrBlank()) {
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_link)!!, user!!.blog!!))
            }
            if (!user!!.twitter_username.isNullOrBlank()) {
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_x)!!, "@${user!!.twitter_username!!}"))
            }
            if (user!!.public_repos>=0) {
                attributes.add(Pair(ContextCompat.getDrawable(requireContext(), R.drawable.ic_repository)!!, "${user!!.public_repos.formatNumber()} public repos"))
            }

            adapterAttribute.initItems(attributes)
            binding.tvAccountName.text = user!!.name!!
            binding.tvUsername.text = user!!.login!!
            Glide.with(requireContext()).load(user!!.avatar_url!!).into(binding.ivAvatar)
            binding.rvRepository.adapter = adapterRepository
            binding.btnMoreRepositories.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("User", user)
                }
                findNavController().navigate(R.id.listRepositoryFragment, bundle)
            }
            viewModel.getRepositories(user!!.login!!)
        }
    }

    private fun initViewModel() {
        viewModel.repositoriesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {
                    Log.d(TAG, "Loading")
                    binding.rvRepository.visibility = View.GONE
                    binding.btnMoreRepositories.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }

                is NetworkState.Success -> {
                    binding.rvRepository.visibility = View.VISIBLE
                    binding.btnMoreRepositories.visibility = View.VISIBLE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    val repos = mutableListOf<Repository>()
                    repos.addAll(state.data.take(5))
                    Log.d(TAG, "Success")
                    Log.d(TAG, repos.toString())
                    adapterRepository.initItems(repos)
                }

                is NetworkState.Error -> {
                    val error = state.exception
                    binding.rvRepository.visibility = View.GONE
                    binding.btnMoreRepositories.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    binding.tvTittle.text = "Upps!!"
                    binding.tvDescription.text = "${error.message}"
                    Log.e(TAG, error.message, error)
                }
            }
        }
    }

    private fun initToolbar() {
        (activity as? MainActivity)?.setTitle(user!!.name!!)
        (activity as? MainActivity)?.toggleSearchBox(false)
        setHasOptionsMenu(true)
    }

    @Deprecated("onPrepareOptionsMenu")
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search_bar)
        searchItem?.isVisible = false
    }
}