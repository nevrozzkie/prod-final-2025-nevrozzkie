package utils

import android.graphics.Bitmap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Ticker(
    val title: String,
    val price: Float,
    val currency: String,
    val rubles: Float,
    val percentageDelta: Float,
    val logoBitmap: Bitmap?
)

@Serializable
data class RFetchTickerPriceResponse(
    @SerialName("c") val price: Float,
    @SerialName("dp") val percentageDelta: Float
)

@Serializable
data class RFetchCompanyInfoResponse(
    val currency: String,
    val logo: String,
    @SerialName("ticker") val title: String
)

