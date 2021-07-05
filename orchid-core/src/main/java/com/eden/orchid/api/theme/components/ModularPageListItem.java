package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.Map;

public interface ModularPageListItem<L extends ModularPageList<L, I>, I extends ModularPageListItem<L, I>> extends ModularListItem<L, I> {

    boolean canBeUsedOnPage(OrchidPage containingPage, L modularList, List<Map<String, Object>> possibleItems, List<I> currentItems);

    void initialize(OrchidContext context, OrchidPage containingPage);

}
