package com.eden.orchid;

import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.PreCompiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class OrchidUtils {

    /**
     * Returns the jar file used to load class clazz, or null if clazz was not loaded from a jar.
     *
     * @param clazz  the class to load a jar from
     * @return  the JarFile for a given class, or null if the class was not loaded from a jar
     */
    private static JarFile jarForClass(Class<?> clazz) {
        String path = "/" + clazz.getName().replace('.', '/') + ".class";
        URL jarUrl = clazz.getResource(path);
        if (jarUrl == null) {
            return null;
        }

        String url = jarUrl.toString();
        int bang = url.indexOf("!");
        String JAR_URI_PREFIX = "jar:file:";
        if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
            try {
                return new JarFile(url.substring(JAR_URI_PREFIX.length(), bang));
            }
            catch (IOException e) {
                throw new IllegalStateException("Error loading jar file.", e);
            }
        }
        else {
            return null;
        }
    }

    public static JSONArray compileResources(String resourceDir, Compiler compiler) {
        JSONArray writtenFileNames = new JSONArray();

        JSONArray themeFiles = compileThemeResources(resourceDir, compiler);
        JSONArray externalFiles = compileExternalResources(resourceDir, compiler);

        String baseUrl = "";

        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        for(int i = 0; i < themeFiles.length(); i++) {
            writtenFileNames.put(baseUrl + File.separator +  themeFiles.get(i));
        }
        for(int i = 0; i < externalFiles.length(); i++) {
            writtenFileNames.put(baseUrl + File.separator + externalFiles.get(i));
        }

        return writtenFileNames;
    }

    private static JSONArray compileThemeResources(String resourceDir, Compiler compiler) {
        JSONArray writtenFileNames = new JSONArray();

        JarFile fromJar = jarForClass(Orchid.getTheme().getClass());

        for (Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().startsWith(resourceDir + File.separator) && !entry.isDirectory()) {

                if (FilenameUtils.isExtension(entry.getName(), compiler.getSourceExtensions())) {
                    String destFileName =
                            resourceDir
                                    + File.separator
                                    + FilenameUtils.removeExtension(entry.getName()).substring(resourceDir.length() + 1) // strip the jar resource path from the entry name
                                    + "."
                                    + compiler.getOutputExtension(); // replace the file extension

                    // Load the contents of the file
                    try {
                        String fileContents = IOUtils.toString(fromJar.getInputStream(entry), "UTF-8");

                        // Pass the file contents through the precompiler
                        PreCompiler preCompiler = SiteCompilers.getPrecompiler(Orchid.getTheme().getPrecompilerClass());

                        if (preCompiler != null) {
                            fileContents = preCompiler.compile(fileContents, new JSONObject().put("site", Orchid.query("options")));
                        }

                        fileContents = compiler.compile(FilenameUtils.getExtension(entry.getName()), fileContents);

                        // Write the file to the output destination
                        writeFile(destFileName, fileContents);
                        writtenFileNames.put(destFileName);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return writtenFileNames;
    }

    private static JSONArray compileExternalResources(String resourceDir, Compiler compiler) {
        JSONArray writtenFileNames = new JSONArray();

        String resDir = Orchid.query("options.resourcesDir") + File.separator + resourceDir;

        File res = new File(resDir);

        if (res.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(res, compiler.getSourceExtensions(), true));

            for (File file : files) {

                String destFileName =
                        resourceDir
                                + File.separator
                                + FilenameUtils.removeExtension(file.getName())
                                + "."
                                + compiler.getOutputExtension();

                try {
                    // Load the contents of the file
                    String fileContents = IOUtils.toString(new FileInputStream(file), "UTF-8");

                    // Pass the file contents through the precompiler
                    PreCompiler preCompiler = SiteCompilers.getPrecompiler(Orchid.getTheme().getPrecompilerClass());

                    if(preCompiler != null) {
                        fileContents = preCompiler.compile(fileContents, new JSONObject().put("site", Orchid.query("options")));
                    }

                    fileContents = compiler.compile(FilenameUtils.getExtension(file.getName()), fileContents);

                    // Write the file to the output destination
                    writeFile(destFileName, fileContents);
                    writtenFileNames.put(destFileName);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return writtenFileNames;
    }

    public static String getResourceFileContents(String fileName) {
        fileName = fileName.replaceAll("/", File.separator);
        // First attempt to load a file in the external resources directory
        if(!isEmpty(Orchid.query("options.resourcesDir"))) {
            File res = new File(Orchid.query("options.resourcesDir") + File.separator + fileName);

            if(res.exists() && !res.isDirectory()) {
                try {
                    return IOUtils.toString(new FileInputStream(res), "UTF-8");
                }
                catch(IOException e) {

                }
            }
        }

        // If we don't have the requested file in external resources, try to load the file from internal resources
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);
        try {
            return IOUtils.toString(is, "UTF-8");
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // We couldn't find the requested file
        return null;
    }

    public static void writeFile(String dest, String contents) {
        Path file = Paths.get(Orchid.query("options.d") + "/" + dest);
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, contents.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static JSONObject createClassDocJson(ClassDoc classDoc) {
        JSONObject classInfoJson = new JSONObject();
        classInfoJson.put("simpleName", classDoc.name());
        classInfoJson.put("name", classDoc.qualifiedName());
        classInfoJson.put("package", classDoc.containingPackage().name());
        classInfoJson.put("url", classDoc.name() + ".html");

        classInfoJson.put("methods", new JSONArray());

        for(MethodDoc methodDoc : classDoc.methods()) {
            JSONObject methodDocJson = new JSONObject();

            String visibility = "";
            if(methodDoc.isPublic())              visibility = "public";
            else if(methodDoc.isProtected())      visibility = "protected";
            else if(methodDoc.isPackagePrivate()) visibility = "package-private";
            else if(methodDoc.isPrivate())        visibility = "private";

            methodDocJson.put("visibility", visibility);
            methodDocJson.put("static", methodDoc.isStatic());
            methodDocJson.put("abstract", methodDoc.isAbstract());
            methodDocJson.put("synchronized", methodDoc.isSynchronized());
            methodDocJson.put("final", methodDoc.isFinal());
            methodDocJson.put("returns", new JSONObject());

            methodDocJson.getJSONObject("returns").put("simpleName", methodDoc.returnType().simpleTypeName());
            methodDocJson.getJSONObject("returns").put("name", methodDoc.returnType().qualifiedTypeName());
            methodDocJson.getJSONObject("returns").put("dimension", methodDoc.returnType().dimension());

            methodDocJson.put("name", methodDoc.name());


            methodDocJson.put("parameters", new JSONArray());

            for(Parameter parameter : methodDoc.parameters()) {
                JSONObject parameterJson = new JSONObject();

                parameterJson.put("simpleName", parameter.type().simpleTypeName());
                parameterJson.put("name", parameter.type().qualifiedTypeName());
                parameterJson.put("dimension", parameter.type().dimension());
                parameterJson.put("variableName", parameter.typeName());

                methodDocJson.getJSONArray("parameters").put(parameterJson);
            }

            classInfoJson.getJSONArray("methods").put(methodDocJson);
        }

        return classInfoJson;
    }
}
