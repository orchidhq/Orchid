package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.google.common.base.Optional;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.resource.loader.ResourceLoader;
import org.jtwig.resource.loader.TypedResourceLoader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@Singleton
public class JTwigResourceLoader extends TypedResourceLoader {

    @Inject
    public JTwigResourceLoader(Provider<OrchidResources> resources) {
        super("orchid", new Loader(resources));
    }

    private static class Loader implements ResourceLoader {

        private Provider<OrchidResources> resources;

        public Loader(Provider<OrchidResources> resources) {
            this.resources = resources;
        }

        @Override
        public Optional<Charset> getCharset(String path) {
            return Optional.absent();
        }

        @Override
        public InputStream load(String path) {
            if(isMultiPath(path)) {
                for(String template : getMultiPaths(path)) {
                    if(templateExists(template)) {
                        return loadTemplate(template);
                    }
                }

                return IOUtils.toInputStream("");
            }
            else {
                return loadTemplate(path);
            }
        }

        @Override
        public boolean exists(String path) {
            if(isMultiPath(path)) {
                for(String template : getMultiPaths(path)) {
                    if(templateExists(template)) {
                        return true;
                    }
                }

                return false;
            }
            else {
                return templateExists(path);
            }
        }

        @Override
        public Optional<URL> toUrl(String path) {
            return Optional.absent();
        }

        private boolean templateExists(String path) {
            path = path.trim();

            OrchidResource resource;

            if(path.startsWith("/")) {
                resource = resources.get().getResourceEntry(path);
            }
            else {
                resource = resources.get().getResourceEntry("templates/" + path);
            }

            return (resource != null);
        }

        private InputStream loadTemplate(String path) {
            path = path.trim();

            OrchidResource resource;

            if(path.startsWith("/")) {
                resource = resources.get().getResourceEntry(path);
            }
            else {
                resource = resources.get().getResourceEntry("templates/" + path);
            }

            if(resource != null) {
                String content = resource.getContent();
                return IOUtils.toInputStream(content);
            }
            else {
                return null;
            }
        }

        private boolean isMultiPath(String path) {
            return (path.trim().startsWith("[") && path.trim().endsWith("]"));
        }

        private String[] getMultiPaths(String path) {
            return StringUtils.strip(path, "[]").split(",");
        }
    }
}
