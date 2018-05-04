package com.eden.orchid.swiftdoc

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.swiftdoc.page.BaseSwiftdocResource
import com.eden.orchid.swiftdoc.page.SwiftdocSourcePage
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import com.eden.orchid.swiftdoc.swift.statements.SwiftClass
import com.eden.orchid.swiftdoc.swift.statements.SwiftEnum
import com.eden.orchid.swiftdoc.swift.statements.SwiftExtension
import com.eden.orchid.swiftdoc.swift.statements.SwiftFloatingComment
import com.eden.orchid.swiftdoc.swift.statements.SwiftFree
import com.eden.orchid.swiftdoc.swift.statements.SwiftGlobal
import com.eden.orchid.swiftdoc.swift.statements.SwiftProtocol
import com.eden.orchid.swiftdoc.swift.statements.SwiftStruct
import com.eden.orchid.swiftdoc.swift.statements.SwiftTypealias
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.name.Named
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.stream.Stream
import javax.inject.Inject

class SwiftdocGenerator
@Inject constructor(context: OrchidContext, val model: SwiftdocModel, @Named("resourcesDir") val resourcesDir: String)
    : OrchidGenerator(context, "swiftdoc", OrchidGenerator.PRIORITY_DEFAULT) {

    @Option
    @StringDefault("swift")
    @Description("The base directory in local resources to look for swift source code in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        model.initialize()

        context.getLocalResourceEntries(baseDir, arrayOf("swift"), true).forEach { resource ->
            val sourceFile = resource.reference.originalFullFileName
            try {
                val codeJson = parseSwiftFile(sourceFile)

                val ref = OrchidReference(resource.reference)
                ref.extension = "html"
                ref.stripFromPath(baseDir)
                ref.path = OrchidUtils.normalizePath(OrchidUtils.toSlug("swift/source/" + ref.originalPath))
                ref.fileName = OrchidUtils.toSlug(ref.originalFileName)

                val fileResource = StringResource(resource.rawContent, ref)

                val arr = codeJson.optJSONArray("key.substructure") ?: JSONArray()

                val statements = ArrayList<SwiftStatement>()

                arr.forEachIndexed { i, _ ->
                    val o = arr.getJSONObject(i)

                    var isHidden = false
                    isHidden = isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.private")
//                        isHidden = isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.internal")

                    if(!isHidden) { // skip hidden/internal statements
                        val statement: SwiftStatement? = when (o.getString("key.kind")) {
                            "source.lang.swift.decl.class"              -> SwiftClass(context, o, fileResource)
                            "source.lang.swift.decl.typealias"          -> SwiftTypealias(context, o, fileResource)
                            "source.lang.swift.syntaxtype.comment.mark" -> SwiftFloatingComment(context, o, fileResource)
                            "source.lang.swift.decl.enum"               -> SwiftEnum(context, o, fileResource)
                            "source.lang.swift.decl.extension"          -> SwiftExtension(context, o, fileResource)
                            "source.lang.swift.decl.function.free"      -> SwiftFree(context, o, fileResource)
                            "source.lang.swift.decl.protocol"           -> SwiftProtocol(context, o, fileResource)
                            "source.lang.swift.decl.var.global"         -> SwiftGlobal(context, o, fileResource)
                            "source.lang.swift.decl.struct"             -> SwiftStruct(context, o, fileResource)
                            else                                        -> {
                                Clog.e("found other statement kind {}", o.getString("key.kind"))
                                null
                            }
                        }
                        if (statement != null) {
                            statement.init()
                            statements.add(statement)
                            model.allStatements.add(statement)
                        }
                    }
                }

                val res = StringResource("", ref)
                val page = SwiftdocSourcePage(res, statements, codeJson.toString(2))
                model.pages.add(page)
            }
            catch(e: Exception) {
                e.printStackTrace()
            }
        }

        for(statement in model.allStatements) {
            if(statement is SwiftExtension) continue
            if(statement is SwiftFloatingComment) continue
            if(statement is SwiftFree) continue
            if(statement is SwiftTypealias) continue

            val ref = OrchidReference(statement.origin)
            ref.stripFromPath("swift/source")
            if(!EdenUtils.isEmpty(ref.originalPath)) {
                ref.path = OrchidUtils.toSlug("swift/${statement.kind}/${ref.originalPath}/${statement.name}")
            }
            else {
                ref.path = OrchidUtils.toSlug("swift/${statement.kind}/${statement.name}")
            }
            ref.fileName = OrchidUtils.toSlug(ref.originalFileName)
            val page = SwiftdocStatementPage(BaseSwiftdocResource(ref, statement), statement)
            page.title = statement.name
            model.statementPages.add(page)
        }

        return model.getAllPages()
    }

    override fun getCollections(): List<OrchidCollection<*>>? {
        val collections = java.util.ArrayList<OrchidCollection<*>>()

        collections.add(SwiftdocCollection(this, "swiftClasses",   model.classPages))
        collections.add(SwiftdocCollection(this, "swiftStructs",   model.structPages))
        collections.add(SwiftdocCollection(this, "swiftProtocols", model.protocolPages))
        collections.add(SwiftdocCollection(this, "swiftEnums",     model.enumPages))
        collections.add(SwiftdocCollection(this, "swiftGlobals",   model.globalPages))

        return collections
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

// Run SourceKitten executable
//----------------------------------------------------------------------------------------------------------------------

    private fun parseSwiftFile(name: String): JSONObject {
        try {
        val process = ProcessBuilder()
                .command("sourcekitten", "doc", "--single-file", "./$name")
                .directory(File(resourcesDir))
                .start()

            val collector = InputStreamCollector(process.inputStream)
            Executors.newSingleThreadExecutor().submit(collector)
            process.waitFor()

            val codeJson = JSONObject(collector.toString())
            return codeJson.getJSONObject(codeJson.keySet().first())
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return JSONObject()
    }

    private class InputStreamCollector(val inputStream: InputStream) : Runnable {

        var output = StringBuffer()

        override fun run() {
            output = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).lines().forEach({output.append(it)})
        }

        override fun toString(): String {
            return output.toString()
        }
    }

}