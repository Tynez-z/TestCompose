package com.example.testCompose.data.security.model

enum class SecureKey(val storeKey: String) {
    API_KEY(storeKey = "api_key"),
    DB_PASSPHRASE(storeKey = "db_passphrase"),
    ACCESS_TOKEN(storeKey = "access_token"),
    REFRESH_TOKEN(storeKey = "refresh_token")
}