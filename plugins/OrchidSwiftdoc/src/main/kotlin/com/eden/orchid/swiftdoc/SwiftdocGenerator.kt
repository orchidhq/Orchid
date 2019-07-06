package com.eden.orchid.swiftdoc

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
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
import com.eden.orchid.utilities.InputStreamCollector
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.name.Named
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

@Description(
    "Generate documentation from Swift code and comments with the help of SourceKitten. It's like Javadoc, " +
            "but for Swift!",
    name = "Swiftdoc"
)
class SwiftdocGenerator
@Inject
constructor(
    @Named("src") val resourcesDir: String
) : OrchidGenerator<SwiftdocModel>(GENERATOR_KEY, PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "swiftdoc"
    }

    @Option
    @StringDefault("swift")
    @Description("The source directories with Swift files to document.")
    lateinit var sourceDirs: List<String>

    override fun startIndexing(context: OrchidContext): SwiftdocModel {

        val allStatements = ArrayList<SwiftStatement>()
        val pages = ArrayList<SwiftdocSourcePage>()
        val statementPages = ArrayList<SwiftdocStatementPage>()

        sourceDirs
            .forEach { baseDir ->
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
                            isHidden =
                                isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.private")
//                        isHidden = isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.internal")

                            if (!isHidden) { // skip hidden/internal statements
                                val statement: SwiftStatement? = when (o.getString("key.kind")) {
                                    "source.lang.swift.decl.class" -> SwiftClass(context, o, fileResource)
                                    "source.lang.swift.decl.typealias" -> SwiftTypealias(context, o, fileResource)
                                    "source.lang.swift.syntaxtype.comment.mark" -> SwiftFloatingComment(
                                        context,
                                        o,
                                        fileResource
                                    )
                                    "source.lang.swift.decl.enum" -> SwiftEnum(context, o, fileResource)
                                    "source.lang.swift.decl.extension" -> SwiftExtension(context, o, fileResource)
                                    "source.lang.swift.decl.function.free" -> SwiftFree(context, o, fileResource)
                                    "source.lang.swift.decl.protocol" -> SwiftProtocol(context, o, fileResource)
                                    "source.lang.swift.decl.var.global" -> SwiftGlobal(context, o, fileResource)
                                    "source.lang.swift.decl.struct" -> SwiftStruct(context, o, fileResource)
                                    else -> {
                                        Clog.e("found other statement kind {}", o.getString("key.kind"))
                                        null
                                    }
                                }
                                if (statement != null) {
                                    statement.init()
                                    statements.add(statement)
                                    allStatements.add(statement)
                                }
                            }
                        }

                        val res = StringResource("", ref)
                        val page = SwiftdocSourcePage(res, statements, codeJson.toString(2))
                        pages.add(page)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        for (statement in allStatements) {
            if (statement is SwiftExtension) continue
            if (statement is SwiftFloatingComment) continue
            if (statement is SwiftFree) continue
            if (statement is SwiftTypealias) continue

            val ref = OrchidReference(statement.origin)
            ref.stripFromPath("swift/source")
            if (!EdenUtils.isEmpty(ref.originalPath)) {
                ref.path = OrchidUtils.toSlug("swift/${statement.kind}/${ref.originalPath}/${statement.name}")
            } else {
                ref.path = OrchidUtils.toSlug("swift/${statement.kind}/${statement.name}")
            }
            ref.fileName = OrchidUtils.toSlug(ref.originalFileName)
            val page = SwiftdocStatementPage(BaseSwiftdocResource(ref, statement), statement)
            page.title = statement.name
            statementPages.add(page)
        }

        return SwiftdocModel(allStatements, pages, statementPages)
    }

    override fun getCollections(
        context: OrchidContext,
        model: SwiftdocModel
    ): List<OrchidCollection<*>> {
        return listOf(
            PageCollection(this, "swiftClasses", model.classPages),
            PageCollection(this, "swiftStructs", model.structPages),
            PageCollection(this, "swiftProtocols", model.protocolPages),
            PageCollection(this, "swiftEnums", model.enumPages),
            PageCollection(this, "swiftGlobals", model.globalPages)
        )
    }

    override fun startGeneration(
        context: OrchidContext,
        model: SwiftdocModel
    ) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

// Run SourceKitten executable
//----------------------------------------------------------------------------------------------------------------------

    private fun parseSwiftFile(name: String): JSONObject {
        try {
            val processBuilder = ProcessBuilder()
            processBuilder.command("sourcekitten", "doc", "--single-file", "./$name")
            processBuilder.directory(File(resourcesDir))
            processBuilder.redirectErrorStream()

            val process = processBuilder.start()

            val collector = InputStreamCollector(process.inputStream)
            Executors.newSingleThreadExecutor().submit(collector)
            process.waitFor()

            val codeJson = JSONObject(collector.toString())
            return codeJson.getJSONObject(codeJson.keySet().first())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return JSONObject()
    }

}