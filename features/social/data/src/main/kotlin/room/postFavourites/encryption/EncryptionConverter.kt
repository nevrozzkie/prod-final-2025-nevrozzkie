package room.postFavourites.encryption

import androidx.room.TypeConverter

class EncryptionConverter {
    @TypeConverter
    fun fromLong(value: Long): String {
        return EncryptionUtils.encrypt(value.toString())
    }

    @TypeConverter
    fun toLong(encryptedData: String): Long {
        return EncryptionUtils.decrypt(encryptedData).toLong()
    }
}