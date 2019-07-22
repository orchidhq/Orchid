package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.OrchidModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import io.github.classgraph.ClassGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
            new ClassGraph().enableClassInfo().scan().getSubclasses(OrchidFlag.class.getName()).loadClasses(OrchidFlag.class).forEach(matchingClass -> {
                try {
                    OrchidFlag option = matchingClass.newInstance();
                    if (option != null) {
                        orchidFlags.add(option);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            instance = new OrchidFlags(orchidFlags);
        }
        return instance;
    }

    private Collection<OrchidFlag> flags;
    private Map<String, OrchidFlag.Value> parsedFlagsData;

    OrchidFlags(Collection<OrchidFlag> flags) {
        this.flags = flags;
    }

    public Map<String, String> getFlagNames() {
        Map<String, String> allFlags = new HashMap<>();
        for (OrchidFlag flag : flags) {
            flag.loadFlagNames((key, aliases) -> {
                addFlag(allFlags, key, key);
            });
        }
        return allFlags;
    }

    public Map<String, OrchidFlag.FlagDescription> describeFlags() {
        Map<String, OrchidFlag.FlagDescription> allFlags = new HashMap<>();
        for (OrchidFlag flag : flags) {
            allFlags.putAll(flag.describeFlags());
        }
        return allFlags;
    }

    public Map<String, String> getFlagAliases() {
        Map<String, String> allFlags = new HashMap<>();
        for (OrchidFlag flag : flags) {
            flag.loadFlagNames((key, aliases) -> {
                if (!EdenUtils.isEmpty(aliases)) {
                    for (String alias : aliases) {
                        addFlag(allFlags, key, alias);
                    }
                }
            });
        }
        return allFlags;
    }

    public List<String> getPositionalFlags() {
        return Collections.singletonList("task");
    }

    private void addFlag(Map<String, String> allFlags, String key, String alias) {
        if (allFlags.containsKey(alias)) {
            throw new IllegalArgumentException(Clog.format("A flag with key {} already exists! Currently mapping {}, intended mapping {}.", alias, allFlags.get(alias), key));
        }
        allFlags.put(alias, key);
    }

    public OrchidModule parseFlags(Map<String, Object> flagsMap) {
        parsedFlagsData = new HashMap<>();
        for (OrchidFlag flag : flags) {
            DefaultExtractor.getInstance().extractOptions(flag, flagsMap);
        }
        return new OrchidModule() {
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
        return parsedFlagsData.values().stream().sorted(Comparator.comparing(OrchidFlag.Value::getKey)).map(entry -> {
            if (entry.getType().equals(String.class) && EdenUtils.isEmpty((String) entry.getValue())) {
                return "";
            }
            if (entry.isProtected()) {
                return "-" + entry.getKey() + ": [HIDDEN]";
            }
            return "-" + entry.getKey() + ": " + entry.getValue();
        }).filter(it -> !EdenUtils.isEmpty(it)).collect(Collectors.joining("\n"));
    }

    public <T> T getFlagValue(String key) {
        for (OrchidFlag flag : flags) {
            for (OrchidFlag.Value entry : flag.getParsedFlags().values()) {
                if (entry.getKey().equals(key)) {
                    return (T) entry.getValue();
                }
            }
        }
        return null;
    }

    public Collection<OrchidFlag> getFlags() {
        return this.flags;
    }
}
