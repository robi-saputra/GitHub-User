package robi.technicaltest.networks.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import robi.technicaltest.networks.ApiServices
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.data.SearchData
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getUsers(): NetworkState<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUsers()
                if (response.isSuccessful) {
                    NetworkState.Success(response.body() ?: emptyList())
                } else {
                    NetworkState.Error(Exception("Failed to fetch users"))
                }
            } catch (e: Exception) {
                NetworkState.Error(e)
            }
        }
    }

    suspend fun getUserDetails(username: String): NetworkState<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserDetails(username)
                if (response.isSuccessful) {
                    NetworkState.Success(response.body()!!)
                } else {
                    NetworkState.Error(Exception("Failed to fetch user details"))
                }
            } catch (e: Exception) {
                NetworkState.Error(e)
            }
        }
    }

    suspend fun getUserRepositories(username: String): NetworkState<List<Repository>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserRepositories(username)
                if (response.isSuccessful) {
                    NetworkState.Success(response.body() ?: emptyList())
                } else {
                    NetworkState.Error(Exception("Failed to fetch repositories"))
                }
            } catch (e: Exception) {
                NetworkState.Error(e)
            }
        }
    }

    suspend fun getUserRepositoriesPages(username: String, page: Int): NetworkState<List<Repository>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserRepositoriesPages(username, page)
                if (response.isSuccessful) {
                    NetworkState.Success(response.body() ?: emptyList())
                } else {
                    NetworkState.Error(Exception("Failed to fetch repositories"))
                }
            } catch (e: Exception) {
                NetworkState.Error(e)
            }
        }
    }

    suspend fun searchUsers(query: String, page: Int): NetworkState<SearchData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchUsers(query, page)
                if (response.isSuccessful) {
                    NetworkState.Success(response.body()!!)
                } else {
                    NetworkState.Error(Exception("Failed to search users"))
                }
            } catch (e: Exception) {
                NetworkState.Error(e)
            }
        }
    }
}