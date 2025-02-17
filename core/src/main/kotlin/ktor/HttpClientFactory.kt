package ktor

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.HttpClientCall
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.android.AndroidClientEngine
import io.ktor.client.engine.android.AndroidEngineConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.content
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.maxAge
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendAll
import io.ktor.util.date.GMTDate
import io.ktor.util.filter
import io.ktor.util.toMap
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

class HttpClientFactory {
    fun createFactory(
        androidContext: Context,
        block: HttpClientConfig<AndroidEngineConfig>.() -> Unit = {},
        defaultRequestBlock: (DefaultRequest.DefaultRequestBuilder.() -> Unit)? = null
    ) =
        HttpClient(CIO) {
            install("forceCache") {
                receivePipeline.intercept(HttpReceivePipeline.Before) { response ->
                    this.proceedWith(object : HttpResponse() {
                        override val call: HttpClientCall = response.call
                        override val coroutineContext: CoroutineContext = response.coroutineContext
                        override val headers: Headers = HeadersBuilder().apply {
                            appendAll(response.headers)
                            this.remove(HttpHeaders.ETag)
                            this.remove("cf-cache-status")
                            this.remove(HttpHeaders.CacheControl)
                            val maxAge = response.call.request.attributes.getOrNull(CacheControlMaxAgeKey)
                            maxAge?.let {
                                this.append(HttpHeaders.CacheControl, "public, max-age=${maxAge}")
                            }

                        }.build()

                        @InternalAPI
                        override val rawContent: ByteReadChannel
                            get() = response.rawContent
                        override val requestTime: GMTDate = response.requestTime
                        override val responseTime: GMTDate = response.responseTime
                        override val status: HttpStatusCode = response.status
                        override val version: HttpProtocolVersion = response.version
                    })
                }
            }
            install(HttpCache) {
                val dir = File(androidContext.dataDir, "ktor-cache")
                publicStorage(FileStorage(dir))
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Any)
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 15000
                requestTimeoutMillis = 30000
            }
            defaultRequest {

                url {

                    protocol = URLProtocol.HTTPS
                }
                contentType(ContentType.Application.Json)
                port = 443
                defaultRequestBlock?.let { it() }
            }
        }


}

