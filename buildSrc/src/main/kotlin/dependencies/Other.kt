package dependencies

object Other {

    /**
     *[Material Components Android]
     * https://mvnrepository.com/artifact/com.google.android.material/material
     */
    const val material = "com.google.android.material:material:${Versions.material}"

    /**
     * [Core kotlin extensions]
     * https://developer.android.com/kotlin/ktx
     */
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktxCore}"

    /**
     * [App compat]
     * Allows access to new APIs on older API versions of the platform (many using Material Design).
     * https://developer.android.com/jetpack/androidx/releases/appcompat
     */
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"

    /**
     * [Lifecycle ktx]
     * Lifecycle KTX defines a LifecycleScope for each Lifecycle object.
     * https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-runtime-ktx
     */
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    /**
     * [Kotlin multiplatform / multi-format reflectionless serialization]
     * https://github.com/Kotlin/kotlinx.serialization
     */
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"

    /**
     * [Coil Jetpack Compose]
     * An image loading library for Android backed by Kotlin Coroutines
     * https://mvnrepository.com/artifact/io.coil-kt/coil-compose
     */
    const val coil = "com.github.skydoves:landscapist-coil:${Versions.coil}"

    /**
     * [Paging ]
     * https://developer.android.com/jetpack/androidx/releases/paging
     */
    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"

    /**
     * [ExoPlayer ]
     * https://github.com/google/ExoPlayer/blob/release-v2/RELEASENOTES.md
     */
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    const val exoPlayerCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoPlayer}"
    const val exoPlayerHls = "com.google.android.exoplayer:exoplayer-hls:${Versions.exoPlayer}"
    const val exoPlayerUi = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoPlayer}"

    /**
     * [DataStore ]
     * https://developer.android.com/topic/libraries/architecture/datastore#kts
     */
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
}