package robi.technicaltest.githubuser.ui.listrepository

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import robi.technicaltest.baseapplication.BaseFragment
import robi.technicaltest.githubuser.MainActivity
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.FragmentListRepositoryBinding
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.data.User

@AndroidEntryPoint
class ListRepositoryFragment: BaseFragment<FragmentListRepositoryBinding>() {
    private lateinit var adapter: Adapter
    private val viewModel: RepositoryViewModel by activityViewModels()
    private var user: User? = null
    private var page: Int = 1
    private var isNext = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListRepositoryBinding {
        user = requireArguments().getParcelable("User")
        return FragmentListRepositoryBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewModel()
        adapter = Adapter(mutableListOf())
        binding.rvRepository.adapter = adapter

        binding.nestedScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            val bottomReached = !binding.nestedScrollView.canScrollVertically(1) // Check if at the bottom
            if (bottomReached) {
                binding.footerShimmerLayout.visibility = View.VISIBLE
                binding.footerShimmerLayout.startShimmer()
                Log.d(TAG, "Bottom Data")
                loadNextPage()
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            adapter.initItems(mutableListOf())
            viewModel.getRepositories(user!!.login!!, 1)
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.getRepositories(user!!.login!!, page)
    }

    private fun initViewModel() {
        viewModel.repositoriesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {
                    Log.d(TAG, "Loading")
                    if (isNext) {
                        binding.rvRepository.visibility = View.VISIBLE
                        binding.shimmerLayout.visibility = View.GONE
                        binding.footerShimmerLayout.visibility = View.VISIBLE
                        binding.footerShimmerLayout.startShimmer()
                    } else {
                        binding.footerShimmerLayout.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.rvRepository.visibility = View.GONE
                    }
                    binding.exceptionLayout.visibility = View.GONE
                }

                is NetworkState.Success -> {
                    binding.rvRepository.visibility = View.VISIBLE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    binding.footerShimmerLayout.visibility = View.GONE
                    binding.footerShimmerLayout.stopShimmer()
                    val repos = mutableListOf<Repository>()
                    repos.addAll(state.data)
                    Log.d(TAG, "Success")
                    Log.d(TAG, repos.toString())
                    if (isNext) {
                        isNext = false
                        adapter.updateItemsInRange(repos)
                    } else {
                        adapter.initItems(repos)
                    }
                }

                is NetworkState.Error -> {
                    val error = state.exception
                    binding.rvRepository.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    binding.footerShimmerLayout.visibility = View.GONE
                    binding.footerShimmerLayout.stopShimmer()
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

    private fun loadNextPage() {
        page++
        isNext = true
        viewModel.getRepositories(user!!.login!!, page)
    }
}