package room.postImages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface PostImagesDao {
    @Insert
    suspend fun insertAll(images: List<PostImageEntity>)

    @Query("SELECT * FROM post_images_table WHERE post_id = :postId")
    suspend fun getImagesByPostId(postId: Long): List<PostImageEntity>

    @Query("DELETE FROM post_images_table WHERE post_id = :postId")
    suspend fun deleteImagesByPostId(postId: Long)
}