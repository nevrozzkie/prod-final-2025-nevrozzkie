package room.postFavourites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class PostFavoritesEntity(
    @PrimaryKey @ColumnInfo("post_id") val postId: String
)