package room

import NewsItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import utils.bitmap
import utils.toLocalDate
import utils.toTimestamp

@Entity(tableName = MainDatabaseNames.Tables.NEWS)
data class NewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val desc: String,
    val url: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "image_byte_array") val imageByteArray: ByteArray?,
    @ColumnInfo(name = "is_image_loading") val isImageLoading: Boolean,
    val source: String,
    val geo: String?,
    val date: Long,
    val insertInDBTimestamp: Long = Clock.System.now().toTimestamp()
) {
    fun toNewsItem() = NewsItem(
        title = title,
        desc = desc,
        imageBitmap = imageByteArray?.bitmap,
        source = source,
        geo = geo,
        isImageLoading = isImageLoading,
        date = date.toLocalDate(),
        id = id,
        url = url
    )
}

internal fun Flow<List<NewsEntity>>.mapToNewsItems() = this.map { list ->
    list.map { it.toNewsItem() }
}