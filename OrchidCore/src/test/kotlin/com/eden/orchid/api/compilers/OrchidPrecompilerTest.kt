package com.eden.orchid.api.compilers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler
import com.eden.orchid.impl.compilers.parsers.JsonParser
import com.eden.orchid.impl.compilers.parsers.PropertiesParser
import com.eden.orchid.impl.compilers.parsers.TOMLParser
import com.eden.orchid.impl.compilers.parsers.YamlParser
import com.eden.orchid.testhelpers.OrchidUnitTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue

class OrchidPrecompilerTest : OrchidUnitTest {

    private lateinit var context: OrchidContext
    private lateinit var compilerService: CompilerService

    private lateinit var yamlParser: OrchidParser
    private lateinit var jsonParser: OrchidParser
    private lateinit var tomlParser: OrchidParser
    private lateinit var propertiesParser: OrchidParser
    private lateinit var parsers: Set<OrchidParser>

    private lateinit var underTest: FrontMatterPrecompiler

    @BeforeEach
    fun setUp() {
        context = mock(OrchidContext::class.java)

        yamlParser = YamlParser()
        jsonParser = JsonParser()
        tomlParser = TOMLParser()
        propertiesParser = PropertiesParser()
        parsers = setOf(yamlParser, jsonParser, tomlParser, propertiesParser)

        underTest = FrontMatterPrecompiler(context, parsers)

        compilerService = CompilerServiceImpl()

        `when`(context.resolveSet(OrchidCompiler::class.java)).thenReturn(emptySet())
        `when`(context.resolveSet(OrchidParser::class.java)).thenReturn(parsers)
        `when`(context.resolve(OrchidPrecompiler::class.java)).thenReturn(underTest)
        `when`(context.getService(CompilerService::class.java)).thenReturn(compilerService)
        `when`(context.parserFor(anyString())).thenCallRealMethod()
        `when`(context.parse(anyString(), anyString())).thenCallRealMethod()

        compilerService.initialize(context)
    }

    @Test
    fun getCompilerExtensions() {
        assertThat(underTest.priority, `is`(equalTo(100)))
    }

    @Test
    fun parseYamlFrontMatter() {
        val input = """
            |---
            |title: Front Matter Title
            |---
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun parseJsonFrontMatter() {
        val input = """
            |;;;
            |{
            |   "title": "Front Matter Title"
            |}
            |;;;
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun parseTomlFrontMatter() {
        val input = """
            |+++
            |title = "Front Matter Title"
            |+++
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun parseYamlFrontMatterFromExtension() {
        val input = """
            |---yml
            |title: Front Matter Title
            |---
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }


    @Test
    fun parseJsonFrontMatterFromExtension() {
        val input = """
            |---json
            |{
            |   "title": "Front Matter Title"
            |}
            |---
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun parseTomlFrontMatterFromExtension() {
        val input = """
            |---toml
            |title = "Front Matter Title"
            |---
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun testNoFrontMatter() {
        val input = "Page Content"

        expectThat(underTest.shouldPrecompile("md", input)).isFalse()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo(input) }
                .and { get { second }.isNull() }
    }

    @Test
    fun parseEmptyFrontMatter() {
        val input = """
            |---
            |---
            |Page Content
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }.isNotNull().isEmpty() }
    }

    @Test
    fun parseFrontMatterWithCustomDelimiters() {
        val input = """
            |###
            |title: 'Front Matter Title'
            |###
            |Page Content
        """.trimMargin()

        // test when no custom delimiters are set
        expectThat(underTest.shouldPrecompile("md", input)).isFalse()
        var output = underTest.getEmbeddedData("md", input)
        expectThat(output)
                .and { get { first }.isEqualTo(input) }
                .and { get { second }.isNull() }

        underTest.customDelimeters = listOf(
                FrontMatterPrecompiler.CustomDelimiter().apply {
                    regex = "^###\n(.*)\n###\n"
                    group = 1
                    parser = "yml"
                    fileExtensions = listOf("js")
                }
        )

        // test when custom delimiter is set, but input does not match the delimiter's file extensions
        expectThat(underTest.shouldPrecompile("md", input)).isFalse()
        output = underTest.getEmbeddedData("md", input)
        expectThat(output)
                .and { get { first }.isEqualTo(input) }
                .and { get { second }.isNull() }

        // test when custom delimiter is set, and input matches the delimiter's file extensions
        expectThat(underTest.shouldPrecompile("js", input)).isTrue()
        output = underTest.getEmbeddedData("js", input)
        expectThat(output)
                .and { get { first }.isEqualTo("Page Content") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun parseFrontMatterCustomDelimiter_CssComments() {
        val input = """
                |/*
                |title: "Front Matter Title"
                |customItems:
                |  - Item One
                |  - Item Two
                |  - Item Three
                |*/
                |
                |.button {
                |  color: red;
                |}
        """.trimMargin()

        underTest.customDelimeters = listOf(
                FrontMatterPrecompiler.CustomDelimiter().apply {
                    regex = "^/\\*\n(.*)\n\\*/\n"
                    group = 1
                    parser = "yml"
                    fileExtensions = listOf("css")
                }
        )

        // test when custom delimiter is set, and input matches the delimiter's file extensions
        expectThat(underTest.shouldPrecompile("css", input)).isTrue()
        val output = underTest.getEmbeddedData("css", input)
        expectThat(output)
                .and { get { first.replace("\\s".toRegex(), "") }.isEqualTo(".button{color:red;}") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
                .and {
                    get { second }["customItems"]
                            .isA<Collection<*>>()
                            .get { toList() }
                            .hasSize(3)
                            .containsExactly("Item One", "Item Two", "Item Three")
                }
    }

    @Test
    fun parseFrontMatterCustomDelimiter_JbakeCompatibility() {
        val input = """
                |title=Weekly Links #2
                |date=2013-02-01
                |type=post
                |tags=weekly links, java
                |status=published
                |~~~~~~
                |# Markdown content
        """.trimMargin()

        underTest.customDelimeters = listOf(
                FrontMatterPrecompiler.CustomDelimiter().apply {
                    regex = "^(.*?)\n~~~~~~\n"
                    group = 1
                    parser = "properties"
                    fileExtensions = listOf("md")
                }
        )

        // test when custom delimiter is set, and input matches the delimiter's file extensions
        expectThat(underTest.shouldPrecompile("md", input)).isTrue()
        val output = underTest.getEmbeddedData("md", input)
        expectThat(output)
                .and { get { first }.isEqualTo("# Markdown content") }
                .and { get { second }["title"].isEqualTo("Weekly Links #2") }
                .and { get { second }["date"].isEqualTo("2013-02-01") }
                .and { get { second }["type"].isEqualTo("post") }
                .and { get { second }["tags"].isEqualTo("weekly links, java") }
                .and { get { second }["status"].isEqualTo("published") }

    }

    @Test
    fun parseYamlFrontMatterWithNoPageContent() {
        val input = """
            |---
            |title: Front Matter Title
            |---
        """.trimMargin()

        expectThat(underTest.shouldPrecompile("md", input)).isTrue()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { first }.isEqualTo("") }
                .and { get { second }["title"].isEqualTo("Front Matter Title") }
    }
}
