package room.posts

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostConverters {
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return Json.decodeFromString(value)
    }
}