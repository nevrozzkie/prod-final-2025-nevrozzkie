package ktor

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.save
import io.ktor.client.plugins.isSaved
import io.ktor.client.plugins.retry
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.append
import io.ktor.http.cacheControl
import io.ktor.http.cio.parseMultipart
import io.ktor.http.maxAge
import io.ktor.http.path
import io.ktor.util.AttributeKey
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.InternalAPI

// DefaultPost
@OptIn(InternalAPI::class)
suspend fun HttpClient.dPost(
    path: String,
    body: Any? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val p = this.post {
        url {
            path(path)
            if (body != null) setBody(body)
            block()
        }
    }
    return p
}

val CacheControlMaxAgeKey = AttributeKey<Int>("CacheControlMaxAge")

// DefaultGet
suspend fun HttpClient.dGet(
    path: String,
    saveForSeconds: Int,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val response = this.get {
        if (saveForSeconds > 0) attributes.put(CacheControlMaxAgeKey, saveForSeconds)
        else header(HttpHeaders.CacheControl, "no-cache")
        url {
            path(path)
            block()
        }
    }
    return response
}
// DefaultGet
suspend fun HttpClient.dGet(
    url: Url,
    saveForSeconds: Int,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val response = this.get(url) {
        if (saveForSeconds > 0) attributes.put(CacheControlMaxAgeKey, saveForSeconds)
        else header(HttpHeaders.CacheControl, "no-cache")
        url {
            block()
        }
    }
    return response
}


// DefaultBody
suspend inline fun <reified T> HttpResponse.dBody(): T {
    call.response.isOk()
    return call.bodyNullable(typeInfo<T>()) as T
}

// Check on Error -> Throw error
suspend fun HttpResponse.isOk(): Boolean = true