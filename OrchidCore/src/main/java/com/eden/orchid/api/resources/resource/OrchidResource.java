package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Resources provide the "intrinsic content" of a page.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Getter @Setter
public abstract class OrchidResource {

    protected final OrchidContext context;
    protected final OrchidReference reference;

    protected String rawContent;
    protected String content;
    protected JSONElement embeddedData;

    protected int priority;

    @Getter(AccessLevel.NONE)
    protected boolean shouldRender = true;

    public OrchidResource(OrchidReference reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A resource must have a valid OrchidReference");
        }
        else {
            this.context = reference.getContext();
            this.reference = reference;
        }
    }

    public JSONElement queryEmbeddedData(String pointer) {
        if (embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    public boolean shouldPrecompile() {
        JSONElement data = getEmbeddedData();
        if(OrchidUtils.elementIsObject(data) && ((JSONObject) data.getElement()).has("precompile")) {
            return ((JSONObject) data.getElement()).getBoolean("precompile");
        }

        return context.getInjector().getInstance(OrchidPrecompiler.class).shouldPrecompile(getRawContent());
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public InputStream getContentStream() {
        return IOUtils.toInputStream(getRawContent(), Charset.defaultCharset());
    }

    public String getRawContent() {
        return rawContent;
    }

    public String compileContent(Object data) {
        if (!EdenUtils.isEmpty(getContent())) {
            String compiledContent = getContent();

            if (shouldPrecompile()) {
                compiledContent = context.compile(getPrecompilerExtension(), compiledContent, data);
            }

            return context.compile(
                    getReference().getExtension(),
                    compiledContent,
                    data
            );
        }
        else {
            return "";
        }
    }

    public String getPrecompilerExtension() {
        JSONElement data = getEmbeddedData();
        if(OrchidUtils.elementIsObject(data) && ((JSONObject) data.getElement()).has("precompileAs")) {
            return ((JSONObject) data.getElement()).getString("precompileAs");
        }

        return context.getDefaultPrecompilerExtension();
    }

    public boolean canUpdate() {
        return false;
    }

    public boolean canDelete() {
        return false;
    }

    public void update(InputStream newContent) throws IOException {

    }

    public void delete() throws IOException {

    }

    @Override public String toString() {
        return "OrchidResource[" + this.getClass().getSimpleName() + "]{" +
                "reference=" + reference.getRelativePath() +
                '}';
    }
}
