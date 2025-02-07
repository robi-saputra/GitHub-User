package robi.technicaltest.networks


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.data.SearchData
import robi.technicaltest.networks.data.User


interface ApiServices {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{username}")
    suspend fun getUserDetails(@Path("username") username: String): Response<User>

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<Repository>>

    @GET("users/{username}/repos")
    suspend fun getUserRepositoriesPages(@Path("username") username: String, @Query("page") page: Int): Response<List<Repository>>

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String, @Query("page") page: Int): Response<SearchData>
}