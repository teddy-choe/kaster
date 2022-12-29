package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.domainlist.DomainListInput
import app.kaster.common.domainlist.DomainListInput.AddDomain
import app.kaster.common.domainlist.DomainListViewModel
import app.kaster.common.login.LoginPersistenceInMemory
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainListSpec {

    @Test
    fun `A logged in user sees the list of previously added domains`() = testHarness("d1", "d2", "d3") {
        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(domains)
        }
    }

    @Test
    fun `A logged in user can add new domain entries`() = testHarness {
        viewModel.onInput(AddDomain)

        verify { onEditDomainEntryMock(null) }
    }

    @Test
    fun `A logged in user can modify domain entries`() = testHarness {
        val domainFixture = "www.example.org"

        viewModel.onInput(DomainListInput.EditDomain(domainFixture))

        verify { onEditDomainEntryMock(domainFixture) }
    }

    @Test
    fun `Entries are sorted by domain name`() = testHarness("z", "a", "c", "B") {
        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(listOf("a", "B", "c", "z"))
        }
    }

    @Test
    fun `The list is filtered for matching domain names, when a search term is entered`() {
        TODO()
    }

    @Test
    fun `User can manually log out from domain list`() = testHarness {
        viewModel.onInput(DomainListInput.Logout)

        loginPersistence.credentials.value.shouldBeNull()
    }

    private class TestHarness(val domains: Set<String>) {
        val onEditDomainEntryMock = mockk<(String?) -> Unit> { every { this@mockk(any()) } just runs }
        val loginPersistence = LoginPersistenceInMemory("Bender", "BiteMyShinyMetalAss!")
        val domainEntryPersistence = DomainEntryPersistenceInMemory(domains.map { DomainEntry(it) }.toSet())
        val viewModel = DomainListViewModel(
            onEditDomainEntryMock,
            loginPersistence,
            domainEntryPersistence
        )
    }

    private fun testHarness(vararg domains: String, block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(domains.toSet()))
    }

}
