package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.page.SwiftdocSourcePage
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import com.eden.orchid.swiftdoc.swift.statements.SwiftClass
import com.eden.orchid.swiftdoc.swift.statements.SwiftEnum
import com.eden.orchid.swiftdoc.swift.statements.SwiftExtension
import com.eden.orchid.swiftdoc.swift.statements.SwiftGlobal
import com.eden.orchid.swiftdoc.swift.statements.SwiftProtocol
import com.eden.orchid.swiftdoc.swift.statements.SwiftStruct
import com.eden.orchid.swiftdoc.swift.statements.SwiftTypealias

class SwiftdocModel(
    val allStatements: List<SwiftStatement>,
    val pages: List<SwiftdocSourcePage>,
    val statementPages: List<SwiftdocStatementPage>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage>
        get() = listOf(
            *pages.toTypedArray(),
            *statementPages.toTypedArray()
        )

    val classPages: List<SwiftdocStatementPage>
        get() {
            return statementPages.filter { it.statement is SwiftClass }
        }
    val enumPages: List<SwiftdocStatementPage>
        get() {
            return statementPages.filter { it.statement is SwiftEnum }
        }
    val globalPages: List<SwiftdocStatementPage>
        get() {
            return statementPages.filter { it.statement is SwiftGlobal }
        }
    val protocolPages: List<SwiftdocStatementPage>
        get() {
            return statementPages.filter { it.statement is SwiftProtocol }
        }
    val structPages: List<SwiftdocStatementPage>
        get() {
            return statementPages.filter { it.statement is SwiftStruct }
        }

    fun extensionsFor(statement: SwiftStatement): List<SwiftExtension> {
        return allStatements
            .filter { it is SwiftExtension }
            .map { it as SwiftExtension }
            .filter { it.name == statement.name }
    }

    fun aliasesFor(statement: SwiftStatement): List<SwiftTypealias> {
        return allStatements
            .filter { it is SwiftTypealias }
            .map { it as SwiftTypealias }
            .filter { it.target == statement.name }
    }

}