package com.eden.orchid.compilers;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteCompilers {
    public static Map<Integer, Compiler> compilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, PreCompiler> precompilers = new TreeMap<>(Collections.reverseOrder());

    public static void registerCompiler(Compiler compiler) {
        int priority = compiler.priority();
        while(compilers.containsKey(priority)) {
            priority--;
        }

        SiteCompilers.compilers.put(priority, compiler);
    }

    public static void registerPreCompiler(PreCompiler compiler) {
        int priority = compiler.priority();
        while(compilers.containsKey(priority)) {
            priority--;
        }

        SiteCompilers.precompilers.put(priority, compiler);
    }
}
