package room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import room.postImages.PostImageEntity
import room.postImages.PostImagesDao
import room.posts.PostConverters
import room.posts.PostEntity
import room.posts.PostEntityWithImages
import room.posts.PostsDao

@TypeConverters(PostConverters::class)
@Database(entities = [PostEntity::class, PostImageEntity::class], version = 9)
internal abstract class SocialDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun postImageDao(): PostImagesDao
}