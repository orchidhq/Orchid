package com.eden.orchid.impl.compilers.sass;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.compilers.OrchidCompiler;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public final class SassCompiler extends OrchidCompiler {

    private final SassImporter importer;
    private final Pattern previousContextRegex = Pattern.compile("^//\\s*?CONTEXT=(.*?)$", Pattern.MULTILINE);

    public enum CompilerSyntax {
        SASS("sass"), SCSS("scss"), UNSPECIFIED("");

        public final String ext;
        CompilerSyntax(String ext) {
            this.ext = ext;
        }
    }

    @Inject
    public SassCompiler(SassImporter importer) {
        super(800);
        this.importer = importer;
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{CompilerSyntax.SCSS.ext, CompilerSyntax.SASS.ext};
    }

    @Override
    public String getOutputExtension() {
        return "css";
    }

    @Override
    public String compile(String extension, String input, Map<String, Object> data) {
        Options options = new Options();
        options.getImporters().add(importer);

        String sourceContext = "";

        Matcher m = previousContextRegex.matcher(input);
        if(m.find()) {
            sourceContext = m.group(1);
        }

        if (extension.equals(CompilerSyntax.SASS.ext)) {
            options.setIsIndentedSyntaxSrc(true);
        }
        else {
            options.setIsIndentedSyntaxSrc(false);
        }

        try {
            if(EdenUtils.isEmpty(sourceContext)) {
                return new Compiler().compileString(input, options).getCss();
            }
            else {
                return new Compiler().compileString(input, new URI(sourceContext), new URI(sourceContext), options).getCss();
            }
        }
        catch (Exception e) {
            Clog.e("error compiling .{} content", e, extension);
            return "";
        }
    }
}
