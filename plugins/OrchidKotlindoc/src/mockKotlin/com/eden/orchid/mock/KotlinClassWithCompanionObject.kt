package com.eden.orchid.mock

class KotlinClassWithCompanionObject {

    companion object {
        const val companionObjectVariable: String = ""
        fun companionObjectFunction() : String {
            return companionObjectVariable
        }
    }
}