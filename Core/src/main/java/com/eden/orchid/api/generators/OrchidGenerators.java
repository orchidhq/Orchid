package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.utilities.ObservableTreeSet;
import lombok.Data;
import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Singleton
public final class OrchidGenerators {
    private JSONArray disabledGenerators;
    private Set<OrchidGenerator> generators;

    private OrchidContext context;
    private OrchidRootIndex index;

    @Inject
    public OrchidGenerators(OrchidContext context, Set<OrchidGenerator> generators, OrchidRootIndex index) {
        this.context = context;
        this.generators = new ObservableTreeSet<>(generators);
        this.index = index;
    }

    public void startIndexing() {
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .forEach(this::indexGenerator);

//        Clog.v("We have #{$1} css pages", this.index.find("assets/css").size());
//        Clog.v("We have #{$1} js pages", this.index.find("assets/js").size());
//        Clog.v("We have #{$1} svg pages", this.index.find("assets/svg").size());
//        Clog.v("We have #{$1} assets pages", this.index.find("assets").size());

        Clog.v("We have #{$1} 'pages' pages", this.index.find("pages").size());
        Clog.v("We have #{$1} 'pages/blns' pages", this.index.find("pages/blns").size());

        Clog.v("We have #{$1} pages total", this.index.getAllPages().size());
    }

    public void startGeneration() {
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .forEach(this::useGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        List<? extends OrchidPage> generatorPages = generator.startIndexing();
        if (!EdenUtils.isEmpty(generator.getName()) && generatorPages != null && generatorPages.size() > 0) {
            for(OrchidPage page : generatorPages) {
                index.addPage(generator.getName(), page);
                if(page.getResource() instanceof FreeableResource) {
                    ((FreeableResource) page.getResource()).free();
                }
            }
        }
    }

    private void useGenerator(OrchidGenerator generator) {
        Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        List<? extends OrchidPage> generatorPages = null;
        if(!EdenUtils.isEmpty(generator.getName())) {
            generatorPages = index.getGeneratorPages(generator.getName());
        }
        if(generatorPages == null) {
            generatorPages = new ArrayList<>();
        }
        generator.startGeneration(generatorPages);
    }

    public boolean shouldUseGenerator(OrchidGenerator generator) {
        if(disabledGenerators == null) {
            JSONElement el = context.query("options.disabledGenerators");
            if(el != null) {
                if(el.getElement() instanceof JSONArray) {
                    disabledGenerators = (JSONArray) el.getElement();
                }
            }
        }

        if(disabledGenerators != null) {
            for(int i = 0; i < disabledGenerators.length(); i++) {
                if(disabledGenerators.getString(i).equalsIgnoreCase(generator.getClass().getName())) {
                    return false;
                }
                else if(disabledGenerators.getString(i).equalsIgnoreCase(generator.getClass().getSimpleName())) {
                    return false;
                }
            }
        }

        return true;
    }
}
