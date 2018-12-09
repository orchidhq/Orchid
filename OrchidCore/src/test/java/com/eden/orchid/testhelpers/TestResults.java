package com.eden.orchid.testhelpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TestResults {

    public final Map<String, TestRenderer.TestRenderedPage> renderedPageMap;

    @Getter
    public final boolean renderingSuccess;

    @Getter
    public final Throwable thrownException;

    @Override
    public String toString() {
        return "TestResults: " + (renderingSuccess ? "success" : "failure") + " with " + renderedPageMap.size() + " pages";
    }

    public String showResults() {
        if(renderedPageMap.size() == 0) {
            return "(empty site)";
        }
        else {
            return renderedPageMap
                    .keySet()
                    .stream()
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
    }

    public TestResults printResults() {
        System.out.println(showResults());
        return this;
    }
}
