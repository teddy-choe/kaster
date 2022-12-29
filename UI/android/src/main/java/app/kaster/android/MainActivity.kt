package app.kaster.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import app.kaster.common.RootInput
import app.kaster.common.RootViewModel

class MainActivity : AppCompatActivity() {

    private val loginPersistence by lazy { LoginPersistenceAndroid(applicationContext, lifecycleScope) }
    private val domainListPersistence by lazy { DomainEntryPersistenceAndroid(applicationContext, lifecycleScope) }
    private val rootViewModel by lazy { RootViewModel({ finish() }, loginPersistence) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            intent?.navToDomainEntry()
        }
        setContent {
            KasterAndroidUi(loginPersistence, domainListPersistence, rootViewModel)
            BackHandler { rootViewModel.onInput(RootInput.BackPressed) }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.navToDomainEntry()
    }

    private fun Intent.navToDomainEntry() {
        val host = getStringExtra(Intent.EXTRA_TEXT)?.toUri()?.simpleHost ?: return
        rootViewModel.onInput(RootInput.ShowDomainEntry(host))
    }

    private val Uri.simpleHost: String?
        get() = host?.removePrefix("www.")
}