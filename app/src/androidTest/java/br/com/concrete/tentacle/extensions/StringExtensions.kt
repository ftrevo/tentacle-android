package br.com.concrete.tentacle.extensions

import androidx.test.platform.app.InstrumentationRegistry

fun String.getJson()  = InstrumentationRegistry.getInstrumentation()
        .targetContext
        .assets
        .open(this).bufferedReader().use { it.readText() }