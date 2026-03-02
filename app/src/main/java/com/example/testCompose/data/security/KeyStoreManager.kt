package com.example.testCompose.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyStoreManager @Inject constructor() {
    private val alias = "encryptor"
    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .setKeySize(256)
                    .build()
            )
        }.generateKey()
    }

    private fun getKeyStore(): KeyStore {
        return KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = getKeyStore()
        val entry = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        return entry?.secretKey ?: createKey()
    }

    fun encrypt(plainText: String): EncryptedData {
        val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        }
        val cipherText = encryptCipher.doFinal(plainText.toByteArray())
        val encodeCipherText = Base64.encodeToString(cipherText, Base64.NO_WRAP)

        val iv = encryptCipher.iv
        val encodedIv = Base64.encodeToString(iv, Base64.NO_WRAP)
        return EncryptedData(iv = encodedIv, cipherText = encodeCipherText)
    }

    fun decrypt(encryptedData: EncryptedData): String {
        val iv = Base64.decode(encryptedData.iv, Base64.NO_WRAP)
        val cipherText = Base64.decode(encryptedData.cipherText, Base64.NO_WRAP)
        val decryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(128, iv),
            )
        }
        return String(decryptCipher.doFinal(cipherText))
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = "NoPadding"
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}

data class EncryptedData(val iv: String, val cipherText: String)