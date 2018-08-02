package com.eden.orchid.api.options;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class OrchidFlags {

    static OrchidFlags instance;

    public static OrchidFlags getInstance() {
        if (instance == null) {
            List<OrchidFlag> orchidFlags = new ArrayList<>();

            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchSubclassesOf(OrchidFlag.class, (matchingClass) -> {
                try {
                    OrchidFlag option = matchingClass.newInstance();
                    if (option != null) {
                        orchidFlags.add(option);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanner.scan();
            instance = new OrchidFlags(orchidFlags);
        }
        return instance;
    }

    @Getter
    private Collection<OrchidFlag> flags;

    private Map<String, OrchidFlag.Value> parsedFlagsData;

    OrchidFlags(Collection<OrchidFlag> flags) {
        this.flags = flags;
    }

    public AbstractModule parseFlags(String[] args) {
        Map<String, Object> flagsMap = OrchidUtils.parseCommandLineArgs(args);
        parsedFlagsData = new HashMap<>();

        for (OrchidFlag flag : flags) {
            DefaultExtractor.getInstance().extractOptions(flag, flagsMap);
        }

        return new AbstractModule() {
            @Override
            protected void configure() {
                for (OrchidFlag flag : flags) {
                    for (OrchidFlag.Value entry : flag.getParsedFlags().values()) {
                        parsedFlagsData.put(entry.getKey(), entry);
                        AnnotatedBindingBuilder binder = bind(entry.getType());
                        binder.annotatedWith(Names.named(entry.getKey())).toInstance(entry.getValue());
                    }
                }
            }
        };
    }

    public String printFlags() {
        return parsedFlagsData
                .values()
                .stream()
                .sorted(Comparator.comparing(OrchidFlag.Value::getKey))
                .map(entry -> {
                    if(entry.getType().equals(String.class) && EdenUtils.isEmpty((String) entry.getValue())) {
                        return "";
                    }
                    if(entry.isProtected()) {
                        return "-" + entry.getKey() + ": [HIDDEN]";
                    }

                    return "-" + entry.getKey() + ": " + entry.getValue();
                })
                .filter(it -> !EdenUtils.isEmpty(it))
                .collect(Collectors.joining("\n"));
    }

    public <T> T getFlagValue(String key) {
        for (OrchidFlag flag : flags) {
            for (OrchidFlag.Value entry : flag.getParsedFlags().values()) {
                if(entry.getKey().equals(key)) {
                    return (T) entry.getValue();
                }
            }
        }

        return null;
    }

}
