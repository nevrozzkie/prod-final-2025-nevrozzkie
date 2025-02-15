package decompose

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class DefaultCoroutineExecutor<in Intent : Any, Action : Any, State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext = Dispatchers.Main
) : CoroutineExecutor<Intent, Action, State, Message, Label>(mainContext) {
    fun onDispatch(message: Message) = dispatch(message)
}