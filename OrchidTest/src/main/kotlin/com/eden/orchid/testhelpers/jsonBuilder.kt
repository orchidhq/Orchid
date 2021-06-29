package com.eden.orchid.testhelpers

import java.util.LinkedHashMap

@DslMarker
annotation class JsonDsl

@JsonDsl
inline fun obj(build: JsonObjectBuilder.() -> Unit): Map<String, Any> {
    return JsonObjectBuilder().also(build).map
}

@JsonDsl
inline fun arr(build: JsonArrayBuilder.() -> Unit): List<Any> {
    return JsonArrayBuilder().also(build).arr
}

@JsonDsl
class JsonObjectBuilder {
    val map: MutableMap<String, Any> = LinkedHashMap()

    infix fun String.to(value: String) {
        map[this] = value
    }

    infix fun String.to(value: Int) {
        map[this] = value
    }

    infix fun String.to(value: Double) {
        map[this] = value
    }

    infix fun String.to(value: Boolean) {
        map[this] = value
    }

    infix fun String.to(value: Map<String, Any>) {
        map[this] = value
    }

    infix fun String.to(value: List<Any>) {
        map[this] = value
    }
}

@JsonDsl
class JsonArrayBuilder {
    val arr: MutableList<Any> = mutableListOf()

    infix fun add(value: String) {
        arr += value
    }

    infix fun add(value: Int) {
        arr += value
    }

    infix fun add(value: Double) {
        arr += value
    }

    infix fun add(value: Boolean) {
        arr += value
    }

    infix fun add(value: Map<String, Any>) {
        arr += value
    }

    infix fun add(value: List<Any>) {
        arr.add(value)
    }
}

/*

- make elevator pitch for Reliant persistant login, reauthenticate on secured screens

 */
