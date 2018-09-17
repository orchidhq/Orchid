package com.eden.orchid.api.indexing;

import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
@Singleton
public final class OrchidRootIndex extends OrchidIndex {
    private Map<String, OrchidIndex> allIndexedPages;

    public OrchidRootIndex(String ownKey) {
        super(null, ownKey);
        this.allIndexedPages = new HashMap<>();
    }

    public void addChildIndex(String key, OrchidIndex index) {
        allIndexedPages.put(key, index);
        for(OrchidPage page : index.getAllPages()) {
            this.addToIndex(page.getReference().getPath(), page);
        }
    }

    public List<OrchidPage> getChildIndex(String generator) {
        if(allIndexedPages.containsKey(generator)) {
            return allIndexedPages.get(generator).getAllPages();
        }
        else {
            return new ArrayList<>();
        }
    }

    public List<OrchidPage> getChildIndices(String[] generators) {
        return Arrays
                .stream(generators)
                .flatMap(it -> getChildIndex(it).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<OrchidPage> getAllPages() {
        return allIndexedPages
                .values()
                .stream()
                .flatMap(it -> it.getAllPages().stream())
                .collect(Collectors.toList());
    }

    @Override
    public void addToIndex(String taxonomy, OrchidPage page) {
        super.addToIndex(this.ownKey + "/" + taxonomy, page);
    }

    @Override
    public List<OrchidPage> find(String taxonomy) {
        return super.find(this.ownKey + "/" + taxonomy);
    }

    @Override
    public OrchidPage findPage(String taxonomy) {
        return super.findPage(this.ownKey + "/" + taxonomy);
    }

    @Override
    public OrchidIndex findIndex(String taxonomy) {
        return super.findIndex(this.ownKey + "/" + taxonomy);
    }

    public List<OrchidPage> find(String taxonomy, String childKey) {
        return allIndexedPages.get(childKey).find(childKey + "/" + taxonomy);
    }

    @Override
    public String toString() {
        return "root " + super.toString();
    }
}
