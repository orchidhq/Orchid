package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class OrchidOptions {

    private OrchidContext context;
    private Set<OrchidGenerator> generators;
    private Set<OrchidParser> parsers;
    private OrchidResources resources;
    private String environment;

    private String[] dataParserExtensions;
    private String[] formats = new String[] {"config-#{$1}.#{$2}", "config.#{$2}"};


    @Inject
    public OrchidOptions(
            OrchidContext context,
            Set<OrchidGenerator> generators,
            Set<OrchidParser> parsers,
            OrchidResources resources) {
        this.context = context;
        this.generators = generators;
        this.parsers = parsers;
        this.resources = resources;
        this.environment = OrchidFlags.getInstance().getString("environment");
    }

    public JSONObject loadOptions() {
        dataParserExtensions = parsers
                .stream()
                .map(OrchidParser::getSourceExtensions)
                .flatMap(Stream::of)
                .toArray(String[]::new);

        JSONObject options = new JSONObject();
        options.put("config", loadConfigFile());
        options.put("data", loadDatafiles());

        generators
                .stream()
                .forEach(generator -> options.put(generator.getKey(), loadGeneratorOptions(generator)));

//        Clog.v(options.toString(2));

        return options;
    }

    public JSONObject loadConfigFile() {
        return Arrays
                .stream(dataParserExtensions)
                .map(ext ->
                        Arrays.stream(formats)
                              .map(format -> resources.getLocalResourceEntry(Clog.format(format, environment, ext)))
                              .filter(Objects::nonNull)
                              .map(OrchidResource::getContent)
                              .filter(OrchidUtils.not(EdenUtils::isEmpty))
                              .map(content -> context.parse(ext, content))
                              .findFirst()
                              .orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(JSONObject::new);
    }

    public JSONObject loadDatafiles() {
        List<OrchidResource> files = resources.getLocalResourceEntries("data", dataParserExtensions, false);

        JSONObject allDatafiles = new JSONObject();

        for(OrchidResource file : files) {
            JSONObject fileData = context.parse(file.getReference().getExtension(), file.getContent());
            allDatafiles.put(file.getReference().getFileName(), fileData);
        }

        return allDatafiles;
    }

    public JSONObject loadGeneratorOptions(OrchidGenerator generator) {
        return new JSONObject();
    }
}
