package com.eden.orchid.sourcedoc

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun OrchidIntegrationTest.testCss() {
    resource(
        "assets/css/orchidSourceDoc.scss", """
            |* {
            |  box-sizing: border-box;
            |}
            |.sourcedoc-page {
            |  display: grid;
            |  grid-template-columns: 70% auto;
            |  
            |  .orchid-sourcedoc {
            |    grid-column: 1;
            |    border: thin solid gray;
            |    padding: 20px 40px;
            |  }
            |  .menus {
            |    grid-column: 2;
            |    border: thin solid gray;
            |    padding: 20px 40px;
            |  }
            |}
            |
            |.orchid-sourcedoc {
            |  
            |  --section-color: black;
            |  .section {
            |    border: thin solid var(--section-color);
            |    border-radius: 8px;
            |    position: relative;
            |    padding: 0 40px;
            |    margin-top: 40px;
            |    margin-bottom: 40px;
            |
            |    &::before {
            |      content: attr(data-tab-text);
            |      background: var(--section-color);
            |      color: white;
            |      font-family: monospace;
            |      border-top-left-radius: 8px;
            |      border-top-right-radius: 8px;
            |      position: absolute;
            |      height: 20px;
            |      top: -20px;
            |      right: 10px;
            |      padding: 0 10px;
            |      vertical-align: middle;
            |    }
            |
            |    * {
            |      padding-top: 20px;
            |      padding-bottom: 20px;
            |    }
            |  }
            |}
            """.trimMargin()
    )
}

fun OrchidIntegrationTest.testPageStructure() {
    resource(
        "templates/pages/sourceDocPage.peb", """
            |<div class="sourcedoc-page">
            |  <h1>{{ page.title }}</h1>
            |  <div class="orchid-sourcedoc">
            |    <h2>Page Content</h2>
            |    {{ renderSection(page, page.rootSection) }}
            |  </div>
            |  <div class="menus">
            |    <div class="theme-menu">
            |      <h2>Theme Menu</h2>
            |      {% include 'themeMenu.peb' %}
            |    </div>
            |    <div class="page-menu">
            |      <h2>Page Menu</h2>
            |      {% include 'pageMenu.peb' %}
            |    </div>
            |  </div>
            |</div>
            |
            |{% macro renderSection(page, section) %}
            |<div class="section section-root" data-tab-text="{{ page.sectionId(section) }}" style="--section-color: green;" id="{{ page.sectionId(section) }}">
            |{% for element in section.elements %}
            |  <div class="section section-element" data-tab-text="{{ element.kind }}" style="--section-color: black;" id="{{ page.elementId(element) }}">
            |    <div class="section section-signature" data-tab-text="signature" style="--section-color: red;">{{ page.renderSignature(element)|raw }}</div>
            |    <div class="section section-comments" data-tab-text="comments" style="--section-color: blue;">{{ page.renderComment(element)|compileAs('md') }}</div>
            |    {% set childrenSections = page.getSectionsData(element) %}
            |    {% if childrenSections|length > 0 %}
            |      {% for childSection in childrenSections %}
            |        {{ renderSection(page, childSection) }}
            |      {% endfor %}
            |    {% endif %}
            |  </div>
            |{% endfor %}
            |</div>
            |{% endmacro %}
        """.trimMargin()
    )

    resource(
        "templates/pages/sourceDocModules.peb",
        """
            |<ul>
            |{% for module in page.modules %}
            |  <li><a href="{{ module.homepage }}">{{ module.name }}</a>
            |{% endfor %}
            |</li>
        """.trimMargin()
    )
}

fun OrchidIntegrationTest.addPageMenus() {
    configObject(
        "allPages",
        """
        |{
        |    "menu": [
        |        {
        |            "type": "sourcedocPageLinks",
        |            "includeItems": false
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPageLinks",
        |            "includeItems": true
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPageLinks",
        |            "includeItems": true,
        |            "itemTitleType": "SIGNATURE"
        |        }
        |    ]
        |}
        |""".trimMargin()
    )
}

fun Assertion.Builder<TestResults>.withDefaultSourcedocPages(): Assertion.Builder<TestResults> {
    return this
        .pageWasRendered("/assets/css/orchidSourceDoc.css") { }
        .pageWasRendered("/favicon.ico") { }
}

// Setup modules
//----------------------------------------------------------------------------------------------------------------------

fun moduleSetup(
    type: String,
    showRunnerLogs: Boolean,
    otherSourceKinds: List<String> = emptyList(),
    name: String? = null
) : String {
    val sourcePaths = (listOf(type) + otherSourceKinds).map {
        "\"./../../Orchid${it.capitalize()}doc/src/mock${it.capitalize()}\""
    }.joinToString()

    if(name != null) {
        return """
            |{
            |    "name": "$name",
            |    "sourceDirs": [$sourcePaths],
            |    "showRunnerLogs": $showRunnerLogs
            |}
            """.trimMargin()
    }
    else {
        return """
            |{
            |    "sourceDirs": [$sourcePaths],
            |    "showRunnerLogs": $showRunnerLogs
            |}
            """.trimMargin()
    }
}

