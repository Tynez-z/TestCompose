package com.example.testCompose.data.security

import com.example.testCompose.data.security.model.SecureKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorageCache @Inject constructor(private val storage: SecureStorageImpl) :
    SecureStorage by storage {

    // passphrase is excluded from in-memory cache - it's read once at startup and immediately erase
    // as a ByteArray, so caching Base64 string in a ConcurrentHashMap is unnecessary exposure
    private val NO_CACHE_KEYS = setOf(SecureKey.DB_PASSPHRASE)
    private val cache = ConcurrentHashMap<SecureKey, String>()
    private val mutex = Mutex()

    override suspend fun get(key: SecureKey): String? {
        if (key in NO_CACHE_KEYS) return storage.get(key)

        val cached = cache[key]
        if (cached != null) {
            return cached // found in RAM -> return
        }

        // not in RAM -> read from dataStore + decrypt -> save to RAM for next time
        // Re-check inside lock — prevents duplicate storage reads
        return mutex.withLock {
            cache[key]?.let { return@withLock it }

            storage.get(key)?.also { decryptedValue ->
                cache[key] = decryptedValue
            }
        }
    }

    override suspend fun put(key: SecureKey, value: String) {
        mutex.withLock {
            storage.put(key, value)
            if (key !in NO_CACHE_KEYS) cache[key] = value
        }

    }

    override suspend fun remove(key: SecureKey) {
        mutex.withLock {
            storage.remove(key)
            cache.remove(key)
        }

    }

    override suspend fun clear() {
        mutex.withLock {
            storage.clear()
            cache.clear()
        }
    }
}