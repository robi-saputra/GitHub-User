package robi.technicaltest.githubuser.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import robi.technicaltest.baseapplication.BaseFragment
import robi.technicaltest.githubuser.MainActivity
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.FragmentSearchBinding
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User

class SearchFragment: BaseFragment<FragmentSearchBinding>() {
    private var query: String? = null
    private lateinit var adapter: Adapter
    private val viewModel: SearchViewModel by activityViewModels()
    private var page = 1
    private var isNext = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        query = requireArguments().getString("Query")
        return FragmentSearchBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewModel()
        adapter = Adapter(mutableListOf())
        adapter.callback = object : Adapter.AdapterCallback{
            override fun onItemClick(user: User) {
                val bundle = Bundle().apply {
                    putParcelable("User", user)
                }
                findNavController().navigate(R.id.detailUserFragment, bundle)
            }
        }
        binding.rvListUsers.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            adapter.initItems(mutableListOf())
            viewModel.searchUsers(query!!, 1)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.nestedScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            val bottomReached = !binding.nestedScrollView.canScrollVertically(1) // Check if at the bottom
            if (bottomReached) {
                binding.footerShimmerLayout.visibility = View.VISIBLE
                binding.footerShimmerLayout.startShimmer()
                Log.d(TAG, "Bottom Data")
                loadNextPage()
            }
        }

        viewModel.searchUsers(query!!, page)
    }

    private fun initToolbar() {
        (activity as? MainActivity)?.setTitle(query!!)
        (activity as? MainActivity)?.toggleSearchBox(false)
        setHasOptionsMenu(true)
    }

    @Deprecated("onPrepareOptionsMenu")
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search_bar)
        searchItem?.isVisible = true
    }

    private fun initViewModel() {
        viewModel.usersState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {
                    if (isNext) {
                        binding.rvListUsers.visibility = View.VISIBLE
                        binding.shimmerLayout.visibility = View.GONE
                        binding.footerShimmerLayout.visibility = View.VISIBLE
                        binding.footerShimmerLayout.startShimmer()
                    } else {
                        binding.footerShimmerLayout.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        binding.rvListUsers.visibility = View.GONE
                    }
                    binding.exceptionLayout.visibility = View.GONE
                }
                is NetworkState.Success -> {
                    binding.rvListUsers.visibility = View.VISIBLE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.footerShimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.footerShimmerLayout.stopShimmer()
                    binding.shimmerLayout.stopShimmer()
                    val users = mutableListOf<User>()
                    users.addAll(state.data)
                    Log.d(TAG, "Success")
                    Log.d(TAG, users.toString())
                    if (isNext) {
                        isNext = false
                        adapter.updateItemsInRange(users)
                    } else {
                        adapter.initItems(users)
                    }
                }
                is NetworkState.Error -> {
                    val error = state.exception
                    isNext = false
                    binding.rvListUsers.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.footerShimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    binding.footerShimmerLayout.stopShimmer()
                    binding.tvTittle.text = "Upps!!"
                    binding.tvDescription.text = "${error.message}"
                    Log.e(TAG, "Error: ${ error.message }", error)
                }
            }
        }
    }

    private fun loadNextPage() {
        page++
        isNext = true
        viewModel.searchUsers(query!!, page)
    }
}