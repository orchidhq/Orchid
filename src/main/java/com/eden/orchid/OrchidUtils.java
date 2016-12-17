package com.eden.orchid;

import com.eden.orchid.compilers.AssetCompiler;
import com.eden.orchid.options.SiteOptions;
import com.sun.javadoc.RootDoc;
import liqp.Template;
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
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidUtils {
    /**
     * Returns the jar file used to load class clazz, or defaultJar if clazz was not loaded from a
     * jar.
     */
    public static JarFile jarForClass(Class<?> clazz, JarFile defaultJar) {
        String path = "/" + clazz.getName().replace('.', '/') + ".class";
        URL jarUrl = clazz.getResource(path);
        if (jarUrl == null) {
            return defaultJar;
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
            return defaultJar;
        }
    }

    public static JSONArray copyAndCompileResourcesToDirectory(JarFile fromJar, AssetCompiler compiler) throws IOException {
        JSONArray writtenFileNames = new JSONArray();

        for (Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().startsWith(compiler.getSourceDir() + File.separator) && !entry.isDirectory()) {

                if (FilenameUtils.isExtension(entry.getName(), compiler.getSourceExtensions())) {
                    String destFileName =
                            compiler.getDestDir()
                                    + File.separator
                                    + FilenameUtils.removeExtension(entry.getName()).substring(compiler.getSourceDir().length() + 1) // strip the jar resource path from the entry name
                                    + "."
                                    + compiler.getDestExtension(); // replace the file extension

                    // Load the contents of the file
                    String fileContents = IOUtils.toString(fromJar.getInputStream(entry), "UTF-8");

                    // Pass the file contents through Liquid with the parsed site options
                    String liquifiedFileContents = Template
                            .parse(fileContents)
                            .render(new JSONObject().put("site", SiteOptions.siteOptions).toString(2));

                    // Send the Liquified file contents through the custom assetCompilers
                    String output = compiler.compile(FilenameUtils.getExtension(entry.getName()), liquifiedFileContents);

                    // Write the file to the output destination
                    writeFile(destFileName, output);
                    writtenFileNames.put(destFileName);
                }
            }
        }

        return writtenFileNames;
    }

    public static JSONArray copyAndCompileExternalResourcesToDirectory(RootDoc root, AssetCompiler compiler) throws IOException {
        JSONArray writtenFileNames = new JSONArray();

        String resDir = SiteOptions.siteOptions.getString("resourcesDir") + File.separator + compiler.getSourceDir();

        File res = new File(resDir);

        if (res.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(res, compiler.getSourceExtensions(), true));

            for (File file : files) {

                String destFileName =
                        compiler.getDestDir()
                                + File.separator
                                + FilenameUtils.removeExtension(file.getName())
                                + "."
                                + compiler.getDestExtension();

                // Load the contents of the file
                String fileContents = IOUtils.toString(new FileInputStream(file), "UTF-8");

                // Pass the file contents through Liquid with the parsed site options
                String liquifiedFileContents = Template
                        .parse(fileContents)
                        .render(new JSONObject().put("site", SiteOptions.siteOptions).toString(2));

                // Send the Liquified file contents through the custom assetCompilers
                String output = compiler.compile(FilenameUtils.getExtension(file.getName()), liquifiedFileContents);

                // Write the file to the output destination
                writeFile(destFileName, output);
                writtenFileNames.put(destFileName);
            }
        }


        return writtenFileNames;
    }

    public static String getResourceFileContents(String fileName) {
        fileName = fileName.replaceAll("/", File.separator);
        // First attempt to load a file in the external resources directory
        if(!isEmpty(SiteOptions.siteOptions.getString("resourcesDir"))) {
            File res = new File(SiteOptions.siteOptions.getString("resourcesDir") + File.separator + fileName);

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
        Path file = Paths.get(SiteOptions.outputDir + "/" + dest);
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
}
