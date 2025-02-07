package robi.technicaltest.networks.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User
import robi.technicaltest.networks.repository.GitHubRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repository: GitHubRepository) {
    operator fun invoke(): Flow<NetworkState<List<User>>> = flow {
        emit(NetworkState.Loading)
        emit(repository.getUsers())
    }
}