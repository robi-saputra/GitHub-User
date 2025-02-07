package robi.technicaltest.networks.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.SearchData
import robi.technicaltest.networks.repository.GitHubRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(private val repository: GitHubRepository) {
    operator fun invoke(query: String, page: Int): Flow<NetworkState<SearchData>> = flow {
        emit(NetworkState.Loading)
        emit(repository.searchUsers(query, page))
    }
}