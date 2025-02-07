package robi.technicaltest.githubuser.ui.listusers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import robi.technicaltest.baseapplication.BaseFragment
import robi.technicaltest.githubuser.R
import robi.technicaltest.githubuser.databinding.FragmentListUserBinding
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User

@AndroidEntryPoint
class ListUserFragment: BaseFragment<FragmentListUserBinding>() {
    private lateinit var adapter: Adapter
    private val viewModel: ListUserViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListUserBinding {
        return FragmentListUserBinding.inflate(inflater)
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
            viewModel.getUsers()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.getUsers()
    }

    private fun initToolbar() {
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
                    binding.rvListUsers.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }
                is NetworkState.Success -> {
                    binding.rvListUsers.visibility = View.VISIBLE
                    binding.exceptionLayout.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    val users = mutableListOf<User>()
                    users.addAll(state.data)
                    Log.d(TAG, "Success")
                    Log.d(TAG, users.toString())
                    adapter.initItems(users)
                }
                is NetworkState.Error -> {
                    val error = state.exception
                    binding.rvListUsers.visibility = View.GONE
                    binding.exceptionLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.shimmerLayout.stopShimmer()
                    binding.tvTittle.text = "Upps!!"
                    binding.tvDescription.text = "${error.message}"
                    Log.e(TAG, "Error: ${ error.message }", error)
                }
            }
        }
    }
}