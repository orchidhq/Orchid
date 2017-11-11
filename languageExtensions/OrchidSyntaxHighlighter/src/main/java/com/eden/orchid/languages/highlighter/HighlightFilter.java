package com.eden.orchid.languages.highlighter;

import com.eden.common.util.EdenUtils;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.python.util.PythonInterpreter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HighlightFilter implements JtwigFunction {

    @Override
    public String name() {
        return "highlight";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {
        List<Object> fnParams = request
                .minimumNumberOfArguments(1)
                .maximumNumberOfArguments(2)
                .getArguments();

        if(fnParams.size() == 1) {
            String input = fnParams.get(0).toString();
            return apply(input, null);
        }
        else if(fnParams.size() == 2) {
            String input = fnParams.get(0).toString();
            String language = fnParams.get(1).toString();
            return apply(input, language);
        }
        else {
            return null;
        }
    }

    public Object apply(String input, String language) {
        PythonInterpreter interpreter = new PythonInterpreter();

        String pythonScript =
                "from pygments import highlight\n"
                + "from pygments.lexers import get_lexer_by_name\n"
                + "from pygments.formatters import HtmlFormatter\n"
                + "\n"
                + "lexer = get_lexer_by_name(codeLanguage, stripall=True)\n"
                + "formatter = HtmlFormatter(linenos=True)\n"
                + "\nresult = highlight(code, lexer, formatter)";

        // Set a variable with the content you want to work with
        interpreter.set("code", input);
        interpreter.set("codeLanguage", ((!EdenUtils.isEmpty(language)) ? language : "java"));

        // Simple use Pygments as you would in Python
        interpreter.exec(pythonScript);

        return interpreter.get("result", String.class);
    }
}