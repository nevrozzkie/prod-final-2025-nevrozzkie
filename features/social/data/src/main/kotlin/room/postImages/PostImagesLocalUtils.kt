package room.postImages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import room.RoomSocialLocalDataSource
import room.SocialDatabaseNames
import room.posts.PostEntity

@Entity(
    tableName = SocialDatabaseNames.Tables.IMAGES,
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["post_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class PostImageEntity(
    //Can't name it id cuz room ne razlichaet, kogda 2 id...
    @PrimaryKey(autoGenerate = true) @ColumnInfo("image_id") val imageId: Long = 0L,
    @ColumnInfo("post_id") val postId: Long,
    @ColumnInfo("image_data") val imageData: ByteArray
)
