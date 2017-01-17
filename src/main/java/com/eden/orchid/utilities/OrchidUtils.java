package com.eden.orchid.utilities;

import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.OrchidResources;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Collection;

public final class OrchidUtils {

    /**
     * Returns true if the string is null or empty. An empty string is defined to be either 0-length or all whitespace
     *
     * @param str the string to be examined
     * @return true if str is null or empty
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0 || str.toString().trim().length() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isEmpty(JSONElement str) {
        if (str != null && str.getElement().getClass().equals(String.class)) {
            return isEmpty((String) str.getElement());
        }
        else {
            return true;
        }
    }

    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean acceptsExtension(String sourceExt, String[] acceptedExts) {
        for(String ext : acceptedExts) {
            if(ext.equalsIgnoreCase(sourceExt)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isJsonAware(Object object) {
        if(object instanceof JSONObject) return true;
        if(object instanceof JSONArray)  return true;
        if(object instanceof String)     return true;
        if(object instanceof Byte)       return true;
        if(object instanceof Short)      return true;
        if(object instanceof Integer)    return true;
        if(object instanceof Long)       return true;
        if(object instanceof Double)     return true;
        if(object instanceof Float)      return true;
        if(object instanceof Boolean)    return true;

        return false;
    }

    public static String applyBaseUrl(String url) {
        String baseUrl = "";

        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        return baseUrl + File.separator + url;
    }

    public static String compileTemplate(String templateName, Object... data) {
        OrchidResource container = OrchidResources.getResourceEntry(templateName);

        if(container != null) {
            return Orchid.getTheme().compile(FilenameUtils.getExtension(templateName), container.getContent(), data);
        }

        return "";
    }
//
//    public static String compileLayout(String layoutName, Object... data) {
//        OrchidResource layout = OrchidResources.getResourceEntry("templates/layouts/" + layoutName);
//
//        if(layout != null) {
//            return Orchid.getTheme().compile(FilenameUtils.getExtension(layoutName), layout.getContent(), data);
//        }
//
//        return "";
//    }
//
//    public static void createPage(String outputPath, String fileName, String container, String layout, String inputExt, String inputContent, JSONObject data) {
//        JSONObject jsonData = new JSONObject(Orchid.getRoot().toMap());
//
//        for(String key : data.keySet()) {
//            jsonData.put(key, data.get(key));
//        }
//
//        inputContent = Orchid.getTheme().compile(inputExt, inputContent);
//        jsonData.put("content", inputContent);
//
//        inputContent = compileContainer(container, jsonData);
//        jsonData.put("content", inputContent);
//
//        inputContent = compileLayout(layout, jsonData);
//
//        OrchidResources.writeFile(outputPath, fileName, inputContent);
//    }
}
