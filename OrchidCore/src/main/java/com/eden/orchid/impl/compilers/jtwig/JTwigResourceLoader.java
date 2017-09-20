package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.google.common.base.Optional;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
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
    public JTwigResourceLoader(Provider<OrchidContext> resources) {
        super("orchid", new Loader(resources));
    }

    private static class Loader implements ResourceLoader {

        private Provider<OrchidContext> contextProvider;

        public Loader(Provider<OrchidContext> contextProvider) {
            this.contextProvider = contextProvider;
        }

        @Override
        public Optional<Charset> getCharset(String path) {
            return Optional.absent();
        }

        @Override
        public Optional<URL> toUrl(String path) {
            return Optional.absent();
        }

        @Override
        public InputStream load(String path) {
            path = path.trim();

            OrchidResource resource;

            if(!path.startsWith("templates")) {
                resource = contextProvider.get().getResourceEntry("templates/" + path);
            }
            else {
                resource = contextProvider.get().getResourceEntry(path);
            }

            if(resource != null) {
                String content = resource.getContent();
                return IOUtils.toInputStream(content, Charset.defaultCharset());
            }
            else {
                return null;
            }
        }

        @Override
        public boolean exists(String path) {
            path = path.trim();

            OrchidResource resource;

            if(!path.startsWith("templates/")) {
                resource = contextProvider.get().getResourceEntry("templates/" + path);
            }
            else {
                resource = contextProvider.get().getResourceEntry(path);
            }

            return (resource != null);
        }

    }
}
