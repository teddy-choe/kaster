package app.passwordkaster.android

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.common.login.LoginContent
import app.passwordkaster.logic.login.LoginInput
import app.passwordkaster.logic.login.LoginViewState
import dev.mokkery.MockMode.autofill
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.not
import org.junit.Rule
import org.junit.Test

class LoginUiTest {

    private val inputMock = mock<(LoginInput) -> Unit>(autofill)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginInputReceivedWhenLoginEnabled() {
        givenLoginUiWithState(
            LoginViewState(
                loginEnabled = true
            )
        )

        composeTestRule.onNodeWithTag("login").performClick()

        verify { inputMock(LoginInput.Login) }
    }

    @Test
    fun noInputWhenLoginDisabled() {
        givenLoginUiWithState(
            LoginViewState(
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithTag("login").performClick()

        verify(not) { inputMock(LoginInput.Login) }
    }

    @Test
    fun usernameAndPasswordAreCorrectlyDisplayedWhenUnmasked() {
        val expectedUsername = "someUser"
        val expectedPassword = "12345"

        givenLoginUiWithState(
            LoginViewState(
                username = expectedUsername,
                password = expectedPassword,
                passwordMasked = false,
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithTag("username").assertTextContains(expectedUsername)
        composeTestRule.onNodeWithTag("password").assertTextContains(expectedPassword)
    }

    @Test
    fun passwordIsMasked() {
        val password = "12345"

        givenLoginUiWithState(
            LoginViewState(
                username = "username",
                password = password,
                passwordMasked = true,
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithTag("password").assert(hasTextExactly(password).not())
    }

    @Test
    fun unmaskInputReceivedWhenPasswordIsMasked() {
        givenLoginUiWithState(
            LoginViewState(
                passwordMasked = true
            )
        )

        composeTestRule.onNodeWithContentDescription("Hide password").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Show password").performClick()

        verify { inputMock(LoginInput.UnmaskPassword) }
    }

    @Test
    fun maskInputReceivedWhenPasswordIsUnmasked() {
        givenLoginUiWithState(
            LoginViewState(
                passwordMasked = false
            )
        )

        composeTestRule.onNodeWithContentDescription("Show password").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Hide password").performClick()

        verify { inputMock(LoginInput.MaskPassword) }
    }

    @Test
    fun biometricLoginIsShownWhenEnabled() {
        givenLoginUiWithState(
            LoginViewState(
                biometricLoginEnabled = true
            )
        )

        composeTestRule.onNodeWithTag("loginWithBiometrics").assertIsDisplayed()
    }

    @Test
    fun biometricLoginIsHiddenWhenDisabled() {
        givenLoginUiWithState(
            LoginViewState(
                biometricLoginEnabled = false
            )
        )

        composeTestRule.onNodeWithTag("loginWithBiometrics").assertDoesNotExist()
    }

    @Test
    fun showOssLicensesInputReceived() {
        givenLoginUiWithState(LoginViewState())

        composeTestRule.onNodeWithTag("OpenSourceLicenses").performClick()

        verify { inputMock(LoginInput.ShowOSSLicenses) }
    }

    private fun givenLoginUiWithState(viewState: LoginViewState) {
        composeTestRule.setContent {
            KasterTheme {
                LoginContent(viewState, inputMock)
            }
        }
    }

}