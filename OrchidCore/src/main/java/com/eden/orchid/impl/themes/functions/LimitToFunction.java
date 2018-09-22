package com.eden.orchid.impl.themes.functions;

import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Description(value = "Trim a String, array, or Iterable down to size.", name = "Limit-To")
public final class LimitToFunction extends TemplateFunction {

    @Option
    @Description("A String to limit the length of.")
    private Object input;

    @Option
    @Description("The length to limit.")
    private int count;

    @Inject
    public LimitToFunction() {
        super("limitTo", false);
    }

    @Override
    public String[] parameters() {
        return new String[] {"input", "count"};
    }

    @Override
    public Object apply() {
        if(count == 0) {
            throw new IllegalArgumentException("Count must be given.");
        }

        if(input != null) {
            if (input instanceof Iterable) {
                Iterable originalItems = (Iterable) input;
                List<Object> limitedItems = new ArrayList<>();
                int i = 0;
                for (Object o : originalItems) {
                    if (i >= count) break;
                    limitedItems.add(o);
                    i++;
                }
                return limitedItems;
            }
            else if (input.getClass().isArray()) {
                Object[] originalItems = (Object[]) input;
                List<Object> limitedItems = new ArrayList<>();
                int i = 0;
                for (Object o : originalItems) {
                    if (i >= count) break;
                    limitedItems.add(o);
                    i++;
                }
                return limitedItems;
            }
            else {
                String actualInput = (input instanceof String) ? (String) input : this.input.toString();

                return actualInput.substring(0, count);
            }
        }
        return "";
    }

}
