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
}

fun OrchidIntegrationTest.addPageMenus() {
    configObject(
        "theme",
        """
        |{
        |    "menu": [
        |        
        |    ]
        |}
        |""".trimMargin()
    )
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