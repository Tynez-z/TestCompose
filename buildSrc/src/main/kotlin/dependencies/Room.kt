package dependencies

object Room {

    /**
     * [Room Runtime]
     * https://developer.android.com/jetpack/androidx/releases/room
     */
    const val runtime = "androidx.room:room-runtime:${Versions.room}"

    /**
     * [Room Compiler]
     * https://developer.android.com/jetpack/androidx/releases/room
     */
    const val compiler = "androidx.room:room-compiler:${Versions.room}"

    /**
     * [Kotlin Extensions and Coroutines support for Room]
     * https://developer.android.com/jetpack/androidx/releases/room
     */
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
}