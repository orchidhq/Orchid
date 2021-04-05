package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;
import com.jakewharton.picnic.Table;

@ImplementedBy(GeneratorServiceImpl.class)
public interface GeneratorService extends OrchidService {

    default void startIndexing() {
        getService(GeneratorService.class).startIndexing();
    }

    default void startGeneration() {
        getService(GeneratorService.class).startGeneration();
    }

    default void onPageGenerated(OrchidPage page, long millis) {
        getService(GeneratorService.class).onPageGenerated(page, millis);
    }

    default String getBuildSummary() {
        return getService(GeneratorService.class).getBuildSummary();
    }

    default Table getBuildDetail() {
        return getService(GeneratorService.class).getBuildDetail();
    }

    /**
     * Get the keys corresponding to a set of generators that are whitelisted and blacklisted. This filtering is done
     * on top of the normal filtering that a user can specify themselves to globally enable/disable generators.
     *
     * @param include whitelisted generator keys
     * @param exclude blacklisted generator keys
     * @return the keys corresponding to those generators that pass the whitelist and the blacklist and aren't globally
     *  enabled or disabled
     */
    default String[] getGeneratorKeys(String[] include, String[] exclude) {
        return getService(GeneratorService.class).getGeneratorKeys(include, exclude);
    }

}
