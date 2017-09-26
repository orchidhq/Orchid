package com.eden.orchid.impl.indexing;

import com.eden.orchid.api.indexing.OrchidIndex;

public class OrchidInternalIndex extends OrchidIndex {

    public OrchidInternalIndex(String ownKey) {
        super(ownKey);
    }

    @Override
    public Class<? extends OrchidIndex> childIndexClass() {
        return OrchidInternalIndex.class;
    }

}
