package room.postFavourites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostFavoritesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoritePost: PostFavoritesEntity)

    @Query("DELETE FROM favorites_table WHERE post_id = :encryptedPostId")
    suspend fun deleteFromFavorites(encryptedPostId: String)

    @Query("SELECT * FROM favorites_table")
    fun fetchFavourites(): Flow<List<PostFavoritesEntity>>
}