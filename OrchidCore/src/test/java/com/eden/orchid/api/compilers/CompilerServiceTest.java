package com.eden.orchid.api.compilers;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public final class CompilerServiceTest extends BaseOrchidTest {

    private OrchidContext context;
    private CompilerService underTest;
    private CompilerServiceImpl service;

    private OrchidCompiler mockCompiler;
    private OrchidParser mockParser;
    private OrchidPrecompiler mockPrecompiler;

    private String mockInput;
    private String compiledOutput;
    private Map<String, Object> parsedOutput;
    private String precompilerOutput;
    private Map<String, Object> precompilerElement;
    private EdenPair<String, Map<String, Object>> precompilerEmbeddedData;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // target outputs
        mockInput = "input";
        compiledOutput = "compiled";
        parsedOutput = new HashMap<>();
        precompilerOutput = "precompiled";
        precompilerElement = new HashMap<>();
        precompilerEmbeddedData = new EdenPair<>(precompilerOutput, precompilerElement);

        Set<OrchidCompiler> compilers = new HashSet<>();
        mockCompiler = mock(OrchidCompiler.class);
        compilers.add(mockCompiler);
        when(mockCompiler.getSourceExtensions()).thenReturn(new String[]{"md", "markdown"});
        when(mockCompiler.getOutputExtension()).thenReturn("html");
        when(mockCompiler.compile("md", mockInput, null)).thenReturn(compiledOutput);
        when(mockCompiler.compile("markdown", mockInput, null)).thenReturn(compiledOutput);
        when(mockCompiler.compile("mkdwn", mockInput, null)).thenReturn(compiledOutput);

        Set<OrchidParser> parsers = new HashSet<>();
        mockParser = mock(OrchidParser.class);
        parsers.add(mockParser);
        when(mockParser.getSourceExtensions()).thenReturn(new String[]{"yml", "yaml"});
        when(mockParser.parse("yml", "input")).thenReturn(parsedOutput);
        when(mockParser.parse("yaml", "input")).thenReturn(parsedOutput);

        mockPrecompiler = mock(OrchidPrecompiler.class);
        when(mockPrecompiler.getEmbeddedData("", mockInput)).thenReturn(precompilerEmbeddedData);

        // test the service directly
        context = mock(OrchidContext.class);
        when(context.getSiteData(any())).thenReturn(null);
        when(context.resolveSet(OrchidCompiler.class)).thenReturn(compilers);
        when(context.resolveSet(OrchidParser.class)).thenReturn(parsers);
        when(context.resolve(OrchidPrecompiler.class)).thenReturn(mockPrecompiler);

        service = new CompilerServiceImpl();
        service.initialize(context);
        service.allConfig = new HashMap<>();

        // add custom compiler extensions
        service.customCompilerExtensions = new JSONObject();
        service.customCompilerExtensions.put("mkdwn", "md");

        service.onPostExtraction();

        // test that the default implementation is identical to the real implementation
        underTest = new CompilerService() {
            public void initialize(OrchidContext context) { }

            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getCompilerExtensions(), containsInAnyOrder("md", "markdown", "mkdwn"));
    }

    @Test
    public void getParserExtensions() throws Throwable {
        assertThat(underTest.getParserExtensions(), containsInAnyOrder("yaml", "yml"));
    }

    @Test
    public void compilerFor() throws Throwable {
        assertThat(underTest.compilerFor("md"), is(mockCompiler));
        assertThat(underTest.compilerFor("markdown"), is(mockCompiler));
        assertThat(underTest.compilerFor("mkdwn"), is(mockCompiler));
        assertThat(underTest.compilerFor("ad"), is(nullValue()));
    }

    @Test
    public void parserFor() throws Throwable {
        assertThat(underTest.parserFor("yml"), is(mockParser));
        assertThat(underTest.parserFor("yaml"), is(mockParser));
        assertThat(underTest.parserFor("json"), is(nullValue()));
    }

    @Test
    public void compile() throws Throwable {
        assertThat(underTest.compile("md", mockInput), is(compiledOutput));
        assertThat(underTest.compile("markdown", mockInput), is(compiledOutput));
        assertThat(underTest.compile("mkdwn", mockInput), is(compiledOutput));
    }

    @Test
    public void parse() throws Throwable {
        assertThat(underTest.parse("yml", mockInput), is(parsedOutput));
        assertThat(underTest.parse("yaml", mockInput), is(parsedOutput));
    }

    @Test
    public void getEmbeddedData() throws Throwable {
        assertThat(underTest.getEmbeddedData("", mockInput), is(precompilerEmbeddedData));
    }

    @Test
    public void getOutputExtension() throws Throwable {
        assertThat(underTest.getOutputExtension("md"), is("html"));
        assertThat(underTest.getOutputExtension("markdown"), is("html"));
    }

    @Test
    public void testBinaryExtensions() throws Throwable {
        List<String> validBinaryExtensions = new ArrayList<>();
        Collections.addAll(validBinaryExtensions, "jpg", "jpeg", "png", "pdf", "gif", "svg", "otf", "eot", "ttf", "woff", "woff2", "ico");

        String[] extArray = new String[validBinaryExtensions.size()];
        validBinaryExtensions.toArray(extArray);

        assertThat(underTest.getBinaryExtensions(), containsInAnyOrder(extArray));

        for(String ext : validBinaryExtensions) {
            assertThat(underTest.isBinaryExtension(ext), is(true));
            assertThat(underTest.isBinaryExtension(ext.toUpperCase()), is(true));
        }
        assertThat(underTest.isBinaryExtension("mov"), is(false));
        assertThat(underTest.isBinaryExtension("mpeg"), is(false));
        assertThat(underTest.isBinaryExtension("MOV"), is(false));
        assertThat(underTest.isBinaryExtension("MOV"), is(false));

        service.customBinaryExtensions = new String[]{"mov", "mpeg"};
        Collections.addAll(validBinaryExtensions, service.customBinaryExtensions);

        extArray = new String[validBinaryExtensions.size()];
        validBinaryExtensions.toArray(extArray);

        assertThat(underTest.getBinaryExtensions(), containsInAnyOrder(extArray));

        for(String ext : validBinaryExtensions) {
            assertThat(underTest.isBinaryExtension(ext), is(true));
            assertThat(underTest.isBinaryExtension(ext.toUpperCase()), is(true));
        }
        assertThat(underTest.isBinaryExtension("mov"), is(true));
        assertThat(underTest.isBinaryExtension("mpeg"), is(true));
        assertThat(underTest.isBinaryExtension("MOV"), is(true));
        assertThat(underTest.isBinaryExtension("MOV"), is(true));
    }

}
