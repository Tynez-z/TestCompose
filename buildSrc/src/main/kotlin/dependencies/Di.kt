package dependencies

object Di {

    /**
     * [Hilt Compiler]
     * https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
     */
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    /**
     * [Hilt Android]
     * https://mvnrepository.com/artifact/com.google.dagger/hilt-android
     */
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
}