package dependencies

object Di {

    /**
     * [Hilt Compiler]
     * https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
     */
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hilt}"

    /**
     * [Hilt Android]
     * https://mvnrepository.com/artifact/com.google.dagger/hilt-android
     */
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltCore}"

    /**
     * [Hilt Processor]
     * https://mvnrepository.com/artifact/com.google.dagger/hilt-compiler
     */
    const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltCore}"
}