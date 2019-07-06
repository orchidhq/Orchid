package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.eden.orchid.api.generators.OrchidGeneratorKt.modelOf;

public class MockGenerator extends OrchidGenerator<OrchidGenerator.Model> {

    List<? extends OrchidPage> mockPages;
    List<? extends OrchidPage> generatedPages;

    public MockGenerator(String key, int priority, List<? extends OrchidPage> mockPages) {
        super(key, priority);
        this.mockPages = mockPages;
    }

    @NotNull
    @Override
    public Model startIndexing(@NotNull OrchidContext context) {
        return modelOf(this, ()->mockPages);
    }

    @Override
    public void startGeneration(@NotNull OrchidContext context, Model model) {
        generatedPages = model.getAllPages();
    }
}
