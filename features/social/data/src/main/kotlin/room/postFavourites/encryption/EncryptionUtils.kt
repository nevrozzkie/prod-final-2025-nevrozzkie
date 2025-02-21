package room.postFavourites.encryption

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


// ...https://www.baeldung.com/kotlin/advanced-encryption-standard
object EncryptionUtils {
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val key: SecretKey
    private val iv: ByteArray

    init {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        key = keyGenerator.generateKey()

        iv = ByteArray(16)
        val secureRandom = java.security.SecureRandom()
        secureRandom.nextBytes(iv)
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedData: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData))
        return String(decryptedBytes)
    }
}