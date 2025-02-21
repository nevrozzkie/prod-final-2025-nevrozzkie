package room.posts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PostsDao {
    @Insert
    suspend fun insertPost(postEntity: PostEntity): Long

    @Update
    suspend fun updatePost(postEntity: PostEntity)

    //    @Query("SELECT * FROM posts_table")
//    fun fetchPosts() : Flow<List<PostEntity>>
    @Transaction
    @Query(
        """
        SELECT * FROM posts_table
        LEFT JOIN post_images_table ON posts_table.id = post_images_table.post_id
        """
    )
    fun fetchPostsWithImages(): Flow<List<PostEntityWithImages>>
}