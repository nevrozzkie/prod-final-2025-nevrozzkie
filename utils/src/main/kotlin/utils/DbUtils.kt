package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withDatabaseContext(block: suspend () -> T): T {
    return withIOContext(block)
}

suspend fun <T> withIOContext(block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        block()
    }
}
