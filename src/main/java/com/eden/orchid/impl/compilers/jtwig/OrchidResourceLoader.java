package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.AutoRegister;
import com.google.common.base.Optional;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
            if(path.trim().startsWith("[") && path.trim().endsWith("]")) {
                String[] templates = StringUtils.strip(path, "[]").split(",");

                for(String template : templates) {
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
            if(path.trim().startsWith("[") && path.trim().endsWith("]")) {
                String[] templates = StringUtils.strip(path, "[]").split(",");

                for(String template : templates) {
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
                resource = Orchid.getResources().getResourceEntry(path);
            }
            else {
                resource = Orchid.getResources().getResourceEntry("templates/" + path);
            }

            return (resource != null);
        }

        private InputStream loadTemplate(String path) {
            path = path.trim();

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
            else {
                return null;
            }
        }
    }
}
