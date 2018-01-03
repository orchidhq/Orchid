package com.eden.orchid.kss.parser

class Modifier(var name: String, var description: String) {

    fun className(): String {
        return name.replace("\\.".toRegex(), "").replace(":".toRegex(), " pseudo-class-").trim()
    }

}


