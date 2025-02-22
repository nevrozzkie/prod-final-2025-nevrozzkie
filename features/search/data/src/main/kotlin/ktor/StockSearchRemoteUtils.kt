package ktor

import kotlinx.serialization.Serializable

@Serializable
internal data class RFindTickersResponse(
    val result: List<RFindTickersResponseItem>
)

@Serializable
internal data class RFindTickersResponseItem(
    val symbol: String
)