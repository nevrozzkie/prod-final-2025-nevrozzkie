package main

import com.arkivanov.mvikotlin.core.store.Store
import main.MainStore.Intent
import main.MainStore.Label
import main.MainStore.State

interface MainStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

}
