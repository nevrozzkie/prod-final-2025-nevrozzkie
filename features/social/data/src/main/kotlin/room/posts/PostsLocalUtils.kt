package room.posts

import Post
import PostNewsData
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import io.ktor.http.HttpMethod.Companion.Post
import kotlinx.datetime.format
import kotlinx.serialization.Serializable
import room.SocialDatabaseNames
import room.postImages.PostImageEntity
import rusFormat
import time24Format
import toLocalDate
import toLocalDateTime
import java.sql.Timestamp


@Entity(tableName = SocialDatabaseNames.Tables.POSTS)
internal data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val text: String,
    val tags: List<Int>,
    @ColumnInfo(name = "creation_timestamp") val creationTimestamp: Long,
    @ColumnInfo(name = "edit_timestamp") val editTimestamp: Long?,
    @ColumnInfo(name = "news_id") val newsId: String?,
    @ColumnInfo(name = "news_url") val newsUrl: String?,
    @ColumnInfo(name = "news_icon") val newsIcon: ByteArray?,
    @ColumnInfo(name = "news_title") val newsTitle: String?
)

internal data class PostEntityWithImages(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "post_id"
    )
    val images: List<PostImageEntity>
) {
    fun toPost() = run {
        val creationDateTime = post.creationTimestamp.toLocalDateTime()
        println("MEOWxx: ${post.creationTimestamp} ${post.editTimestamp}")
        Post(
            id = post.id,
            images = images.map { it.imageData },
            tags = post.tags,
            text = post.text,
            creationTime = creationDateTime.time.format(time24Format),
            edited = post.editTimestamp != null && post.creationTimestamp != post.editTimestamp,
            newsData = post.newsId?.let {
                PostNewsData(
                    id = post.newsId,
                    url = post.newsUrl!!,
                    icon = post.newsIcon,
                    title = post.newsTitle!!
                )
            },
            creationDate = creationDateTime.date.format(rusFormat)

        )
    }
}