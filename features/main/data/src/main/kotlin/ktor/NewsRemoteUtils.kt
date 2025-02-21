package ktor

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import room.NewsEntity
import toTimestamp


@Serializable
internal data class RFetchNewsResponse(
    @SerialName("results")
    val news: List<RNewsItem>
)

@Serializable
internal data class RNewsItem(
    @SerialName("slug_name") val id: String,
    @SerialName("geo_facet") val geo: List<String>,
    val title: String,
    val url: String,
    @SerialName("abstract") val desc: String,
    val source: String,
    @SerialName("published_date") val date: Instant?,
    @SerialName("multimedia")  val media: List<RNewsMultimedia>
) {
    fun toEntity(id: String, imageByteArray: ByteArray?, isImageLoading: Boolean) = NewsEntity(
        id = id,
        title = title,
        desc = desc,
        imageByteArray = imageByteArray,
        source = source,
        geo = geo.minByOrNull { g -> g.length },
        date = date?.toTimestamp() ?: 0,
        isImageLoading = isImageLoading,
        imageUrl = media.maxByOrNull { it.height * it.width }?.url,
        url = url
    )
}


@Serializable
internal data class RNewsMultimedia(
    val url: String,
    val height: Int,
    val width: Int,
    val type: String,
)