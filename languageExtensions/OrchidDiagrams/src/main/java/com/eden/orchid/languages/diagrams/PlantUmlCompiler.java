package com.eden.orchid.languages.diagrams;

import com.eden.orchid.api.compilers.OrchidCompiler;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Singleton
public class PlantUmlCompiler extends OrchidCompiler {

    @Inject
    public PlantUmlCompiler() {
        super(800);
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        try {
            try {
                // ensure string is wrapped in @startuml...@enduml
                source = source.trim();
                if(!source.startsWith("@startuml")) {
                    source = "@startuml\n" + source;
                }
                if(!source.endsWith("@enduml")) {
                    source = source + "\n@enduml";
                }

                // compile string to SVG
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                String desc = new SourceStringReader(source).generateImage(os, new FileFormatOption(FileFormat.SVG));
                os.close();
                String s = "<div>";
                s += new String(os.toByteArray(), Charset.forName("UTF-8"));
                s += "</div>";
                return s;
            }
            catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getOutputExtension() {
        return "svg";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"uml"};
    }
}
