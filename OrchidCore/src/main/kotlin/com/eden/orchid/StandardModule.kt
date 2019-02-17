package com.eden.orchid

import com.eden.orchid.api.ApiModule
import com.eden.orchid.api.registration.ClasspathModuleInstaller
import com.eden.orchid.api.registration.FlagsModule
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.impl.ImplModule
import com.google.inject.AbstractModule

@IgnoreModule
class StandardModule private constructor(
    private val args: Array<String>?,
    private val flags: Map<String, Any>?,
    private val includeCoreApi: Boolean,
    private val includeCoreImpl: Boolean,
    private val includeFlags: Boolean,
    private val includeClasspath: Boolean
) : AbstractModule() {

    override fun configure() {
        if (includeCoreApi) {
            install(ApiModule())
        }
        if (includeCoreImpl) {
            install(ImplModule())
        }
        if (includeFlags) {
            if (args == null && flags == null) {
                throw IllegalStateException("A mapping of flags must be provided to use the FlagsModule")
            }
            install(FlagsModule(args, flags))
        }
        if (includeClasspath) {
            install(ClasspathModuleInstaller())
        }
    }

    class StandardModuleBuilder internal constructor() {
        private var args: Array<String>? = null
        private var flags: Map<String, Any>? = null
        private var includeCoreApi = true
        private var includeCoreImpl = true
        private var includeFlags = true
        private var includeClasspath = true

        fun args(args: Array<String>): StandardModuleBuilder {
            this.args = args
            return this
        }

        fun flags(flags: Map<String, Any>): StandardModuleBuilder {
            this.flags = flags
            return this
        }

        fun includeCoreApi(includeCoreApi: Boolean): StandardModuleBuilder {
            this.includeCoreApi = includeCoreApi
            return this
        }

        fun includeCoreImpl(includeCoreImpl: Boolean): StandardModuleBuilder {
            this.includeCoreImpl = includeCoreImpl
            return this
        }

        fun includeFlags(includeFlags: Boolean): StandardModuleBuilder {
            this.includeFlags = includeFlags
            return this
        }

        fun includeClasspath(includeClasspath: Boolean): StandardModuleBuilder {
            this.includeClasspath = includeClasspath
            return this
        }

        fun build(): StandardModule {
            return StandardModule(args, flags, includeCoreApi, includeCoreImpl, includeFlags, includeClasspath)
        }

        override fun toString(): String {
            return "StandardModule.StandardModuleBuilder(args=" + java.util.Arrays.deepToString(this.args) + ", flags=" + this.flags + ", includeCoreApi=" + this.includeCoreApi + ", includeCoreImpl=" + this.includeCoreImpl + ", includeFlags=" + this.includeFlags + ", includeClasspath=" + this.includeClasspath + ")"
        }
    }

    companion object {
        @JvmStatic
        fun builder(): StandardModuleBuilder {
            return StandardModuleBuilder()
        }
    }
}
