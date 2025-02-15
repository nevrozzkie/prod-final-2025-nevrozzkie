package utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RFetchExchangeRateResponse(
    @SerialName("conversion_rate") val conversionRate: Float
)
