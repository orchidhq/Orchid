package com.eden.orchid.api.indexing;

import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.util.List;

@Getter @Setter
@Singleton
public final class OrchidRootExternalIndex extends OrchidExternalIndex {

    public OrchidRootExternalIndex() {
        super("external");
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

    public void addChildIndex(OrchidIndex index) {
        for(OrchidPage page : index.getAllPages()) {
            this.addToIndex(page.getReference().getPath(), page);
        }
    }

    @Override
    public String toString() {
        return "root external index: " + this.ownKey;
    }
}
