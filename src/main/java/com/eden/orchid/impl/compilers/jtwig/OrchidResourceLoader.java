package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.AutoRegister;
import com.google.common.base.Optional;
import org.apache.commons.io.IOUtils;
import org.jtwig.resource.loader.ResourceLoader;
import org.jtwig.resource.loader.TypedResourceLoader;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@AutoRegister
public class OrchidResourceLoader extends TypedResourceLoader {

    public OrchidResourceLoader() {
        super("orchid", new Loader());
    }

    private static class Loader implements ResourceLoader {
        @Override
        public Optional<Charset> getCharset(String path) {
            return Optional.absent();
        }

        @Override
        public InputStream load(String path) {
            OrchidResource resource;

            if(path.startsWith("/")) {
                resource = Orchid.getResources().getResourceEntry(path);
            }
            else {
                resource = Orchid.getResources().getResourceEntry("templates/" + path);
            }

            if(resource != null) {
                String content = resource.getContent();
                return IOUtils.toInputStream(content);
            }

            return null;
        }

        @Override
        public boolean exists(String path) {
            OrchidResource resource;

            if(path.startsWith("/")) {
                resource = Orchid.getResources().getResourceEntry(path);
            }
            else {
                resource = Orchid.getResources().getResourceEntry("templates/" + path);
            }

            return (resource != null);
        }

        @Override
        public Optional<URL> toUrl(String path) {
            return Optional.absent();
        }
    }
}
