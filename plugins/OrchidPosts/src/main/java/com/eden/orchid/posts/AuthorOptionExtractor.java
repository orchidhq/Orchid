package com.eden.orchid.posts;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 */
public final class AuthorOptionExtractor extends OptionExtractor<Author> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;
    private final PostsModel postsModel;

    @Inject
    public AuthorOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter, PostsModel postsModel) {
        super(1000);
        this.contextProvider = contextProvider;
        this.converter = converter;
        this.postsModel = postsModel;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(Author.class);
    }

    public String getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public Author getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {

            if(options.get(key) instanceof JSONObject) {
                Author author = new Author();
                author.extractOptions(contextProvider.get(), options.getJSONObject(key));
                return author;
            }
            else {
                EdenPair<Boolean, String> value = converter.convert(options.get(key));
                if(value.first) {
                    return postsModel.getAuthorByName(value.second);
                }
            }
        }

        return getDefaultValue(field);
    }

    @Override
    public Author getDefaultValue(Field field) {
        return null;
    }

    @Override
    public List<Author> getList(Field field, JSONObject options, String key) {
        return null;
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        return null;
    }
}
