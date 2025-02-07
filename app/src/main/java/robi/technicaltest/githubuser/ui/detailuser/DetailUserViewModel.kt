package robi.technicaltest.githubuser.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.usecase.GetUserRepositoriesUseCase
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(
    private val useCase: GetUserRepositoriesUseCase
): ViewModel() {
    val TAG = this::class.java.simpleName
    private val _repositoriesState = MutableLiveData<NetworkState<List<Repository>>>()
    val repositoriesState: LiveData<NetworkState<List<Repository>>> get() = _repositoriesState

    fun getRepositories(username: String) {
        viewModelScope.launch {
            try {
                useCase(username).collect { state ->
                    when (state) {
                        is NetworkState.Success -> {
                            _repositoriesState.value = NetworkState.Success(state.data)
                        }
                        is NetworkState.Error -> {
                            _repositoriesState.value = NetworkState.Error(state.exception)
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _repositoriesState.value = NetworkState.Error(e)
            }
        }
    }
}