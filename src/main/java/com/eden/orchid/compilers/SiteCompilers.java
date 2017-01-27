package com.eden.orchid.compilers;

import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.tools.javac.resources.compiler;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteCompilers implements RegistrationProvider {
    public static Map<Integer, Compiler> compilers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, PreCompiler> precompilers = new TreeMap<>(Collections.reverseOrder());


    @Override
    public void register(Object object) {
        if(object instanceof Compiler) {
            Compiler compiler = (Compiler) object;
            int priority = compiler.priority();
            while(compilers.containsKey(priority)) {
                priority--;
            }

            SiteCompilers.compilers.put(priority, compiler);
        }
        if(object instanceof PreCompiler) {
            PreCompiler preCompiler = (PreCompiler) object;
            int priority = preCompiler.priority();
            while(compilers.containsKey(priority)) {
                priority--;
            }

            SiteCompilers.precompilers.put(priority, preCompiler);
        }
    }
}
