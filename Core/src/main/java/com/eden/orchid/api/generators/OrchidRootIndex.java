package com.eden.orchid.api.generators;

import com.eden.orchid.api.resources.OrchidPage;
import lombok.Data;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Singleton
public final class OrchidRootIndex extends OrchidIndex {
    private Map<String, OrchidIndex> allIndexedPages;

    public OrchidRootIndex() {
        super("files");
        this.allIndexedPages = new HashMap<>();
    }

    public void addPage(String generator, OrchidPage page) {
        allIndexedPages.putIfAbsent(generator, new OrchidIndex(generator));
        allIndexedPages.get(generator).addToTaxonomy(generator + "/" + page.getReference().getFullPath(), page);
        this.addToTaxonomy(page.getReference().getFullPath(), page);
    }

    public List<OrchidPage> getGeneratorPages(String generator) {
        return allIndexedPages.get(generator).getAllPages();
    }

    @Override
    public void addToTaxonomy(String taxonomy, OrchidPage page) {
        super.addToTaxonomy(this.ownKey + "/" + taxonomy, page);
    }

    @Override
    public List<OrchidPage> find(String taxonomy) {
        return super.find(this.ownKey + "/" + taxonomy);
    }

    public List<OrchidPage> find(String taxonomy, String generator) {
        return allIndexedPages.get(generator).find(generator + "/" + taxonomy);
    }
}
