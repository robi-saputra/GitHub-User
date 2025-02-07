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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import robi.technicaltest.githubuser.ui.search.SearchViewModel
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.SearchData
import robi.technicaltest.networks.data.User
import robi.technicaltest.networks.usecase.GetUserDetailsUseCase
import robi.technicaltest.networks.usecase.SearchUsersUseCase

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchUseCase: SearchUsersUseCase

    @Mock
    private lateinit var userDetailUseCase: GetUserDetailsUseCase

    private lateinit var viewModel: SearchViewModel
    private lateinit var observer: Observer<NetworkState<List<User>>>
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        observer = mock()
        searchUseCase = mock()
        userDetailUseCase = mock()
        viewModel = SearchViewModel(searchUseCase, userDetailUseCase)
        viewModel.usersState.observeForever(observer)
        mockStatic(Log::class.java).use { logMock ->
            whenever(Log.d(any(), any())).thenReturn(0)
            whenever(Log.e(any(), any())).thenReturn(0)
            whenever(Log.e(any(), any(), any())).thenReturn(0)
        }
    }

    @Test
    fun `searchUsers emits loading and success states`() = runTest {
        // Arrange
        val query = "mojombo"
        val page = 1
        val users = listOf(User(
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
        ))
        val userDetails = users.map { it.copy(name = "Tom Preston-Werner") }
        val searchResult = SearchData(
            total_count = 49041,
            incomplete_results = false,
            items = users
        )

        whenever(searchUseCase(query, page)).thenReturn(flowOf(NetworkState.Success(searchResult)))
        whenever(userDetailUseCase("mojombo")).thenReturn(flowOf(NetworkState.Success(userDetails[0])))

        // Act
        viewModel.searchUsers(query, page)
        advanceUntilIdle() // Ensures all coroutines complete

        // Assert
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(NetworkState.Loading) // First, Loading
        inOrder.verify(observer).onChanged(NetworkState.Success(userDetails)) // Then, Success
    }

    @Test
    fun `searchUsers handles empty search results`() = runTest {
        // Arrange
        val query = "unknown"
        val page = 1
        val searchResult = SearchData(
            total_count = 49041,
            incomplete_results = false,
            items = emptyList()
        )

        whenever(searchUseCase(query, page)).thenReturn(flowOf(NetworkState.Success(searchResult)))

        // Act
        viewModel.searchUsers(query, page)
        advanceUntilIdle()

        // Assert
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(NetworkState.Loading)
        inOrder.verify(observer).onChanged(NetworkState.Success(emptyList()))
    }

    @Test
    fun `searchUsers handles API error`() = runTest {
        // Arrange
        val query = "mojombo"
        val page = 1
        val exception = RuntimeException("API failure")

        whenever(searchUseCase(query, page)).thenReturn(flowOf(NetworkState.Error(exception)))

        // Act
        viewModel.searchUsers(query, page)
        advanceUntilIdle()

        // Assert
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(NetworkState.Loading)
        inOrder.verify(observer).onChanged(NetworkState.Error(exception))
    }

    @Test
    fun `getUserDetails returns user details`() = runTest {
        // Arrange
        val username = "mojombo"
        val user = User(
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
        )

        whenever(userDetailUseCase(username)).thenReturn(flowOf(NetworkState.Success(user)))

        // Act
        val result = viewModel.getUserDetails(username)

        // Assert
        assertNotNull(result)
        assertEquals(user, result)
    }

    @Test
    fun `getUserDetails handles errors`() = runTest {
        // Arrange
        val username = "mojombo"
        val exception = RuntimeException("User not found")

        whenever(userDetailUseCase(username)).thenReturn(flowOf(NetworkState.Error(exception)))

        // Act
        val result = viewModel.getUserDetails(username)

        // Assert
        assertNull(result)
    }

    @After
    fun tearDown() {
        viewModel.usersState.removeObserver(observer)
    }
}
