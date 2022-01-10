// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://jitpack.io")

    }

    dependencies {
        val kotlinVersion = "1.5.31"

        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.38.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.1")
        classpath(kotlin("serialization", version = kotlinVersion))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

project.tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}