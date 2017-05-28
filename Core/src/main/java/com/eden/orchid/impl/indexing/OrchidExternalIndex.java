package com.eden.orchid.impl.indexing;

import com.eden.orchid.api.indexing.OrchidIndex;

public class OrchidExternalIndex extends OrchidIndex {
    public OrchidExternalIndex(String ownKey) {
        super(ownKey);
    }

    @Override
    public Class<? extends OrchidIndex> childIndexClass() {
        return OrchidExternalIndex.class;
    }
}
