package robi.technicaltest.githubuser

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.whenever
import robi.technicaltest.githubuser.ui.listusers.ListUserViewModel
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.User
import robi.technicaltest.networks.usecase.GetUserDetailsUseCase
import robi.technicaltest.networks.usecase.GetUsersUseCase


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var userUseCase: GetUsersUseCase

    @Mock
    private lateinit var userDetailUseCase: GetUserDetailsUseCase

    @Mock
    private lateinit var observer: Observer<NetworkState<List<User>>>

    private lateinit var viewModel: ListUserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListUserViewModel(userUseCase, userDetailUseCase)
        viewModel.usersState.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.usersState.removeObserver(observer)
        Dispatchers.resetMain()
    }

    @Test
    fun `getUsers success`() = runTest {
        // Given
        val users = listOf(
            User(
                avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
                bio = null,
                blog = null,
                company = null,
                created_at = null,
                email = null,
                events_url = "https://api.github.com/users/mojombo/events{/privacy}",
                followers = 0,
                followers_url = "https://api.github.com/users/mojombo/followers",
                following = 0,
                following_url = "https://api.github.com/users/mojombo/following{/other_user}",
                gists_url = "https://api.github.com/users/mojombo/gists{/gist_id}",
                gravatar_id = "",
                hireable = null,
                html_url = "https://github.com/mojombo",
                id = 1,
                location = null,
                login = "mojombo",
                name = null,
                node_id = "MDQ6VXNlcjE=",
                organizations_url = "https://api.github.com/users/mojombo/orgs",
                public_gists = 0,
                public_repos = 0,
                received_events_url = "https://api.github.com/users/mojombo/received_events",
                repos_url = "https://api.github.com/users/mojombo/repos",
                site_admin = false,
                starred_url = "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
                subscriptions_url = "https://api.github.com/users/mojombo/subscriptions",
                twitter_username = null,
                type = "User",
                updated_at = null,
                url = "https://api.github.com/users/mojombo",
                user_view_type = "public"
            ),
            User(
                avatar_url = "https://avatars.githubusercontent.com/u/2?v=4",
                bio = null,
                blog = null,
                company = null,
                created_at = null,
                email = null,
                events_url = "https://api.github.com/users/defunkt/events{/privacy}",
                followers = 0,
                followers_url = "https://api.github.com/users/defunkt/followers",
                following = 0,
                following_url = "https://api.github.com/users/defunkt/following{/other_user}",
                gists_url = "https://api.github.com/users/defunkt/gists{/gist_id}",
                gravatar_id = "",
                hireable = null,
                html_url = "https://github.com/defunkt",
                id = 2,
                location = null,
                login = "defunkt",
                name = null,
                node_id = "MDQ6VXNlcjI=",
                organizations_url = "https://api.github.com/users/defunkt/orgs",
                public_gists = 0,
                public_repos = 0,
                received_events_url = "https://api.github.com/users/defunkt/received_events",
                repos_url = "https://api.github.com/users/defunkt/repos",
                site_admin = false,
                starred_url = "https://api.github.com/users/defunkt/starred{/owner}{/repo}",
                subscriptions_url = "https://api.github.com/users/defunkt/subscriptions",
                twitter_username = null,
                type = "User",
                updated_at = null,
                url = "https://api.github.com/users/defunkt",
                user_view_type = "public"
            )
        )

        whenever(userUseCase()).thenReturn(flowOf(NetworkState.Success(users)))
        whenever(userDetailUseCase("wiswis")).thenAnswer { invocation ->
            val username = invocation.arguments[0] as String
            flowOf(NetworkState.Success(User(
                avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
                bio = null,
                blog = null,
                company = null,
                created_at = null,
                email = null,
                events_url = "https://api.github.com/users/$username/events{/privacy}",
                followers = 0,
                followers_url = "https://api.github.com/users/$username/followers",
                following = 0,
                following_url = "https://api.github.com/users/$username/following{/other_user}",
                gists_url = "https://api.github.com/users/$username/gists{/gist_id}",
                gravatar_id = "",
                hireable = null,
                html_url = "https://github.com/$username",
                id = if (username == "mojombo") 1 else 2,
                location = null,
                login = username,
                name = "Detailed $username",
                node_id = "MDQ6VXNlcj${
                    if (username == "mojombo") "E" else "I"
                }",
                organizations_url = "https://api.github.com/users/$username/orgs",
                public_gists = 0,
                public_repos = 0,
                received_events_url = "https://api.github.com/users/$username/received_events",
                repos_url = "https://api.github.com/users/$username/repos",
                site_admin = false,
                starred_url = "https://api.github.com/users/$username/starred{/owner}{/repo}",
                subscriptions_url = "https://api.github.com/users/$username/subscriptions",
                twitter_username = null,
                type = "User",
                updated_at = null,
                url = "https://api.github.com/users/$username",
                user_view_type = "public"
            )))
        }

        // When
        viewModel.getUsers()
        advanceUntilIdle()

        // Then
        verify(observer).onChanged(NetworkState.Success(users))
    }

    @Test
    fun `getUsers error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(userUseCase()).thenReturn(flowOf(NetworkState.Error(exception)))

        // When
        viewModel.getUsers()
        advanceUntilIdle()

        // Then
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(NetworkState.Loading)
        inOrder.verify(observer).onChanged(NetworkState.Error(exception))
    }

    @Test
    fun `getUsers fetch details error`() = runTest {
        // Given
        val users = listOf(
            User(
                avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
                bio = null,
                blog = null,
                company = null,
                created_at = null,
                email = null,
                events_url = "https://api.github.com/users/mojombo/events{/privacy}",
                followers = 0,
                followers_url = "https://api.github.com/users/mojombo/followers",
                following = 0,
                following_url = "https://api.github.com/users/mojombo/following{/other_user}",
                gists_url = "https://api.github.com/users/mojombo/gists{/gist_id}",
                gravatar_id = "",
                hireable = null,
                html_url = "https://github.com/mojombo",
                id = 1,
                location = null,
                login = "mojombo",
                name = null,
                node_id = "MDQ6VXNlcjE=",
                organizations_url = "https://api.github.com/users/mojombo/orgs",
                public_gists = 0,
                public_repos = 0,
                received_events_url = "https://api.github.com/users/mojombo/received_events",
                repos_url = "https://api.github.com/users/mojombo/repos",
                site_admin = false,
                starred_url = "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
                subscriptions_url = "https://api.github.com/users/mojombo/subscriptions",
                twitter_username = null,
                type = "User",
                updated_at = null,
                url = "https://api.github.com/users/mojombo",
                user_view_type = "public"
            ),
            User(
                avatar_url = "https://avatars.githubusercontent.com/u/2?v=4",
                bio = null,
                blog = null,
                company = null,
                created_at = null,
                email = null,
                events_url = "https://api.github.com/users/defunkt/events{/privacy}",
                followers = 0,
                followers_url = "https://api.github.com/users/defunkt/followers",
                following = 0,
                following_url = "https://api.github.com/users/defunkt/following{/other_user}",
                gists_url = "https://api.github.com/users/defunkt/gists{/gist_id}",
                gravatar_id = "",
                hireable = null,
                html_url = "https://github.com/defunkt",
                id = 2,
                location = null,
                login = "defunkt",
                name = null,
                node_id = "MDQ6VXNlcjI=",
                organizations_url = "https://api.github.com/users/defunkt/orgs",
                public_gists = 0,
                public_repos = 0,
                received_events_url = "https://api.github.com/users/defunkt/received_events",
                repos_url = "https://api.github.com/users/defunkt/repos",
                site_admin = false,
                starred_url = "https://api.github.com/users/defunkt/starred{/owner}{/repo}",
                subscriptions_url = "https://api.github.com/users/defunkt/subscriptions",
                twitter_username = null,
                type = "User",
                updated_at = null,
                url = "https://api.github.com/users/defunkt",
                user_view_type = "public"
            )
        )
        val exception = RuntimeException("Detail fetch error")

        whenever(userUseCase()).thenReturn(flowOf(NetworkState.Success(users)))
        whenever(userDetailUseCase("test")).thenReturn(flowOf(NetworkState.Error(exception)))

        // When
        viewModel.getUsers()
        advanceUntilIdle()

        // Then
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(NetworkState.Loading)
        inOrder.verify(observer).onChanged(argThat {
            this is NetworkState.Success<*>
        }) // Empty list due to detail errors
    }
}

