package com.eden.orchid.utilities;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FileLoader {

    private OrchidContext context;
    private OkHttpClient client;

    @Inject
    public FileLoader(OrchidContext context) {
        this.context = context;
        this.client = new OkHttpClient();
    }

    public JSONObject loadAdditionalFile(String url) {
        if(!EdenUtils.isEmpty(url) && url.trim().startsWith("file://")) {
            return loadLocalFile(url.replaceAll("file://", ""));
        }
        else {
            return loadRemoteFile(url);
        }
    }

    public JSONObject loadLocalFile(String url) {
        try {
            File file = new File(url);
            String s = IOUtils.toString(new FileInputStream(file));

            JSONElement el = context.getTheme().parse("json", s);
            if(OrchidUtils.elementIsObject(el)) {
                return (JSONObject) el.getElement();
            }
        }
        catch (FileNotFoundException e) {
            // ignore files not being found
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject loadRemoteFile(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                return new JSONObject(response.body().string());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public OrchidResource findClosestFile(String filename) {
        return findClosestFile(filename, false);
    }

    public OrchidResource findClosestFile(String filename, boolean strict) {
        return findClosestFile(filename, strict, 10);
    }

    public OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        if (!EdenUtils.isEmpty(context.query("options.resourcesDir"))) {
            String resourceDir = context.query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if(!strict) {
                            if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase(filename)) {
                                return new FileResource(context, file);
                            }
                        }
                        else {
                            if (file.getName().equals(filename)) {
                                return new FileResource(context, file);
                            }
                        }
                    }
                }

                // set the folder to its own parent and search again
                if (folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }
}
