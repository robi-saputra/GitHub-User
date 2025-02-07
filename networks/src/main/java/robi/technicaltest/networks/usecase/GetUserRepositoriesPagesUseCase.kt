package robi.technicaltest.networks.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.repository.GitHubRepository
import javax.inject.Inject

class GetUserRepositoriesPagesUseCase  @Inject constructor(private val repository: GitHubRepository) {
    operator fun invoke(username: String, page: Int): Flow<NetworkState<List<Repository>>> = flow {
        emit(NetworkState.Loading)
        emit(repository.getUserRepositoriesPages(username, page))
    }
}