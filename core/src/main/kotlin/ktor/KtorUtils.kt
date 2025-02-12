package ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.util.reflect.typeInfo

// DefaultPost
suspend fun HttpClient.dPost(
    path: String,
    body: Any? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    return this.post {
        url {
            path(path)
            if (body != null) setBody(body)
            block()
        }
    }
}


// DefaultGet
suspend fun HttpClient.dGet(
    path: String,
    body: Any? = null,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = this.get {
    url {
        path(path)
        if (body != null) setBody(body)
        block()
    }
}

// DefaultBody
suspend inline fun <reified T> HttpResponse.dBody(): T {
    call.response.check()
    return call.bodyNullable(typeInfo<T>()) as T
}

// Check on Error -> Throw error
suspend fun HttpResponse.check(): Boolean {
    if (this.status != HttpStatusCode.OK) {
        throw Throwable("${this.status.value} ${this.call.request.url.encodedPath.removeSuffix("/").removePrefix("/")}\n${this.bodyAsText()}")
    }
    return true
}