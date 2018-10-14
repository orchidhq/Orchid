package com.eden.orchid.api.compilers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler
import com.eden.orchid.impl.compilers.parsers.JsonParser
import com.eden.orchid.impl.compilers.parsers.TOMLParser
import com.eden.orchid.impl.compilers.parsers.YamlParser
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue

class OrchidPrecompilerTest {

    private lateinit var context: OrchidContext
    private lateinit var compilerService: CompilerService

    private lateinit var yamlParser: OrchidParser
    private lateinit var jsonParser: OrchidParser
    private lateinit var tomlParser: OrchidParser
    private lateinit var parsers: Set<OrchidParser>

    private lateinit var underTest: FrontMatterPrecompiler

    @BeforeEach
    fun testSetup() {
        context = mock(OrchidContext::class.java)

        yamlParser = YamlParser()
        jsonParser = JsonParser()
        tomlParser = TOMLParser()
        parsers = setOf(yamlParser, jsonParser, tomlParser)

        underTest = FrontMatterPrecompiler(context, parsers)

        compilerService = CompilerServiceImpl(emptySet(), parsers, underTest)
        `when`(context.getService(CompilerService::class.java)).thenReturn(compilerService)
        `when`(context.parse(anyString(), anyString())).thenCallRealMethod()
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
    }

    @Test
    fun testNoFrontMatter() {
        val input = "Page Content"

        expectThat(underTest.shouldPrecompile("md", input)).isFalse()

        val output = underTest.getEmbeddedData("md", input)

        expectThat(output)
                .and { get { it.first }.isEqualTo(input) }
                .and { get { it.second }.isNull() }
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
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }.isNotNull().isEmpty() }
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
                .and { get { it.first }.isEqualTo(input) }
                .and { get { it.second }.isNull() }

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
                .and { get { it.first }.isEqualTo(input) }
                .and { get { it.second }.isNull() }

        // test when custom delimiter is set, and input matches the delimiter's file extensions
        expectThat(underTest.shouldPrecompile("js", input)).isTrue()
        output = underTest.getEmbeddedData("js", input)
        expectThat(output)
                .and { get { it.first }.isEqualTo("Page Content") }
                .and { get { it.second }["title"].isEqualTo("Front Matter Title") }
    }
}
