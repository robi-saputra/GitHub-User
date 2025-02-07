package robi.technicaltest.githubuser.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User
import robi.technicaltest.networks.usecase.GetUserDetailsUseCase
import robi.technicaltest.networks.usecase.SearchUsersUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUsersUseCase,
    private val userDetailUseCase: GetUserDetailsUseCase
): ViewModel() {
    val TAG = this::class.java.simpleName
    private val _usersState = MutableLiveData<NetworkState<List<User>>>()
    val usersState: LiveData<NetworkState<List<User>>> get() = _usersState

    fun searchUsers(query: String, page: Int) {
        viewModelScope.launch {
            _usersState.value = NetworkState.Loading

            try {
                searchUseCase(query, page).collect { state ->
                    when (state) {
                        is NetworkState.Success -> {
                            if (state.data.total_count > 0) {
                                val deferredUsers = state.data.items.map { user ->
                                    async { getUserDetails(user.login!!) }
                                }
                                val detailedUsers = deferredUsers.awaitAll().filterNotNull()
                                _usersState.value = NetworkState.Success(detailedUsers)
                            } else {
                                _usersState.value = NetworkState.Success(emptyList())
                            }
                        }
                        is NetworkState.Error -> {
                            _usersState.value = NetworkState.Error(state.exception)
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _usersState.value = NetworkState.Error(e)
            }
        }
    }

    suspend fun getUserDetails(username: String): User? {
        return try {
            userDetailUseCase(username)
                .first { it is NetworkState.Success || it is NetworkState.Error }
                .let { state ->
                    when (state) {
                        is NetworkState.Success -> {
                            state.data
                        }
                        is NetworkState.Error -> {
                            null
                        }
                        else -> null
                    }
                }
        } catch (e: Exception) {
            null
        }
    }
}