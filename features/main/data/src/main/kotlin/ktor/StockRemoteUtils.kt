package ktor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RFetchTickerPriceResponse(
    @SerialName("c") val price: Float,
    @SerialName("dp") val percentageDelta: Float
)

@Serializable
internal data class RFetchCompanyInfoResponse(
    val currency: String,
    val logo: String,
    @SerialName("ticker") val title: String
)