package com.eden.orchid.impl.indexing;

import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
@Singleton
public final class OrchidRootInternalIndex extends OrchidInternalIndex {
    private Map<String, OrchidInternalIndex> allIndexedPages;

    public OrchidRootInternalIndex() {
        super("internal");
        this.allIndexedPages = new HashMap<>();
    }

    public void addChildIndex(String key, OrchidInternalIndex index) {
        allIndexedPages.put(key, index);
        List<OrchidPage> indexPages = index.getAllPages();

        for(OrchidPage page : indexPages) {
            this.addToIndex(page.getReference().getPath(), page);
        }
    }

    public List<OrchidPage> getGeneratorPages(String generator) {
        if(allIndexedPages.containsKey(generator)) {
            return allIndexedPages.get(generator).getAllPages();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public void addToIndex(String taxonomy, OrchidPage page) {
        super.addToIndex(this.ownKey + "/" + taxonomy, page);
    }

    @Override
    public List<OrchidPage> find(String taxonomy) {
        return super.find(this.ownKey + "/" + taxonomy);
    }

    public List<OrchidPage> find(String taxonomy, String generator) {
        return allIndexedPages.get(generator).find(generator + "/" + taxonomy);
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
