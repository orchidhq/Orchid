package com.eden.orchid.languages.impl;

import com.eden.orchid.api.compilers.OrchidCompiler;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AsciiDoctorCompiler extends OrchidCompiler {

    private Asciidoctor asciidoctor;
    private Options options;

    @Inject
    public AsciiDoctorCompiler() {
        super(800);
        asciidoctor = Asciidoctor.Factory.create();
        options = new Options();
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        return asciidoctor.convert(source, options);
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"ad", "adoc", "asciidoc", "asciidoctor"};
    }
}
