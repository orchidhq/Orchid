package com.eden.orchid.impl.compilers;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class FrontMatterPrecompiler extends OrchidPreCompiler {

    @Inject
    public FrontMatterPrecompiler() {
        this.priority = 100;
    }

    @Override
    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        EdenPair<JSONObject, Integer> frontMatter = parseFrontMatter(input);

        String result;
        if(frontMatter.second != 0) {
            JSONObject root = new JSONObject(getContext().getRoot().toMap());

            for (String key : frontMatter.first.keySet()) {
                root.put(key, frontMatter.first.get(key));
            }

            result = getTheme().compile("twig", input.substring(frontMatter.second), root);
        }
        else {
            result = input;
        }

        return new EdenPair<>(result, new JSONElement(frontMatter.first));
    }

    private EdenPair<JSONObject, Integer> parseFrontMatter(String input) {
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

                return new EdenPair<>(frontMatter, contentStart);
            }
        }

        return new EdenPair<>(null, 0);
    }

}

