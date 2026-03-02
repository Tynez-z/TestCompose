package com.example.testCompose.data.security

import com.example.testCompose.data.security.model.SecureKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64

@Singleton
class DatabasePassphraseProvider @Inject constructor(private val secureStorage: SecureStorage) {
    companion object {
        private const val PASSPHRASE_LENGTH = 32
    }

    private val mutex = Mutex()

    suspend fun getOrCreatePassphrase(): ByteArray = mutex.withLock {
        val existing = secureStorage.get(SecureKey.DB_PASSPHRASE)

        if (existing != null) {
            return Base64.decode(existing, Base64.NO_WRAP)
        }

        // generate random passphrase
        val passphrase = ByteArray(PASSPHRASE_LENGTH).also {
            SecureRandom().nextBytes(it)
        }

        // store passphrase in my SecureStorage (KeyStoreManager encrypts it internally)
        val encodedValue = Base64.encodeToString(passphrase, Base64.NO_WRAP)
        secureStorage.put(
            key = SecureKey.DB_PASSPHRASE,
            value = encodedValue
        )
        return passphrase
    }
}