package newYorkTimes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import decompose.NetworkStateManager
import decompose.isLoading
import decompose.isNotError
import kotlinx.serialization.serializerOrNull
import newYorkTimes.NewYorkTimesComponent.Output
import newYorkTimes.NewYorkTimesStore.Intent
import newYorkTimes.NewYorkTimesStore.Label
import newYorkTimes.NewYorkTimesStore.State
import newYorkTimes.NewYorkTimesStore.Message

class NewYorkTimesExecutor(
    private val webView: WebView,
    private val initialUrl: String,
    private val networkStateManager: NetworkStateManager
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun executeAction(action: Unit) {
        networkStateManager.nStartLoading()
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                val sslErrors = listOf(
                    ERROR_TIMEOUT,
                    ERROR_CONNECT,
                    ERROR_FAILED_SSL_HANDSHAKE
                )
                error?.let {
                    println("ERROxxR: ${error.errorCode} ${error.description}")
                    if (networkStateManager.networkModel.value.isLoading && error.errorCode !in sslErrors) {
                        networkStateManager.nError(
                            "Не удалось загрузить статью",
                            fixText = "Попробовать ещё раз"
                        ) {
                            networkStateManager.nStartLoading()
                            webView.loadUrl(webView.url ?: initialUrl)
                        }
                    }


                }
            }

            //start 2
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                setCssColors()
                jsStartAndObserve(
                    view,
                    textColor = state().textColor,
                    highlightedTextColor = state().highlightedTextColor,
                    backgroundColor = state().backgroundColor
                )
                jsSetPaddingForTopShadow(view)
                publish(Label.UpdateColorsFromUI)
                dispatch(Message.BoundedChanged(true))
                println("WEBPAGEERORR:${url}")
                // After catching error we get page error, but we don't want to show it! So check.
                if (networkStateManager.networkModel.value.isNotError) networkStateManager.nSuccess()
            }

            //start 1
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                dispatch(Message.BoundedChanged(false))
                // no flashbang!
                setCssColors()
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return !(request != null && (
                        request.url.toString()
                            .contains("/auth/") || request.url.toString()
                            .contains(initialUrl.split("www").getOrNull(1) ?: initialUrl))
                        )
            }
        }

        webView.loadUrl(initialUrl)
    }


    override fun executeIntent(intent: Intent) {
        when (intent) {
            // cuz it's (js in webView) not declarative ui =/
            is Intent.UpdateColors -> {
                jsUpdateJsColor(
                    webView,
                    backgroundColor = intent.backgroundColor,
                    textColor = intent.textColor,
                    highlightedTextColor = intent.highlightedTextColor
                )
                dispatch(
                    Message.ColorsUpdated(
                        backgroundColor = intent.backgroundColor,
                        textColor = intent.backgroundColor,
                        highlightedTextColor = intent.highlightedTextColor
                    )
                )
            }
        }
    }

    private fun setCssColors() {
        jsSetCSSColors(
            webView,
            backgroundColor = state().backgroundColor,
            onBackgroundColor = state().textColor
        )
    }
}
