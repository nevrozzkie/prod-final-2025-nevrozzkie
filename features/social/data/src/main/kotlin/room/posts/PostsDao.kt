package room.posts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import room.SocialDatabaseNames

@Dao
internal interface PostsDao {
    @Insert
    suspend fun insertPost(postEntity: PostEntity): Long

    @Query("""
        UPDATE ${SocialDatabaseNames.Tables.POSTS}
        SET 
            news_id = :newsId,
            news_url = :newsUrl,
            news_icon = :newsIcon,
            text = :text,
            tags = :tags,
            news_title = :newsTitle,
            edit_timestamp = :editTimestamp
        WHERE id = :id
    """)
    suspend fun updatePostFields(
        id: Long,
        newsId: String?,
        newsUrl: String?,
        newsIcon: ByteArray?,
        text: String,
        tags: List<Int>,
        newsTitle: String?,
        editTimestamp: Long
    )

    //    @Query("SELECT * FROM posts_table")
//    fun fetchPosts() : Flow<List<PostEntity>>
    @Transaction
    @Query("SELECT * FROM posts_table")
    fun fetchPostsWithImages(): Flow<List<PostEntityWithImages>>
}