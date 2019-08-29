package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest

fun OrchidIntegrationTest.javadocSetup() {
    configObject(
        "javadoc",
        """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava"
            |    ]
            |}
            |""".trimMargin()
    )
    configObject(
        "theme",
        """
        |{
        |    "menu": [
        |        {
        |            "type": "sourcedocPages",
        |            "module": "javadoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Javadoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "javadoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Javadoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "javadoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun OrchidIntegrationTest.groovydocSetup() {
    configObject(
        "groovydoc",
        """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava",
            |        "./../../OrchidGroovydoc/src/mockGroovy",
            |    ]
            |}
            |""".trimMargin()
    )
    configObject(
        "theme",
        """
        |{
        |    "menu": [
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Groovydoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Groovydoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "groovydoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
    )
}

fun OrchidIntegrationTest.kotlindocSetup() {
    configObject(
        "kotlindoc",
        """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava",
            |        "./../../OrchidKotlindoc/src/mockKotlin"
            |    ]
            |}
            |""".trimMargin()
    )
    configObject(
        "theme",
        """
        |{
        |    "menu": [
        |        {
        |            "type": "sourcedocPages",
        |            "module": "kotlindoc",
        |            "node": "packages",
        |            "asSubmenu": true,
        |            "submenuTitle": "Kotlindoc Packages"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "kotlindoc",
        |            "node": "classes",
        |            "asSubmenu": true,
        |            "submenuTitle": "Kotlindoc Classes"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |        {
        |            "type": "sourcedocPages",
        |            "module": "kotlindoc"
        |        },
        |        {
        |            "type": "separator"
        |        },
        |    ]
        |}
        |""".trimMargin()
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
