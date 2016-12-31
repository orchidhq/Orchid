package com.eden.orchid.compilers;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteCompilers {

    // Generic compilers to be dynamically used based on file extension
    public static Map<Integer, Compiler> compilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, ContentCompiler> contentCompilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, PreCompiler> precompilers = new TreeMap<>(Collections.reverseOrder());
}
