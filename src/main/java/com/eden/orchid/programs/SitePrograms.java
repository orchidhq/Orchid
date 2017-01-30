package com.eden.orchid.programs;

import com.eden.orchid.utilities.RegistrationProvider;

import java.util.HashMap;
import java.util.Map;

public class SitePrograms implements RegistrationProvider {

    public static Map<String, Program> sitePrograms = new HashMap<>();
    public static String defaultProgram = "build";

    @Override
    public void register(Object object) {
        if(object instanceof Program) {
            Program program = (Program) object;

            sitePrograms.put(program.getName(), program);
        }
    }

    public static void runProgram(String programName) {
        if(sitePrograms.containsKey(programName)) {
            sitePrograms.get(programName).run();
        }
        else {
            sitePrograms.get(defaultProgram).run();
        }
    }
}
