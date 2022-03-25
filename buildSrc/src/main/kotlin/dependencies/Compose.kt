package dependencies

import Versions

object Compose {

        /**
         * [Jetpack Compose]
         * https://developer.android.com/jetpack/androidx/releases/compose-ui
         * Including layout, drawing, and input.
         */
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"

        /**
         * [Ui Utill]
         * https://mvnrepository.com/artifact/androidx.compose.ui/ui-util?repo=google
         */
        const val uiUtill = "androidx.compose.ui:ui-util:${Versions.compose}"

        /**
         * [Material Design]
         * https://mvnrepository.com/artifact/androidx.compose.material/material
         * Design components library
         */
        const val material = "androidx.compose.material:material:${Versions.compose}"

        /**
         * [Compose Tooling]
         * https://developer.android.com/jetpack/compose/tooling
         * Tooling support (Previews, etc.)
         */
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"

        /**
         * [Compose Layouts]
         * https://mvnrepository.com/artifact/androidx.compose.foundation/foundation-layout
         * Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
         */
        const val layout = "androidx.compose.foundation:foundation:${Versions.compose}"

        /**
         * [Constraint Layout]
         * https://developer.android.com/jetpack/androidx/releases/constraintlayout
         * Position and size widgets in a flexible way with relative positioning
         */
        const val constraint = "androidx.constraintlayout:constraintlayout-compose:${Versions.constraint}"

        /**
         * [Navigation compose]
         * https://developer.android.com/jetpack/compose/navigation
         */
        const val navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"

        /**
         * [Navigation Hilt compose]
         * https://mvnrepository.com/artifact/androidx.hilt/hilt-navigation-compose
         */
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltComposeNavigation}"

        /**
         * [Swipe Refresh]
         * https://google.github.io/accompanist/swiperefresh/
         * https://mvnrepository.com/artifact/com.google.accompanist/accompanist-swiperefresh
         */
        const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}"

        /**
         * [Paging Compose]
         * https://mvnrepository.com/artifact/androidx.paging/paging-compose
         */
        const val pagingCompose = "androidx.paging:paging-compose:${Versions.pagingCompose}"

        /**
         * [Coil Compose]
         * https://coil-kt.github.io/coil/compose/
         */
        const val coilCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"
}