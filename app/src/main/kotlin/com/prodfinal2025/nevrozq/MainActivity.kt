package com.prodfinal2025.nevrozq


import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.prodfinal2025.nevrozq.navigation.root.RootComponentImpl
import com.prodfinal2025.nevrozq.navigation.root.RootFlowScreen
import coreModule
import financeModule
import mainModule
import koin.Inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import searchModule
import socialModule
import themeSettings
import view.theme.AppTheme
import view.LocalViewManager
import view.ThemeTint
import view.getViewManager

class MainActivity : ComponentActivity() {
    override fun finish() {
        stopKoin()
        super.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(
                coreModule,
                mainModule,
                financeModule,
                socialModule,
                searchModule
            )
        }
        enableEdgeToEdge()
        val prefs: SharedPreferences = Inject.instance()
        val viewManager = getViewManager(prefs.getString(themeSettings, "Dark") ?: "Dark")

        val rootComponent = RootComponentImpl(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
            activity = this
        )

        setContent {
            CompositionLocalProvider(
                LocalViewManager provides viewManager
            ) {
                AppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RootFlowScreen(rootComponent)
                    }
                }
            }
        }
    }
}