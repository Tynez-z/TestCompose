package com.example.testCompose.data.security

import com.example.testCompose.data.security.model.SecureKey

interface SecureStorage {
    suspend fun put(key: SecureKey, value: String)
    suspend fun get(key: SecureKey): String?
    suspend fun remove(key: SecureKey)
    suspend fun clear()
    suspend fun has(key: SecureKey): Boolean
}