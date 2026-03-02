package com.example.testCompose

import android.app.Application
import com.example.testCompose.data.security.SecureStorage
import com.example.testCompose.data.security.model.SecureKey
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {

    @Inject
    lateinit var secureStorage: SecureStorage
    
    override fun onCreate() {
        super.onCreate()
        initApiKey()
    }

    fun initApiKey() {
        val dispatcher = Dispatchers.IO
        CoroutineScope(dispatcher).launch {
            if (!secureStorage.has(SecureKey.API_KEY)) {
                secureStorage.put(key = SecureKey.API_KEY, value = BuildConfig.API_KEY)
            }
        }
    }
}