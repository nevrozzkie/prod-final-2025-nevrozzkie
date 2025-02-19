package room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 10)
internal abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}