fun modulesSetup(
    type: String,
    names: List<String>,
    showRunnerLogs: Boolean,
    otherSourceKinds: List<String> = emptyList()
) : String {
    return """
        |{
        |    "modules": [
        |        ${names.joinToString { moduleSetup(type, showRunnerLogs, otherSourceKinds, it) }}
        |    ]
        |}
    """.trimMargin()
}

// Setup Theme Menus
//----------------------------------------------------------------------------------------------------------------------

private fun themeMenuKindSetup(type: String, nodeKind: String, name: String? = null) : String {
    return if(name != null) {
        """
        |{
        |    "type": "sourcedocPages",
        |    "moduleType": "${type}doc",
        |    "moduleName": "$name",
        |    "node": "$nodeKind",
        |    "asSubmenu": true,
        |    "submenuTitle": "Module ${name.capitalize()} ${type.capitalize()}doc ${nodeKind.capitalize()}"
        |}
        """.trimMargin()
    }
    else {
        """
        |{
        |    "type": "sourcedocPages",
        |    "moduleType": "${type}doc",
        |    "node": "$nodeKind",
        |    "asSubmenu": true,
        |    "submenuTitle": "${type.capitalize()}doc ${nodeKind.capitalize()}"
        |}
        """.trimMargin()
    }
}

private fun themeMenuKindSetup(type: String, nodeKinds: List<String>, name: String? = null) : String {
    return nodeKinds.joinToString(",") { themeMenuKindSetup(type, it, name) }
}

private fun themeMenuAllKindsSetup(type: String, nodeKind: String, name: String? = null) : String {
    return if(name != null) {
        """
        |{
        |    "type": "sourcedocPages",
        |    "moduleType": "${type}doc",
        |    "moduleName": "$name",
        |    "asSubmenu": true,
        |    "submenuTitle": "Module ${name.capitalize()} ${type.capitalize()}doc ${nodeKind.capitalize()}"
        |}
        """.trimMargin()
    }
    else {
        """
        |{
        |    "type": "sourcedocPages",
        |    "moduleType": "${type}doc",
        |    "asSubmenu": true,
        |    "submenuTitle": "${type.capitalize()}doc ${nodeKind.capitalize()}"
        |}
        """.trimMargin()
    }
}

private fun themeMenuAllKindsSetup(type: String, nodeKinds: List<String>, name: String? = null) : String {
    return nodeKinds.joinToString(",") { themeMenuAllKindsSetup(type, it, name) }
}

fun themeMenuSetup(type: String, nodeKinds: List<String>) : String {
    return """
        |{
        |    "menu": [
        |        ${themeMenuKindSetup(type, nodeKinds) },
        |        {"type": "separator"},
        |        ${themeMenuAllKindsSetup(type, nodeKinds)},
        |        {"type": "separator"}
        |    ]
        |}
        |""".trimMargin()
}

fun themeMenuSetup(type: String, nodeKinds: List<String>, modules: List<String>) : String {
    return """
        |{
        |    "menu": [
        |        ${modules.joinToString(",") { themeMenuKindSetup(type, nodeKinds, it) + """,{"type": "separator"}""" }.trimEnd(',') },
        |        {"type": "separator"},
        |        ${modules.joinToString(",") { themeMenuAllKindsSetup(type, nodeKinds, it) + """,{"type": "separator"}""" }.trimEnd(',') },
        |        {"type": "separator"}
        |    ]
        |}
        |""".trimMargin()
}

// Setup Tests
//----------------------------------------------------------------------------------------------------------------------

fun OrchidIntegrationTest.sourceDocTestSetup(
    type: String,
    nodeKinds: List<String>,
    otherSourceKinds: List<String>,
    showRunnerLogs: Boolean
) {
    configObject(
        "${type}doc",
        moduleSetup(type, showRunnerLogs, otherSourceKinds)
    )
    configObject(
        "theme",
        themeMenuSetup(type, nodeKinds)
    )
}

fun OrchidIntegrationTest.sourceDocTestSetup(
    type: String,
    nodeKinds: List<String>,
    otherSourceKinds: List<String>,
    modules: List<String>,
    showRunnerLogs: Boolean = false
) {
    configObject(
        "${type}doc",
        modulesSetup(type, modules, showRunnerLogs, otherSourceKinds)
    )
    configObject(
        "theme",
        themeMenuSetup(type, nodeKinds, modules)
    )
}
