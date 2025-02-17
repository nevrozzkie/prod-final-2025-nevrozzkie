package room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 5)
internal abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}