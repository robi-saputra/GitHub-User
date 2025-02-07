package robi.technicaltest.githubuser.ui.listusers

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
import robi.technicaltest.networks.usecase.GetUsersUseCase
import javax.inject.Inject

@HiltViewModel
class ListUserViewModel @Inject constructor(
    private val userUseCase: GetUsersUseCase,
    private val userDetailUseCase: GetUserDetailsUseCase
): ViewModel() {
    val TAG = this::class.java.simpleName
    private val _usersState = MutableLiveData<NetworkState<List<User>>>()
    val usersState: LiveData<NetworkState<List<User>>> get() = _usersState

    fun getUsers() {
        viewModelScope.launch {
            _usersState.value = NetworkState.Loading

            try {
                userUseCase().collect { state ->
                    when (state) {
                        is NetworkState.Success -> {
                            Log.d(TAG, "Fetched Users: ${state.data.map { it.login }}")

                            val deferredUsers = state.data.map { user ->
                                async { getUserDetails(user.login!!) }
                            }

                            val detailedUsers = deferredUsers.awaitAll().filterNotNull()

                            Log.d(TAG, "Detailed Users: $detailedUsers")
                            _usersState.value = NetworkState.Success(detailedUsers)
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

    private suspend fun getUserDetails(username: String): User? {
        return try {
            userDetailUseCase(username)
                .first { it is NetworkState.Success || it is NetworkState.Error }
                .let { state ->
                    when (state) {
                        is NetworkState.Success -> {
                            Log.d(TAG, "User fetched successfully: ${state.data}")
                            state.data
                        }
                        is NetworkState.Error -> {
                            Log.e(TAG, "Error fetching user details: ${state.exception}")
                            null
                        }
                        else -> null
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in getUserDetails: ${e.message}", e)
            null
        }
    }
}