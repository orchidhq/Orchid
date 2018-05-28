package com.eden.orchid.impl.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.publication.OrchidPublisher;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NetlifyPublisher extends OrchidPublisher {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType BINARY = MediaType.parse("application/octet-stream");
    private static final String netlifyUrl = "https://api.netlify.com/api/v1";

    @Getter @Setter
    @Option
    @Description("Your Netlify site ID or domain (ie. orchid.netlify.com).")
    private String siteId;

    private final String netlifyToken;
    private final String destinationDir;
    private final OkHttpClient client;

    @Inject
    public NetlifyPublisher(
            OrchidContext context,
            OkHttpClient client,
            @Named("d") String destinationDir,
            @Nullable @Named("netlifyToken") String netlifyToken) {
        super(context, "netlify", 100);
        this.client = client;
        this.destinationDir = destinationDir;
        this.netlifyToken = netlifyToken;
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        valid = valid && exists(netlifyToken, "A Netlify Personal Access Token is required for deploys, set as 'netlifyToken' flag.");
        valid = valid && exists(siteId,       "A Netlify site domain must be provided.");

        // make sure the site exists
        EdenPair<Boolean, String> site = netlifyGet("sites/" + siteId);
        if(!site.first) {
            Clog.e("A Netlify site at {} does not exist or it cannot be accessed.", siteId);
            valid = false;
        }

        return valid;
    }

    @Override
    public void publish() {
        File file = new File(destinationDir);
        Map<String, List<File>> fileMap = new HashMap<>();

        // create digest of files to be uploaded
        JSONObject body = new JSONObject();
        JSONObject body_files = new JSONObject();

        if (file.exists() && file.isDirectory()) {
            Collection newFiles = FileUtils.listFiles(file, null, true);

            if (!EdenUtils.isEmpty(newFiles)) {
                for (Object object : newFiles) {
                    File child = (File) object;
                    if(child.isDirectory()) continue;
                    try {
                        String path = OrchidUtils.getRelativeFilename(child.getAbsolutePath(), destinationDir);
                        String sha1 = OrchidUtils.sha1(child);
                        body_files.put(path, sha1);
                        fileMap.computeIfAbsent(sha1, s -> new ArrayList<>()).add(child);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        body.put("files", body_files);

        // post to Netlify to determine which files need to be uploaded still
        EdenPair<Boolean, String> requiredFilesResponse = netlifyPost(Clog.format("sites/{}/deploys", siteId), body);
        if(!requiredFilesResponse.first) {
            throw new RuntimeException("something went wrong attempting to deploy to Netlify: " + requiredFilesResponse.second);
        }
        JSONObject requiredFiles = new JSONObject(requiredFilesResponse.second);

        // upload all required files
        String deployId = requiredFiles.getString("id");
        requiredFiles
                .getJSONArray("required")
                .toList()
                .parallelStream()
                .filter(Objects::nonNull)
                .map(o -> (String) o)
                .flatMap(sha1ToUpload -> fileMap.getOrDefault(sha1ToUpload, new ArrayList<>()).parallelStream())
                .forEach(fileToUpload-> {
                    String path = OrchidUtils.getRelativeFilename(fileToUpload.getAbsolutePath(), destinationDir);
                    netlifyUpload(deployId, path, fileToUpload);
                });
    }

    private EdenPair<Boolean, String> netlifyGet(String url) {
        String fullURL = Clog.format("{}/{}", netlifyUrl, url);
        Clog.d("Netlify GET: {}", fullURL);
        try {
            Request request = new Request.Builder()
                    .url(fullURL)
                    .header("Authorization", "Bearer " + netlifyToken)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();

            return new EdenPair<>(response.isSuccessful(), response.body().string());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new EdenPair<>(false, null);
    }

    private EdenPair<Boolean, String> netlifyPost(String url, JSONObject body) {
        String fullURL = Clog.format("{}/{}", netlifyUrl, url);
        Clog.d("Netlify POST: {}", fullURL);
        try {
            Request request = new Request.Builder()
                    .url(fullURL)
                    .header("Authorization", "Bearer " + netlifyToken)
                    .post(RequestBody.create(JSON, body.toString()))
                    .build();
            Response response = client.newCall(request).execute();

            return new EdenPair<>(response.isSuccessful(), response.body().string());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new EdenPair<>(false, null);
    }

    private EdenPair<Boolean, String> netlifyUpload(String deployId, String filename, File toUpload) {
        String fullURL = Clog.format("{}/deploys/{}/files/{}", netlifyUrl, deployId, filename);
        Clog.d("Netlify UPLOAD: {}", fullURL);
        try {
            Request request = new Request.Builder()
                    .url(fullURL)
                    .header("Authorization", "Bearer " + netlifyToken)
                    .put(RequestBody.create(BINARY, toUpload))
                    .build();
            Response response = client.newCall(request).execute();

            return new EdenPair<>(response.isSuccessful(), response.body().string());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new EdenPair<>(false, null);
    }

}
