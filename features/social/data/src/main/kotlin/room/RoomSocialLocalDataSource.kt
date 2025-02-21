package room

import ManagePostDTO
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import room.postFavourites.PostFavoritesDao
import room.postFavourites.PostFavoritesEntity
import room.postFavourites.encryption.EncryptionUtils
import room.postImages.PostImageEntity
import room.postImages.PostImagesDao
import room.posts.PostEntity
import room.posts.PostEntityWithImages
import room.posts.PostsDao
import toTimestamp
import withDatabaseContext

internal data object SocialDatabaseNames {
    data object Tables {
        const val POSTS = "posts_table"
        const val IMAGES = "post_images_table"
        const val FAVOURITES = "favourites_table"
    }

    const val SOCIAL_DB = "social_db"
}

class RoomSocialLocalDataSource(
    androidContext: Context
) {
    private var postsDao: PostsDao
    private var postImagesDao: PostImagesDao
    private var postFavouritesDao: PostFavoritesDao

    init {
        val db = Room.databaseBuilder(
            context = androidContext,
            klass = SocialDatabase::class.java,
            SocialDatabaseNames.SOCIAL_DB
        ).fallbackToDestructiveMigration().build()
        postsDao = db.postsDao()
        postImagesDao = db.postImageDao()
        postFavouritesDao = db.postFavouritesDao()
    }

    internal suspend fun insertFavouritePost(id: Long) {
        withDatabaseContext {
            val encryptedPostId = EncryptionUtils.encrypt(id.toString())
            val favoritePost = PostFavoritesEntity(postId = encryptedPostId)
            postFavouritesDao.insert(favoritePost)
        }
    }

    fun fetchPostsFavourites() = postFavouritesDao.fetchFavourites()

    internal suspend fun deleteFromFavourites(id: Long) {
        withDatabaseContext {
            val encryptedPostId = EncryptionUtils.encrypt(id.toString())
            postFavouritesDao.deleteFromFavorites(encryptedPostId)
        }
    }


    internal suspend fun insertManagePostDTO(dto: ManagePostDTO) {
        withDatabaseContext {
            val id = postsDao.insertPost(
                PostEntity(
                    text = dto.text,
                    tags = dto.tags,
                    creationTimestamp = Clock.System.now().toTimestamp(),
                    editTimestamp = null,
                    newsId = dto.newsData?.id,
                    newsTitle = dto.newsData?.title,
                    newsIcon = dto.newsData?.icon,
                    newsUrl = dto.newsData?.url
                )
            )
            postImagesDao.insertAll(
                dto.images.map { PostImageEntity(postId = id, imageData = it) })
        }
    }

    internal fun fetchPostsEntities(): Flow<List<PostEntityWithImages>> = postsDao.fetchPostsWithImages()
}
