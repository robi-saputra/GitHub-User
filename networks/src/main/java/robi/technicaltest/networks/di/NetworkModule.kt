package robi.technicaltest.networks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import robi.codingchallenge.networks.BuildConfig
import robi.technicaltest.networks.ApiServices
import robi.technicaltest.networks.AuthInterceptor
import robi.technicaltest.networks.repository.GitHubRepository
import robi.technicaltest.networks.usecase.GetUserDetailsUseCase
import robi.technicaltest.networks.usecase.GetUserRepositoriesUseCase
import robi.technicaltest.networks.usecase.GetUsersUseCase
import robi.technicaltest.networks.usecase.SearchUsersUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkApi() : ApiServices {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(apiService: ApiServices): GitHubRepository {
        return GitHubRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(userRepository: GitHubRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserDetailsUseCase(userRepository: GitHubRepository): GetUserDetailsUseCase {
        return GetUserDetailsUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserRepositoriesUseCase(userRepository: GitHubRepository): GetUserRepositoriesUseCase {
        return GetUserRepositoriesUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideSearchUsersUseCase(userRepository: GitHubRepository): SearchUsersUseCase {
        return SearchUsersUseCase(userRepository)
    }
}