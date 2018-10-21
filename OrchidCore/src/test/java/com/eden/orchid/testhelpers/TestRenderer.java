package com.eden.orchid.testhelpers;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TestRenderer implements OrchidRenderer {

    @Getter
    private final Map<String, TestRenderedPage> renderedPageMap;

    @Inject
    public TestRenderer() {
        renderedPageMap = new HashMap<>();
    }

    public boolean render(OrchidPage page, InputStream content) {
        String outputPath = OrchidUtils.normalizePath(page.getReference().getPath());
        String outputName;
        if(EdenUtils.isEmpty(OrchidUtils.normalizePath(page.getReference().getOutputExtension()))) {
            outputName = OrchidUtils.normalizePath(page.getReference().getFileName());
        }
        else {
            outputName = OrchidUtils.normalizePath(page.getReference().getFileName()) + "." + OrchidUtils.normalizePath(page.getReference().getOutputExtension());
        }

        TestRenderedPage outputFile = new TestRenderedPage("/" + outputPath + "/" + outputName, content);
        renderedPageMap.put(outputFile.path, outputFile);

        return true;
    }

    @Data
    public static class TestRenderedPage {
        private final String path;
        private final InputStream contentStream;

        public String getContent() {
            try {
                return IOUtils.toString(contentStream, Charset.forName("UTF-8"));
            }
            catch (Exception e) {
                return "";
            }
        }
    }



}
