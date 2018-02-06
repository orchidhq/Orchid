package com.eden.orchid.javadoc;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.pages.JavadocClassPage;
import com.eden.orchid.javadoc.pages.JavadocPackagePage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
@Singleton
public class JavadocModel {

    private final OrchidContext context;

    public List<JavadocClassPage> allClasses;
    public List<JavadocPackagePage> allPackages;

    private Map<String, OrchidPage> classPageCache;
    private Map<String, OrchidPage> packagePageCache;

    @Inject
    public JavadocModel(OrchidContext context) {
        this.context = context;
    }

    void initialize(List<JavadocClassPage> allClasses, List<JavadocPackagePage> allPackages) {
        this.allClasses = allClasses;
        this.allPackages = allPackages;
        this.classPageCache = new HashMap<>();
        this.packagePageCache = new HashMap<>();
    }

    List<OrchidPage> getAllPages() {
        List<OrchidPage> pages = new ArrayList<>();
        pages.addAll(allClasses);
        pages.addAll(allPackages);

        return pages;
    }

    public OrchidPage getPackagePage(String packageName) {
        if (packagePageCache.containsKey(packageName)) {
            return packagePageCache.get(packageName);
        }
        else {
            for (JavadocPackagePage packagePage : getAllPackages()) {
                if (packagePage.getPackageDoc().name().equals(packageName)) {
                    packagePageCache.put(packageName, packagePage);
                    return packagePage;
                }
            }
        }

        return null;
    }

    public OrchidPage getClassPage(String className) {
        if (classPageCache.containsKey(className)) {
            return classPageCache.get(className);
        }

        for (JavadocClassPage classPage : getAllClasses()) {
            if (classPage.getClassDoc().qualifiedName().equals(className)) {
                classPageCache.put(className, classPage);
                return classPage;
            }
            else if (classPage.getClassDoc().name().equals(className)) {
                classPageCache.put(className, classPage);
                return classPage;
            }
        }

        OrchidPage page = context.getExternalIndex().findPage(className.replaceAll("\\.", "/"));
        if(page != null) {
            classPageCache.put(className, page);
            return page;
        }

        return null;
    }

}
