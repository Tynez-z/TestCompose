package com.example.testCompose.presentation.injection

import android.content.Context
import androidx.room.Room
import com.example.testCompose.data.local.dao.MoviesDao
import com.example.testCompose.data.local.database.MoviesDatabase
import com.example.testCompose.data.security.DatabasePassphraseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        passphraseProvider: DatabasePassphraseProvider
    ): MoviesDatabase {
        System.loadLibrary("sqlcipher")
        val passphrase = runBlocking {
            passphraseProvider.getOrCreatePassphrase()
        }
        val supportFactory = SupportOpenHelperFactory(passphrase)
        passphrase.fill(0) //erase from memory after Room takes it

        /**
         *  encrypted DB cannot see tables in app inspection - PROD
         */

        return Room
            .databaseBuilder(context, MoviesDatabase::class.java, "movie_database")
            .openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration(true)
            .build()
        /**
         *  decrypted can see tables in app inspection - DEBUG
         */
//       return Room.databaseBuilder(
//            context,
//            MoviesDatabase::class.java,
//            "movies_debug.db"
//        )
//            .fallbackToDestructiveMigration(true)
//            .build()

    }

    @Provides
    fun provideMoviesDao(database: MoviesDatabase): MoviesDao {
        return database.moviesDao
    }
}
