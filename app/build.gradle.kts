
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
//    id("kotlinx-serialization")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)

    }
}
android {
    namespace = "com.example.testCompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.testCompose"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation("net.zetetic:sqlcipher-android:4.13.0")
    implementation("androidx.sqlite:sqlite:2.4.0")
    /**
     * [YouTube extractor for ExoPlayer]
     */
    implementation ("com.github.evgenyneu:js-evaluator-for-android:v5.0.0")
    implementation ("com.github.HaarigerHarald:android-youtubeExtractor:master-SNAPSHOT")
    implementation("androidx.compose.ui:ui:1.10.0")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.10.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.10.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.10.0")

//    implementation("io.github.raamcosta.compose-destinations:core:1.10.2")
//    add("ksp", "io.github.raamcosta.compose-destinations:ksp:2.2.0")
    ksp("io.github.raamcosta.compose-destinations:ksp:2.2.0")
    ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.0")

    implementation(platform(Dependencies.compose.bom))
    Dependencies.compose.apply {
        implementation(ui)
        implementation(material)
        implementation(preview)
        implementation(tooling)
        implementation(constraint)
        implementation(navigation)
        implementation(hiltNavigationCompose)
//        implementation(swipeRefresh)
        implementation(pagingCompose)
        implementation(coilCompose)
        implementation(coilNetwork)
        implementation(uiUtil)
        implementation(iconsCore)
        implementation(iconsExtended)
    }

    Dependencies.other.apply {
        implementation(material)
        implementation(ktxCore)
        implementation(appCompat)
        implementation(lifecycle)
//        implementation(coil)
        implementation(paging)
        implementation(media3ExoPlayer)
        implementation(media3Hls)
        implementation(media3Ui)
        implementation(dataStore)
        implementation(serialization)
    }

    Dependencies.hilt.apply {
        implementation(hiltAndroid)
        ksp(hiltCompiler)
    }

    Dependencies.room.apply {
        implementation(runtime)
        implementation(ktx)
        implementation(roomPagination)
        implementation(pagination)
        ksp(compiler)
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