package dependencies

object Test {

    /**
     * [JUnit test]
     * https://mvnrepository.com/artifact/junit/junit
     */
    const val junit = "junit:junit:${Versions.junit}"

    /**
     * [UI Test JUnit 4]
     * https://mvnrepository.com/artifact/androidx.compose.ui/ui-test-junit4
     */
    const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:1.10.0"
    /**
     * [JUnit ext]
     */
    const val jUnitExt = "androidx.test.ext:junit:${Versions.androidTestExtJUnit}"

    /**
     * [Espresso test]
     * https://mvnrepository.com/artifact/androidx.test.espresso/espresso-core
     */
    const val espressoTest = "androidx.test.espresso:espresso-core:${Versions.espressoTest}"

    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"

}