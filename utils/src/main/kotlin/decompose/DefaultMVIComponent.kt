package decompose

import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow

interface DefaultMVIComponent<I : Any, S : Any, L : Any> {
    val store: Store<I, S, L>
    val model: Value<S>
        get() = store.asValue()
    val labels: Flow<L>
        get() = store.labels
    fun onEvent(event: I) {
        store.accept(event)
    }
}

