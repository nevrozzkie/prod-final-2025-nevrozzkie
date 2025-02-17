package ktor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RFetchExchangeRateResponse(
    @SerialName("conversion_rate") val conversionRate: Float
)