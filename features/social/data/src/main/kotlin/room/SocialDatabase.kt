package room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import room.postFavourites.PostFavoritesDao
import room.postFavourites.PostFavoritesEntity
import room.postFavourites.encryption.EncryptionConverter
import room.postImages.PostImageEntity
import room.postImages.PostImagesDao
import room.posts.PostConverters
import room.posts.PostEntity
import room.posts.PostsDao

@TypeConverters(PostConverters::class, EncryptionConverter::class)
@Database(entities = [PostEntity::class, PostImageEntity::class, PostFavoritesEntity::class], version = 11)
internal abstract class SocialDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun postImageDao(): PostImagesDao
    abstract fun postFavouritesDao(): PostFavoritesDao
}