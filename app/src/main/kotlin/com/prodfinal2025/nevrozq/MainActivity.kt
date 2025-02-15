package com.prodfinal2025.nevrozq


import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.prodfinal2025.nevrozq.navigation.root.RootComponentImpl
import com.prodfinal2025.nevrozq.navigation.root.RootContent
import coreModule
import financeModule
import mainModule
import koin.Inject
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
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
                financeModule
            )
        }
        enableEdgeToEdge()
        val prefs: SharedPreferences = Inject.instance()
        val viewManager = getViewManager("Dark")

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
                    Box(Modifier.fillMaxSize()) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            RootContent(rootComponent)
                        }

                        Button(
                            modifier = Modifier.align(Alignment.CenterStart).safeContentPadding(),
                            onClick = {
                                viewManager.tint.value =
                                    if (viewManager.isDark.value)  ThemeTint.Light
                                    else ThemeTint.Dark
                            }
                        ) {
                            Text("Change Theme")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val viewManager = LocalViewManager.current
    val a: SharedPreferences = Inject.instance()
    Button(
        onClick = {
            viewManager.tint.value =
                if (viewManager.tint.value == ThemeTint.Dark) ThemeTint.Light
                else ThemeTint.Dark
            a.edit().putString("meowik", viewManager.tint.value.name).apply()
        },
        modifier = modifier
    ) {
        Text(viewManager.tint.value.name)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    IndividualProdTheme {
        Greeting("Android")
//    }
}