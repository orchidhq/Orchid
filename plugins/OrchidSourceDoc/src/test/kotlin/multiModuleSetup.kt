package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.multiModuleSetup(
    type: String,
    modules: List<String>,
    showRunnerLogs: Boolean,
    nodeKinds: List<String>,
    otherSourceKinds: List<String>
) {
    configObject(
        "${type}doc",
        multiModuleSetup(type, modules, showRunnerLogs, otherSourceKinds)
    )
    configObject(
        "theme",
        multiModuleThemeMenuSetup(type, nodeKinds, modules)
    )
    configObject(
        "${type}doc",
        sourceDocPagesSetup()
    )
}

fun multiModuleSetup(
    type: String,
    names: List<String>,
    showRunnerLogs: Boolean,
    otherSourceKinds: List<String> = emptyList()
): String {
    return """
        |{
        |  "modules": [
        |    ${names.joinToString { singleModuleGeneratorSetup(type, showRunnerLogs, otherSourceKinds, it) }}
        |  ]
        |}
    """.trimMargin()
}

// Setup Theme Menus
//----------------------------------------------------------------------------------------------------------------------

fun multiModuleThemeMenuSetup(type: String, nodeKinds: List<String>, modules: List<String>): String {
    return """
        |{
        |  "menu": [
        |    ${themeMenuModulesSetup(type)},
        |    $separator,
        |    ${modules.joinToString(",") { themeMenuKindSetup(type, nodeKinds, it) + ",$separator" }.trimEnd(',')},
        |    $separator,
        |    ${modules.joinToString(",") { themeMenuAllKindsSetup(type, nodeKinds, it) + ",$separator" }.trimEnd(',')},
        |    $separator
        |  ]
        |}
        |""".trimMargin()
}
