package com.eden.orchid.javadoc.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.javadoc.models.JavadocModel
import com.sun.javadoc.ProgramElementDoc
import javax.inject.Inject

class JavadocVisibilityFunction
@Inject
constructor(val model: JavadocModel) : TemplateFunction("javadocVisibility") {


    override fun parameters(): Array<String> {
        return arrayOf()
    }

    override fun isSafe(): Boolean {
        return true
    }

    override fun apply(input: Any?): Any {
        if(input is ProgramElementDoc) {
            if(input.isPublic) {
                return "public"
            }
            else if(input.isProtected) {
                return "protected"
            }
            else if(input.isPrivate) {
                return "private"
            }
            else if(input.isPackagePrivate) {
                return ""
            }
        }

        throw IllegalArgumentException("Input to javadocVisibility must be a ProgramElementDoc, such as a field or method.")
    }
}