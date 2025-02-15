package utils

import android.graphics.Bitmap
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class NewsItem(
    val title: String,
    val desc: String,
    val imageBitmap: Bitmap?,
    val source: String,
    val geo: String?,
    val date: LocalDate
)



@Serializable
data class RFetchNewsResponse(
    @SerialName("results") val news: List<RNewsItem>
)

@Serializable
data class RNewsItem(
    @SerialName("geo_facet") val geo: List<String>,
//    val kicker: String,
//    val subsection: String,
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
