package com.example.testCompose.common

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

fun Job.stop() {
    apply {
        cancelChildren()
        cancel()
    }
}