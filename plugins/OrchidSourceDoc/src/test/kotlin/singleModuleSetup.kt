package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.singleModuleSetup(
    type: String,
    showRunnerLogs: Boolean,
    nodeKinds: List<String>,
    otherSourceKinds: List<String>,
    name: String?
) {
    configObject(
        "${type}doc",
        singleModuleGeneratorSetup(type, showRunnerLogs, otherSourceKinds, name)
    )
    configObject(
        "theme",
        singleModuleThemeMenuSetup(type, nodeKinds)
    )
    configObject(
        "${type}doc",
        sourceDocPagesSetup()
    )
}

fun singleModuleGeneratorSetup(
    type: String,
    showRunnerLogs: Boolean,
    otherSourceKinds: List<String> = emptyList(),
    name: String? = null
): String {
    val sourcePaths = (listOf(type) + otherSourceKinds).map {
        "\"./../../Orchid${it.capitalize()}doc/src/mock${it.capitalize()}\""
    }.joinToString()

    if (name != null) {
        return """
            |{
            |    "name": "$name",
            |    "sourceDirs": [$sourcePaths],
            |    "showRunnerLogs": $showRunnerLogs
            |}
            """.trimMargin()
    } else {
        return """
            |{
            |    "sourceDirs": [$sourcePaths],
            |    "showRunnerLogs": $showRunnerLogs
            |}
            """.trimMargin()
    }
}

fun singleModuleThemeMenuSetup(type: String, nodeKinds: List<String>): String {
    return """
        |{
        |  "menu": [
        |    ${themeMenuKindSetup(type, nodeKinds)},
        |    $separator,
        |    ${themeMenuAllKindsSetup(type, nodeKinds)},
        |    $separator
        |  ]
        |}
        |""".trimMargin()
}
