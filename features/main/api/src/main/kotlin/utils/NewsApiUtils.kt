package utils

import android.graphics.Bitmap
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter


data class NewsItem(
    val title: String,
    val desc: String,
    val imageBitmap: Bitmap?,
    val source: String,
    val section: String?,
    val date: LocalDate
)



@Serializable
data class RFetchNewsResponse(
    @SerialName("results") val news: List<RNewsItem>
)

@Serializable
data class RNewsItem(
    val section: String,
    val subsection: String,
    val title: String,
    @SerialName("abstract") val desc: String,
    val source: String,
    @SerialName("created_date") val date: Instant,
    @SerialName("multimedia")  val media: List<RNewsMultimedia>
)


@Serializable
data class RNewsMultimedia(
    val url: String,
    val height: Int,
    val width: Int,
    val type: String,
)
