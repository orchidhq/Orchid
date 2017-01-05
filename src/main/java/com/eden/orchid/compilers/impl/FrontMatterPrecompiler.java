package com.eden.orchid.compilers.impl;

import com.eden.orchid.Orchid;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.utilities.OrchidPair;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoRegister
public class FrontMatterPrecompiler implements PreCompiler {
    @Override
    public int priority() {
        return 100;
    }

    @Override
    public OrchidPair<String, JSONElement> getEmbeddedData(String input) {
        OrchidPair<JSONObject, Integer> frontMatter = parseFrontMatter(input);

        String result;
        if(frontMatter.second != 0) {
            JSONObject root = new JSONObject(Orchid.getRoot().toMap());

            for (String key : frontMatter.first.keySet()) {
                root.put(key, frontMatter.first.get(key));
            }

            result = Orchid.getTheme().compile("twig", input.substring(frontMatter.second), root);
        }
        else {
            result = input;
        }

        return new OrchidPair<>(result, new JSONElement(frontMatter.first));
    }

    private OrchidPair<JSONObject, Integer> parseFrontMatter(String input) {
        if(input.startsWith("---")) {
            Matcher m = Pattern.compile("^---$", Pattern.MULTILINE).matcher(input);

            int matches = 0;
            int fmStart = 0;
            int fmEnd = 0;

            int contentStart = 0;

            // if we find a match, get the group
            while (m.find()) {
                if (matches == 0) {
                    fmStart = m.end();
                    matches++;
                }
                else if (matches == 1) {
                    fmEnd = m.start();
                    contentStart = m.end();
                    matches++;
                    break;
                }
            }

            if (matches == 2) {
                String frontMatterYaml = input.substring(fmStart, fmEnd);

                JSONObject frontMatter = new JSONObject((Map<String, Object>) new Yaml().load(frontMatterYaml));

                return new OrchidPair<>(frontMatter, contentStart);
            }
        }

        return new OrchidPair<>(null, 0);
    }

}

