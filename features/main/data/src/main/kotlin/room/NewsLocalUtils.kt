package room

import NewsItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ktor.toImageBitmap
import toLocalDate
import toTimestamp

@Entity(tableName = DatabaseNames.NEWS)
internal data class NewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val desc: String,
    @ColumnInfo(name = "image_byte_array") val imageByteArray: ByteArray?,
    @ColumnInfo(name = "is_image_loading") val isImageLoading: Boolean,
    val source: String,
    val geo: String?,
    val date: Long,
    val insertInDBTimestamp: Long = Clock.System.now().toTimestamp()
) {
    fun mapToNewsItem() = NewsItem(
        title = title,
        desc = desc,
        imageBitmap = imageByteArray?.toImageBitmap(),
        source = source,
        geo = geo,
        isImageLoading = isImageLoading,
        date = date.toLocalDate(),
        id = id
    )
}