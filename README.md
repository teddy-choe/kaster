Password Kaster is a stateless password manager based on the concept of [Spectre App](https://spectre.app).

[Download on Play Store](https://play.google.com/store/apps/details?id=app.passwordkaster.android)

This project serves as practical real-world example to try out the latest technologies and development techniques and
to create an opinionated approach how modern UI development could look like.

### Featured techniques and technologies
* [TDD / BDD](https://dannorth.net/introducing-bdd/) on mobile & multiplatform with super fast [subcutaneous tests](https://www.ministryoftesting.com/articles/8745e4ec) ([Example](app/logic/src/commonTest/kotlin/app/passwordkaster/common))
* Non-flaky, isolated and fast UI integration tests to complement the subcutaneous tests ([Example](app/android/src/androidTest/kotlin/app/passwordkaster/android))
* Kotlin Multiplatform with [Compose Multiplatform](https://github.com/JetBrains/compose-jb) ([Example](app/ui/src/commonMain/kotlin/app/passwordkaster/common/domainlist/DomainListScreen.kt))
  * Android
  * Desktop
  * iOS
  * _coming later_ Web
* Clean [UDF](https://developer.android.com/jetpack/compose/architecture#udf) with Jetpack Compose ([Example](app/logic/src/commonMain/kotlin/app/passwordkaster/logic/domainlist/DomainListViewContract.kt))
  * View model output is pure state, no one-shot events
  * Single input for all view events
* [Showcasing](https://github.com/airbnb/Showkase) UI elements with different device configurations
* [Snapshot testing](https://github.com/cashapp/paparazzi) without Android emulator ([Example](app/ui/src/androidUnitTest/kotlin/app/passwordkaster/android/screenshottests/PreviewScreenshotTests.kt))
* Continuous Deployment pipeline into Play Store (internal track) ([Example](.github/workflows/main.yml))
* Automatic dependency updates with [Renovate](https://github.com/renovatebot/renovate)
