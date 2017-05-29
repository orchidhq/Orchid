package com.eden.orchid;

import com.eden.orchid.api.OrchidContext;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TestReferences {

    @Mock
    private OrchidContext context;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

//    @Test
//    public void testCreatingReferencesUnix() throws Throwable {
//        // Test unix-style file path
//        String fullFilePath = "/Users/cbrooks/Documents/personal/java/eden/Orchid/Core/src/orchidDocs/resources/pages/about.md";
//
//        OrchidReference ref1 = new OrchidReference(context, fullFilePath);
//        assertThat(ref1.getPath(),       is(equalTo("pages")));
//        assertThat(ref1.getFileName(),   is(equalTo("about")));
//        assertThat(ref1.getExtension(),  is(equalTo("md")));
//        assertThat(ref1.getServerPath(), is(equalTo("/pages/about.md")));
//        assertThat(ref1.getRelativePath(), is(equalTo("pages/about.md")));
//
////        OrchidReference ref3 = new OrchidReference(context, fullFilePath);
////        assertThat(ref3.getBasePath(), is(equalTo(null)));
////        assertThat(ref3.getPath(), is(equalTo("Users/cbrooks/Documents/personal/java/eden/Orchid/Core/src/orchidDocs/resources/pages")));
////        assertThat(ref3.getFileName(), is(equalTo("about")));
////        assertThat(ref3.getExtension(), is(equalTo("md")));
////        assertThat(ref3.getServerPath(), is(equalTo("/Users/cbrooks/Documents/personal/java/eden/Orchid/Core/src/orchidDocs/resources/pages/about.md")));
////        assertThat(ref3.getRelativePath(), is(equalTo("Users/cbrooks/Documents/personal/java/eden/Orchid/Core/src/orchidDocs/resources/pages/about.md")));
//    }
//
//    @Test
//    public void testCreatingReferencesWindows() throws Throwable {
//        // Test windows-style file paths
//        String basePath = "C:/projects/orchid/Orchid/Core/src/orchidDocs/resources";
//        String fullFilePath = "C:/projects/orchid/Orchid/Core/src/orchidDocs/resources/pages/about.md";
//        OrchidReference ref1 = new OrchidReference(context, basePath, fullFilePath);
//        assertThat(ref1.getBasePath(), is(equalTo(basePath)));
//        assertThat(ref1.getPath(), is(equalTo("pages")));
//        assertThat(ref1.getFileName(), is(equalTo("about")));
//        assertThat(ref1.getExtension(), is(equalTo("md")));
//        assertThat(ref1.getServerPath(), is(equalTo("/pages/about.md")));
//        assertThat(ref1.getRelativePath(), is(equalTo("pages/about.md")));
//
////        OrchidReference ref3 = new OrchidReference(context, fullFilePath);
////        assertThat(ref3.getBasePath(), is(equalTo(null)));
////        assertThat(ref3.getPath(), is(equalTo("projects/orchid/Orchid/Core/src/orchidDocs/resources/pages")));
////        assertThat(ref3.getFileName(), is(equalTo("about")));
////        assertThat(ref3.getExtension(), is(equalTo("md")));
////        assertThat(ref3.getServerPath(), is(equalTo("/projects/orchid/Orchid/Core/src/orchidDocs/resources/pages/about.md")));
////        assertThat(ref3.getRelativePath(), is(equalTo("projects/orchid/Orchid/Core/src/orchidDocs/resources/pages/about.md")));
//    }
//
//    @Test
//    public void testManipulatingReferencesWindows() throws Throwable {
//        // Test windows-style file paths
//        String basePath = "C:/projects/orchid/Orchid/Core/src/orchidDocs/resources";
//        String fullFilePath = "C:/projects/orchid/Orchid/Core/src/orchidDocs/resources/pages/about.md";
//        OrchidReference ref1 = new OrchidReference(context, basePath, fullFilePath);
//        assertThat(ref1.getBasePath(), is(equalTo(basePath)));
//        assertThat(ref1.getPath(), is(equalTo("pages")));
//        assertThat(ref1.getFileName(), is(equalTo("about")));
//        assertThat(ref1.getExtension(), is(equalTo("md")));
//        assertThat(ref1.getServerPath(), is(equalTo("/pages/about.md")));
//        assertThat(ref1.getRelativePath(), is(equalTo("pages/about.md")));
//
//        ref1.setUsePrettyUrl(true);
//
//        assertThat(ref1.getPath(), is(equalTo("pages/about")));
//        assertThat(ref1.getFileName(), is(equalTo("index")));
//        assertThat(ref1.getExtension(), is(equalTo("md")));
//        assertThat(ref1.getServerPath(), is(equalTo("/pages/about")));
//        assertThat(ref1.getRelativePath(), is(equalTo("pages/about")));
//
//        ref1.setUsePrettyUrl(false);
//
//        assertThat(ref1.getBasePath(), is(equalTo(basePath)));
//        assertThat(ref1.getPath(), is(equalTo("pages")));
//        assertThat(ref1.getFileName(), is(equalTo("about")));
//        assertThat(ref1.getExtension(), is(equalTo("md")));
//        assertThat(ref1.getServerPath(), is(equalTo("/pages/about.md")));
//        assertThat(ref1.getRelativePath(), is(equalTo("pages/about.md")));
//    }

}
