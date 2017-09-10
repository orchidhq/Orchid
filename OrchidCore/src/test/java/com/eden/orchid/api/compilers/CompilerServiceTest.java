package com.eden.orchid.api.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"})
public final class CompilerServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private CompilerService underTest;
    private CompilerServiceImpl service;

    private OrchidCompiler mockCompiler;
    private OrchidParser mockParser;
    private OrchidPrecompiler mockPrecompiler;

    private String mockInput;
    private String compiledOutput;
    private JSONObject parsedOutput;
    private String precompilerOutput;
    private JSONElement precompilerElement;
    private EdenPair<String, JSONElement> precompilerEmbeddedData;

    @BeforeMethod
    public void testSetup() {
        // target outputs
        mockInput = "input";
        compiledOutput = "compiled";
        parsedOutput = new JSONObject();
        precompilerOutput = "precompiled";
        precompilerElement = new JSONElement(new JSONObject());
        precompilerEmbeddedData = new EdenPair<>(precompilerOutput, precompilerElement);

        Set<OrchidCompiler> compilers = new HashSet<>();
        mockCompiler = mock(OrchidCompiler.class);
        compilers.add(mockCompiler);
        when(mockCompiler.getSourceExtensions()).thenReturn(new String[] {"md", "markdown"});
        when(mockCompiler.getOutputExtension()).thenReturn("html");
        when(mockCompiler.compile("md", mockInput)).thenReturn(compiledOutput);
        when(mockCompiler.compile("markdown", mockInput)).thenReturn(compiledOutput);

        Set<OrchidParser> parsers = new HashSet<>();
        mockParser = mock(OrchidParser.class);
        parsers.add(mockParser);
        when(mockParser.getSourceExtensions()).thenReturn(new String[] {"yml", "yaml"});
        when(mockParser.parse("yml", "input")).thenReturn(parsedOutput);
        when(mockParser.parse("yaml", "input")).thenReturn(parsedOutput);

        mockPrecompiler = mock(OrchidPrecompiler.class);
        when(mockPrecompiler.precompile(mockInput)).thenReturn(precompilerOutput);
        when(mockPrecompiler.getEmbeddedData(mockInput)).thenReturn(precompilerEmbeddedData);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new CompilerServiceImpl(compilers, parsers, mockPrecompiler);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new CompilerService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void getCompilerExtensions() throws Throwable {
        assertThat(underTest.getCompilerExtensions(), containsInAnyOrder("md", "markdown"));
    }

    @Test
    public void getParserExtensions() throws Throwable {
        assertThat(underTest.getParserExtensions(), containsInAnyOrder("yaml", "yml"));
    }

    @Test
    public void compilerFor() throws Throwable {
        assertThat(underTest.compilerFor("md"), is(mockCompiler));
        assertThat(underTest.compilerFor("markdown"), is(mockCompiler));
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
    }

    @Test
    public void parse() throws Throwable {
        assertThat(underTest.parse("yml", mockInput), is(parsedOutput));
        assertThat(underTest.parse("yaml", mockInput), is(parsedOutput));
    }

    @Test
    public void getEmbeddedData() throws Throwable {
        assertThat(underTest.getEmbeddedData(mockInput), is(precompilerEmbeddedData));
    }

    @Test
    public void precompile() throws Throwable {
        assertThat(underTest.precompile(mockInput), is(precompilerOutput));
    }

    @Test
    public void getOutputExtension() throws Throwable {
        assertThat(underTest.getOutputExtension("md"), is("html"));
        assertThat(underTest.getOutputExtension("markdown"), is("html"));
    }

}
