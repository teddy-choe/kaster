package app.kaster.common.domainentry

import app.kaster.core.Kaster

data class DomainEntry(
    val domain: String = "",
    val scope: Kaster.Scope = Kaster.Scope.Authentication,
    val counter: Int = 1,
    val type: Kaster.PasswordType = Kaster.PasswordType.Maximum
)
