package com.eden.orchid.generators.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.generators.docParser.PackageDocParser;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.impl.StringResource;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@AutoRegister
public class PackagesGenerator implements Generator {

    private List<OrchidPage> packages;

    @Override
    public String getName() {
        return "packages";
    }

    @Override
    public int priority() {
        return 80;
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject packageIndex = new JSONObject();
        packages = new ArrayList<>();
        Set<String> packageNamesSet = new TreeSet<>();

        RootDoc root = Orchid.getRootDoc();

        if(root == null) {
            return null;
        }

        for(ClassDoc classDoc : root.classes()) {
            packageNamesSet.add(classDoc.containingPackage().name());
        }

        for(String packageName : packageNamesSet) {
            PackageDoc packageDoc = root.packageNamed(packageName);

            OrchidReference packageReference = new OrchidReference("classes", packageDoc.name().replaceAll("\\.", File.separator) + File.separator + "index.html");
            packageReference.setTitle(packageDoc.name());

            JSONObject item = new JSONObject();
            item.put("name", packageReference.getTitle());
            item.put("url", packageReference.toString());

            OrchidPage packagePage = new OrchidPage(new StringResource("", packageReference));
            packagePage.setAlias("packageDoc");

            JSONObject packageInfoJson = PackageDocParser.createPackageDocJson(packageDoc);
            packagePage.setData(packageInfoJson);

            OrchidUtils.buildTaxonomy(packageReference.getPath(), packageIndex, item);

            packages.add(packagePage);
        }

        return new JSONElement(packageIndex);
    }

    @Override
    public void startGeneration() {
        for (OrchidPage packagePage : packages) {
            packagePage.renderTemplate("templates/pages/packageDoc.twig");
        }
    }
}
