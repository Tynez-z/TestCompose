// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val agp_version3 by extra("8.9.1")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven ("https://jitpack.io")
    }

    dependencies {
        val kotlinVersion = "2.3.10"
        val kspVersion = "2.3.0"

        classpath("com.android.tools.build:gradle:$agp_version3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.51.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.57.1")
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:$kspVersion")
        classpath("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:$kotlinVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

project.tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}