package dependencies

import Versions

object Compose {
    // BOM
    const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"

    // When using BOM, we don't specify versions for these:
    const val ui = "androidx.compose.ui:ui"
    const val uiUtil = "androidx.compose.ui:ui-util"
    const val material = "androidx.compose.material:material"
    const val material3 = "androidx.compose.material3:material3"
    const val tooling = "androidx.compose.ui:ui-tooling"
    const val preview = "androidx.compose.ui:ui-tooling-preview"
    const val foundation = "androidx.compose.foundation:foundation"

    // These still need versions:
    const val constraint =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.constraint}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val hiltNavigationCompose =
        "androidx.hilt:hilt-navigation-compose:${Versions.hiltComposeNavigation}"
    const val pagingCompose = "androidx.paging:paging-compose:${Versions.pagingCompose}"

    // Coil 3.x for Compose
    const val coilCompose = "io.coil-kt.coil3:coil-compose:${Versions.coil}"
    const val coilNetwork = "io.coil-kt.coil3:coil-network-okhttp:${Versions.coil}"
    const val iconsCore = "androidx.compose.material:material-icons-core"
    const val iconsExtended = "androidx.compose.material:material-icons-extended"
}