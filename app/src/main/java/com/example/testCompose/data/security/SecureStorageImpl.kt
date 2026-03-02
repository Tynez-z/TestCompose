package com.example.testCompose.data.security

import android.content.Context
import android.os.Build
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.testCompose.data.security.model.SecureKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.crypto.AEADBadTagException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("secure_storage")

@Singleton
class SecureStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val keyStoreManager: KeyStoreManager
) : SecureStorage {
    // Every SecureKey gets its own IV + ciphertext pair
    private fun ivPrefKey(key: SecureKey) = stringPreferencesKey("${key.storeKey}_iv")
    private fun dataPrefixKey(key: SecureKey) = stringPreferencesKey("${key.storeKey}_data")

    override suspend fun put(key: SecureKey, value: String) {
        val encrypted = withContext(Dispatchers.Default) {
            keyStoreManager.encrypt(value)
        }
        context.dataStore.edit { prefs ->
            prefs[ivPrefKey(key)] = encrypted.iv
            prefs[dataPrefixKey(key)] = encrypted.cipherText
        }
    }

    override suspend fun get(key: SecureKey): String? = withContext(Dispatchers.Default) {
        val prefs = context.dataStore.data.first()
        val iv = prefs[ivPrefKey(key)] ?: return@withContext null
        val data = prefs[dataPrefixKey(key)] ?: return@withContext null
        try {
            keyStoreManager.decrypt(encryptedData = EncryptedData(iv = iv, cipherText = data))
        } catch (e: KeyPermanentlyInvalidatedException) {
            remove(key)
            null
        } catch (e: AEADBadTagException) {
            remove(key)
            null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun remove(key: SecureKey) {
        context.dataStore.edit { prefs ->
            prefs.remove(ivPrefKey(key))
            prefs.remove(dataPrefixKey(key))
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    override suspend fun has(key: SecureKey): Boolean =
        context.dataStore.data.first()[ivPrefKey(key)] != null
}