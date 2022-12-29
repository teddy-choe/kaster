package app.kaster.android

import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.kaster.common.KasterTheme
import app.kaster.common.domainlist.DomainListContent
import app.kaster.common.domainlist.DomainListInput
import app.kaster.common.domainlist.DomainListViewState
import app.kaster.common.domainlist.DomainListViewState.SearchState.ShowSearch
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test

class DomainListUiTest {

    private val inputMock = mockk<(DomainListInput) -> Unit>(relaxed = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectingDomainSendsEditEvent() {
        val domainFixture = "www.example.org"
        givenDomainList(domainFixture)

        composeTestRule.onNodeWithText(domainFixture).performClick()

        verify { inputMock(DomainListInput.EditDomain(domainFixture)) }
    }

    @Test
    fun logout() {
        givenDomainList()

        composeTestRule.onNodeWithContentDescription("Log out").performClick()

        verify { inputMock(DomainListInput.Logout) }
    }

    @Test
    fun startSearch() {
        givenDomainList()

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        verify { inputMock(DomainListInput.StartSearch) }
    }

    @Test
    fun stopSearch() {
        givenDomainListUiWithState(
            DomainListViewState(searchState = ShowSearch(""))
        )

        composeTestRule.onNodeWithContentDescription("Stop search").performClick()

        verify { inputMock(DomainListInput.StopSearch) }
    }

    @Test
    fun searchIsFocused() {
        givenDomainListUiWithState(
            DomainListViewState(searchState = ShowSearch(""))
        )

        composeTestRule.onNodeWithTag("search").assertIsFocused()
    }

    private fun givenDomainList(vararg domains: String) = givenDomainListUiWithState(
        DomainListViewState(domains.toList().toImmutableList())
    )

    private fun givenDomainListUiWithState(viewState: DomainListViewState) {
        composeTestRule.setContent {
            KasterTheme {
                DomainListContent(viewState, inputMock)
            }
        }
    }

}