package decompose

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value


sealed interface NetworkState {
    data object None : NetworkState
    data object Loading : NetworkState
    data object Error : NetworkState
}

class NetworkStateManager {
    private val _model = MutableValue(NetworkModel())

    val networkModel: Value<NetworkModel> = _model

    fun nSuccess() {
        _model.value = _model.value.copy(
            state = NetworkState.None,
            errorTitle = "",
            errorDesc = ""
        )
    }

    fun nStartLoading() {
        _model.value = _model.value.copy(state = NetworkState.Loading)
        println("STATE: ${networkModel.value}")
    }

    fun nError(
        errorTitle: String,
        errorDesc: String,
        onFixErrorClick: () -> Unit
    ) {
        _model.value = _model.value.copy(
            state = NetworkState.Error,
            errorTitle = errorTitle,
            errorDesc = errorDesc,
            onFixErrorClick = onFixErrorClick
        )
    }

    data class NetworkModel(
        val state: NetworkState = NetworkState.None,
        val errorTitle: String = "",
        val errorDesc: String = "",
        val onFixErrorClick: () -> Unit = {}
    )
}

val NetworkStateManager.NetworkModel.isLoading : Boolean
    get() = this.state == NetworkState.Loading
val NetworkStateManager.NetworkModel.isNotError : Boolean
    get() = this.state != NetworkState.Error
val NetworkStateManager.NetworkModel.isError : Boolean
    get() = this.state == NetworkState.Error