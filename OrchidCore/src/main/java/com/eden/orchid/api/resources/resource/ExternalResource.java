package com.eden.orchid.api.resources.resource;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.theme.pages.OrchidReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

/**
 * An ExternalResource acts either as a placeholder for URL reference, or can be a full resource when set to download.
 * When an ExternalResource is set to download, the file at the original URL will be downloaded and cached when its
 * contents are requested, so that external pages or assets can be requested at build-time from a CDN or other external
 * location, but served locally so your output site has no dependency on that external service. By default,
 * ExternalResources are set to not download, and instead just point to the desired resource.
 */
public final class ExternalResource extends FreeableResource {
    private boolean download;
    private ResponseBody responseBody;
    private final OrchidReference originalExternalReference;

    public ExternalResource(OrchidReference reference) {
        super(reference);
        originalExternalReference = new OrchidReference(reference);
        this.originalExternalReference.setUsePrettyUrl(false);
        setDownload(false);
    }

    public void setDownload(boolean download) {
        this.download = download;
        if (download) {
            this.reference.setBaseUrl(context.getBaseUrl());
        } else {
            this.reference.setBaseUrl(originalExternalReference.getBaseUrl());
        }
        free();
    }

    @Override
    public void free() {
        super.free();
        responseBody = null;
    }

    @Override
    public boolean shouldPrecompile() {
        if (download) {
            loadContent();
            return super.shouldPrecompile();
        }
        return false;
    }

    @Override
    public boolean shouldRender() {
        if (download) {
            loadContent();
            return super.shouldRender();
        }
        return false;
    }

    @Override
    public void setRawContent(String rawContent) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    private ResponseBody getContentBody() throws IOException {
        if (download) {
            Clog.i("Downloading external resource {}", originalExternalReference.toString());
            OkHttpClient client = context.resolve(OkHttpClient.class);
            Request request = new Request.Builder().url(originalExternalReference.toString()).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            return null;
        } else {
            throw new UnsupportedOperationException("This method is not allowed on ExternalResource when it is not set to download");
        }
    }

    @Override
    protected void loadContent() {
        if (rawContent == null) {
            try {
                if (responseBody == null) {
                    responseBody = getContentBody();
                }
                if (responseBody != null) {
                    rawContent = responseBody.string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.loadContent();
    }

    @Override
    public InputStream getContentStream() {
        loadContent();
        if (responseBody != null) {
            return responseBody.byteStream();
        }
        return null;
    }

    @Override
    public String getContent() {
        if (download) {
            loadContent();
            return super.getContent();
        } else {
            throw new UnsupportedOperationException("This method is not allowed on ExternalResource when it is not set to download");
        }
    }

    @Override
    public String getRawContent() {
        if (download) {
            loadContent();
            return super.getRawContent();
        } else {
            throw new UnsupportedOperationException("This method is not allowed on ExternalResource when it is not set to download");
        }
    }

    @Override
    public JSONElement getEmbeddedData() {
        if (download) {
            loadContent();
            return super.getEmbeddedData();
        } else {
            return new JSONElement(new JSONObject());
        }
    }

    @Override
    public JSONElement queryEmbeddedData(String pointer) {
        if (download) {
            loadContent();
            return super.queryEmbeddedData(pointer);
        } else {
            return new JSONElement(new JSONObject());
        }
    }

    public boolean isDownload() {
        return this.download;
    }
}
