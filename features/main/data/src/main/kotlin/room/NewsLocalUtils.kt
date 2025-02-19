package room

import NewsItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import ktor.imageBitmap
import toLocalDate
import toTimestamp

@Entity(tableName = MainDatabaseNames.Tables.NEWS)
internal data class NewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val desc: String,
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
        imageBitmap = imageByteArray?.imageBitmap,
        source = source,
        geo = geo,
        isImageLoading = isImageLoading,
        date = date.toLocalDate(),
        id = id
    )
}

internal fun Flow<List<NewsEntity>>.mapToNewsItems() = this.map { list ->
    list.map { it.toNewsItem() }
}