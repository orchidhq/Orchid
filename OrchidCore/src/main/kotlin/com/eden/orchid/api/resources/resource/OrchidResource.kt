package com.eden.orchid.api.resources.resource

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.OrchidPrecompiler
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.readToString
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

/**
 * Resources provide the "intrinsic content" of a page.
 *
 * @orchidApi extensible
 * @since v1.0.0
 */
abstract class OrchidResource(
    // TODO: remove this. Resources shouldn't _have_ a reference, but pages should _create_ a reference from APIs on the resource
    val reference: OrchidReference
) {

    protected var _content: String? = null
    protected var _embeddedData: JSONElement? = null

    abstract fun getContentStream(): InputStream

    open fun shouldPrecompile(): Boolean {
        val data = embeddedData
        if (EdenUtils.elementIsObject(data) && (data.element as JSONObject).has("precompile")) {
            return (data.element as JSONObject).getBoolean("precompile")
        }

        val rawContent = getContentStream().readToString()
        return reference
            .context
            .resolve(OrchidPrecompiler::class.java)
            .shouldPrecompile(reference.extension, rawContent)
    }

    open fun shouldRender(): Boolean {
        return true
    }

    open fun compileContent(data: Any?): String {
        var compiledContent = content
        return if (!EdenUtils.isEmpty(compiledContent)) {
            if (shouldPrecompile()) {
                compiledContent = reference.context.compile(precompilerExtension, compiledContent, data)
            }
            reference.context.compile(
                reference.extension,
                compiledContent,
                data
            )
        } else {
            ""
        }
    }

    open val precompilerExtension: String
        get() {
            val data = embeddedData
            return if (EdenUtils.elementIsObject(data) && (data.element as JSONObject).has("precompileAs")) {
                (data.element as JSONObject).getString("precompileAs")
            } else reference.context.defaultPrecompilerExtension
        }

    open fun canUpdate(): Boolean {
        return false
    }

    open fun canDelete(): Boolean {
        return false
    }

    @Throws(IOException::class)
    open fun update(newContent: InputStream) {
    }

    @Throws(IOException::class)
    open fun delete() {
    }

    override fun toString(): String {
        return "OrchidResource[" + this.javaClass.simpleName + "]{" + "reference=" + reference.relativePath + '}'
    }

    open val content: String get() {
        loadContent()
        return _content ?: ""
    }

    open val embeddedData: JSONElement get() {
        loadContent()
        return _embeddedData ?: JSONElement(JSONObject())
    }

    // Freeable Resource Impl
//----------------------------------------------------------------------------------------------------------------------
    protected fun loadContent() {
        val rawContent = getContentStream().readToString()
        if (rawContent != null) {
            val parsedContent = reference.context.getEmbeddedData(reference.extension, rawContent)
            _content = parsedContent.first
            _embeddedData = JSONElement(JSONObject(parsedContent.second))
        } else {
            _content = ""
            _embeddedData = null
        }
    }

    open fun free() {
        _content = null
        _embeddedData = null
    }
}
