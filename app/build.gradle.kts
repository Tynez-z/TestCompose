plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.github.ben-manes.versions")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp") version "1.5.31-1.0.1"

    kotlin("kapt")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        getByName("main") {
            kotlin.srcDir(File("build/generated/ksp/debug/kotlin"))
        }
        getByName("main") {
            kotlin.srcDir(File("build/generated/ksp/release/kotlin"))
        }
    }
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.testCompose"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"cfacbd1b17a84295a04a55d573daa740\"")
            resValue("string", "BASE_URL", "\"https://api.themoviedb.org/\"")
            buildConfigField("String", "YOUTUBE_VIDEO_URL", "\"https://www.youtube.com/watch?v=/\"")
            buildConfigField("String", "YOUTUBE_THUMBNAIL_URL", "\"https://img.youtube.com/vi/\"")
            buildConfigField("String", "BASE_POSTER_PATH", "\"https://image.tmdb.org/t/p/w342\"")
            buildConfigField("String", "BASE_BACKDROP_PATH", "\"https://image.tmdb.org/t/p/w780\"")
        }


        release {
            isMinifyEnabled = false
//            proguardFiles
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField("String", "API_KEY", "\"cfacbd1b17a84295a04a55d573daa740\"")
            resValue("string", "BASE_URL", "\"https://api.themoviedb.org/\"")
            buildConfigField("String", "YOUTUBE_VIDEO_URL", "\"https://www.youtube.com/watch?v=/\"")
            buildConfigField("String", "YOUTUBE_THUMBNAIL_URL", "\"https://img.youtube.com/vi/\"")
            buildConfigField("String", "BASE_POSTER_PATH", "\"https://image.tmdb.org/t/p/w342\"")
            buildConfigField("String", "BASE_BACKDROP_PATH", "\"https://image.tmdb.org/t/p/w780\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    /**
     * [YouTube extractor for ExoPlayer]
     */
    implementation ("com.github.evgenyneu:js-evaluator-for-android:v5.0.0")
    implementation ("com.github.HaarigerHarald:android-youtubeExtractor:master-SNAPSHOT")

    implementation("io.github.raamcosta.compose-destinations:core:1.5.1-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.5.1-beta")

    Dependencies.compose.apply {
        implementation(ui)
        implementation(material)
        implementation(tooling)
        implementation(layout)
        implementation(constraint)
        implementation(navigation)
        implementation(hiltNavigationCompose)
        implementation(swipeRefresh)
        implementation(pagingCompose)
        implementation(coilCompose)
        implementation(uiUtill)
    }

    Dependencies.other.apply {
        implementation(material)
        implementation(ktxCore)
        implementation(appCompat)
        implementation(lifecycle)
        implementation(coil)
        implementation(paging)
        implementation(exoPlayer)
        implementation(exoPlayerCore)
        implementation(exoPlayerHls)
        implementation(exoPlayerUi)
        implementation(dataStore)
        implementation(serialization)
    }

    Dependencies.accompanist.apply {
        implementation(insets)
        implementation(pager)
        implementation(pagerIndicators)
        implementation(swiperefresh)
    }

    Dependencies.hilt.apply {
        implementation(hiltAndroid)
        kapt(hiltCompiler)
        kapt(daggerHiltCompiler)
        kaptAndroidTest(daggerHiltCompiler)
    }

    Dependencies.room.apply {
        implementation(runtime)
        implementation(ktx)
        kapt(compiler)
    }

    Dependencies.retrofit.apply {
        implementation(retrofit2)
        implementation(converterGson)
        implementation(interceptor)
        implementation(gson)
        implementation(okhttp)
    }

    Dependencies.test.apply {
        //unit
        testImplementation(junit)
        androidTestImplementation(jUnitExt)

        //espresso
        androidTestImplementation(espressoTest)

        //compose
        androidTestImplementation(uiTestJunit4)
    }
}