package nl.bluevoid.githubexplorer

import io.mockk.coEvery
import io.mockk.mockk
import nl.bluevoid.githubexplorer.domain.model.Repository
import nl.bluevoid.githubexplorer.domain.model.Visibility
import nl.bluevoid.githubexplorer.domain.usecase.GetGithubDataUsecase
import nl.bluevoid.githubexplorer.presentation.ExplorerViewmodel
import nl.bluevoid.githubexplorer.presentation.UiState
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test as testFlow

class ViewmodelTest {

    // Mock Repository
    private val gitHubUsecase: GetGithubDataUsecase = mockk()

    @Test
    fun `when initialized then viewmodel should return overview`() {
        val vm = getViewModel()
        assertTrue(vm.uiState.value is UiState.Overview)
    }

    @Test
    fun `when initialized then viewmodel should retrieve overview data`() = runTest {

        coEvery { gitHubUsecase.invoke() } returns MutableStateFlow(TEST_ITEMS)
        val vm = getViewModel()

        // wait till non empty list of items is received
        vm.uiState.filter { it is UiState.Overview && it.items.isNotEmpty() }.testFlow {
            val value = awaitItem()
            val items = (value as UiState.Overview).items
            assertTrue(items == TEST_ITEMS)
        }
    }

    private fun getViewModel(): ExplorerViewmodel {
        return ExplorerViewmodel(gitHubUsecase)
    }

    companion object {

        private val REPOSITORY_1 = Repository(
            id = 1,
            name = "1",
            fullName = "One for real",
            description = "One for real",
            ownerAvatarUrl = "ownerAvatarUrlOne",
            visibility = Visibility.Public,
            repositoryLink = "https://github.com/one"
        )

        private val REPOSITORY_2 = Repository(
            id = 2,
            name = "2",
            fullName = "Two for real",
            description = "Two for real",
            ownerAvatarUrl = "ownerAvatarUrlTwo",
            visibility = Visibility.Private,
            repositoryLink = "https://github.com/two"
        )

        val TEST_ITEMS = listOf(REPOSITORY_1, REPOSITORY_2)

    }
}