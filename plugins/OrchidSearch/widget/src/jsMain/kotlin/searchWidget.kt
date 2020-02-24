package com.eden.orchid.search.widget

import com.eden.orchid.search.widget.data.lunr

fun main() {
    println("Running Search Widget from Kotlin")

    val entries = listOf("one", "two", "three")

    entries
        .filter { it != "two" }
        .map { it.toUpperCase() }
        .forEachIndexed { index, s -> println("$index -> $s") }

    lunr {
        println("loaded lunr")
    }
}