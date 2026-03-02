package com.example.testCompose.presentation.injection

import android.content.Context
import com.example.testCompose.data.security.KeyStoreManager
import com.example.testCompose.data.security.SecureStorage
import com.example.testCompose.data.security.SecureStorageCache
import com.example.testCompose.data.security.SecureStorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    // SecureStorage -> SecureStorageCache (which delegates to SecureStorageImpl)
    @Binds
    @Singleton
    abstract fun bindSecureStorage(implementation: SecureStorageCache): SecureStorage
    // KeyStoreManager -> @Inject constructor auto
    // SecureStorageImpl -> @Inject constructor auto
    // SecureStorageCache -> @Inject constructor auto
    // DatabasePassphraseProvider -> @Inject constructor auto
    // DatabaseProvider -> @Inject constructor auto
}