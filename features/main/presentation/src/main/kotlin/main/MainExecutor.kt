package main

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State
import main.MainStore.Message

class MainExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            else -> TODO()
        }
    }
}
