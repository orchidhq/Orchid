package com.eden.orchid.impl.indexing;

import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.inject.Singleton;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Singleton
public final class OrchidRootExternalIndex extends OrchidExternalIndex {

    public OrchidRootExternalIndex() {
        super("external");
    }

    public void addChildIndex(OrchidIndex index) {
        List<OrchidPage> indexPages = index.getAllPages();

        for(OrchidPage page : indexPages) {
            this.addToIndex(page.getReference().getPath(), page);
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

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
