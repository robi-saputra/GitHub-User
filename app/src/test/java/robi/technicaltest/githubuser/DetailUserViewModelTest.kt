package robi.technicaltest.githubuser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import robi.technicaltest.githubuser.ui.detailuser.DetailUserViewModel
import robi.technicaltest.networks.NetworkState
import robi.technicaltest.networks.data.License
import robi.technicaltest.networks.data.Owner
import robi.technicaltest.networks.data.Repository
import robi.technicaltest.networks.usecase.GetUserRepositoriesUseCase

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailUserViewModelTest {
    // Required for LiveData Testing
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use TestCoroutineDispatcher for coroutine testing
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private val useCase: GetUserRepositoriesUseCase = mock()
    private lateinit var viewModel: DetailUserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for testing
        viewModel = DetailUserViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher after test
    }

    @Test
    fun `getRepositories should emit Success when useCase returns data`() = runTest {
        // Arrange
        val mockRepositories = listOf(
            Repository(
                allow_forking = true,
                archive_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/{archive_format}{/ref}",
                archived = false,
                assignees_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/assignees{/user}",
                blobs_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/git/blobs{/sha}",
                branches_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/branches{/branch}",
                clone_url = "https://github.com/defunkt/ruby-ace-editor.git",
                collaborators_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/collaborators{/collaborator}",
                comments_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/comments{/number}",
                commits_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/commits{/sha}",
                compare_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/compare/{base}...{head}",
                contents_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/contents/{+path}",
                contributors_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/contributors",
                created_at = "2012-12-06T00:09:33Z",
                default_branch = "master",
                deployments_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/deployments",
                description = "Ace Cloud9 Editor JS assets for Sprockets/Rails",
                disabled = false,
                downloads_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/downloads",
                events_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/events",
                fork = true,
                forks = 1,
                forks_count = 1,
                forks_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/forks",
                full_name = "defunkt/ruby-ace-editor",
                git_commits_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/git/commits{/sha}",
                git_refs_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/git/refs{/sha}",
                git_tags_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/git/tags{/sha}",
                git_url = "git://github.com/defunkt/ruby-ace-editor.git",
                has_discussions = false,
                has_downloads = true,
                has_issues = false,
                has_pages = false,
                has_projects = true,
                has_wiki = false,
                homepage = null,
                hooks_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/hooks",
                html_url = "https://github.com/defunkt/ruby-ace-editor",
                id = 7026998,
                is_template = false,
                issue_comment_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/issues/comments{/number}",
                issue_events_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/issues/events{/number}",
                issues_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/issues{/number}",
                keys_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/keys{/key_id}",
                labels_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/labels{/name}",
                language = "JavaScript",
                languages_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/languages",
                license = License(
                    key = "other",
                    name = "Other",
                    spdx_id = "NOASSERTION",
                    url = null,
                    node_id = "MDc6TGljZW5zZTA="
                ),
                merges_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/merges",
                milestones_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/milestones{/number}",
                mirror_url = null,
                name = "ruby-ace-editor",
                node_id = "MDEwOlJlcG9zaXRvcnk3MDI2OTk4",
                notifications_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/notifications{?since,all,participating}",
                open_issues = 0,
                open_issues_count = 0,
                owner = Owner(
                    login = "defunkt",
                    id = 2,
                    node_id = "MDQ6VXNlcjI=",
                    avatar_url = "https://avatars.githubusercontent.com/u/2?v=4",
                    gravatar_id = "",
                    url = "https://api.github.com/users/defunkt",
                    html_url = "https://github.com/defunkt",
                    followers_url = "https://api.github.com/users/defunkt/followers",
                    following_url = "https://api.github.com/users/defunkt/following{/other_user}",
                    gists_url = "https://api.github.com/users/defunkt/gists{/gist_id}",
                    starred_url = "https://api.github.com/users/defunkt/starred{/owner}{/repo}",
                    subscriptions_url = "https://api.github.com/users/defunkt/subscriptions",
                    organizations_url = "https://api.github.com/users/defunkt/orgs",
                    repos_url = "https://api.github.com/users/defunkt/repos",
                    events_url = "https://api.github.com/users/defunkt/events{/privacy}",
                    received_events_url = "https://api.github.com/users/defunkt/received_events",
                    type = "User",
                    user_view_type = "public",
                    site_admin = false
                ),
                `private` = false,
                pulls_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/pulls{/number}",
                pushed_at = "2012-08-29T23:38:24Z",
                releases_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/releases{/id}",
                size = 980,
                ssh_url = "git@github.com:defunkt/ruby-ace-editor.git",
                stargazers_count = 3,
                stargazers_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/stargazers",
                statuses_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/statuses/{sha}",
                subscribers_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/subscribers",
                subscription_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/subscription",
                svn_url = "https://github.com/defunkt/ruby-ace-editor",
                tags_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/tags",
                teams_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/teams",
                topics = listOf(),
                trees_url = "https://api.github.com/repos/defunkt/ruby-ace-editor/git/trees{/sha}",
                updated_at = "2021-01-13T19:36:17Z",
                url = "https://api.github.com/repos/defunkt/ruby-ace-editor",
                visibility = "public",
                watchers = 3,
                watchers_count = 3,
                web_commit_signoff_required = false
            )
        )
        val successState = NetworkState.Success(mockRepositories)
        `when`(useCase.invoke("testUser")).thenReturn(flowOf(successState))

        val observer = mock<Observer<NetworkState<List<Repository>>>>()
        viewModel.repositoriesState.observeForever(observer)

        // Act
        viewModel.getRepositories("testUser")
        advanceUntilIdle() // Wait for coroutine execution

        // Assert
        verify(observer).onChanged(NetworkState.Success(mockRepositories))

        viewModel.repositoriesState.removeObserver(observer)
    }

    @Test
    fun `getRepositories should emit Error when useCase throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        val errorState = NetworkState.Error(exception)
        `when`(useCase.invoke("testUser")).thenReturn(flowOf(errorState))

        val observer = mock<Observer<NetworkState<List<Repository>>>>()
        viewModel.repositoriesState.observeForever(observer)

        // Act
        viewModel.getRepositories("testUser")
        advanceUntilIdle()

        // Assert
        verify(observer).onChanged(errorState)

        viewModel.repositoriesState.removeObserver(observer)
    }
}