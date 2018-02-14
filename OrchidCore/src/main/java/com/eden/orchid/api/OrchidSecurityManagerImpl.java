package com.eden.orchid.api;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileDescriptor;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrchidSecurityManagerImpl extends OrchidSecurityManager {

    private final List<String> readableDirs;
    private final List<String> writableDirs;

    private final boolean enforceReadAccess = false;
    private final boolean enforceWriteAccess = true;

    @Inject
    public OrchidSecurityManagerImpl(@Named("resourcesDir") String resourcesDir, @Named("d") String d) {
        String source            = OrchidUtils.normalizePath(resourcesDir);
        String destination       = OrchidUtils.normalizePath(d);
        String destinationParent = OrchidUtils.normalizePath(String.join("/", Arrays.copyOfRange(d.split("/"), 0, d.split("/").length - 1)));
        String tmp               = OrchidUtils.normalizePath(System.getProperty("java.io.tmpdir"));
        String cwd               = OrchidUtils.normalizePath(Paths.get(".").toAbsolutePath().normalize().toString());
        String javaHome          = OrchidUtils.normalizePath(System.getenv("JAVA_HOME"));

        readableDirs = new ArrayList<>();
        if(enforceReadAccess) {
            readableDirs.add(source);
            readableDirs.add(destination);
            readableDirs.add(tmp);
            readableDirs.add(cwd);
            readableDirs.add(javaHome);
            for(URL url: ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs()){
                readableDirs.add(OrchidUtils.normalizePath(url.getFile()));
            }
        }

        writableDirs = new ArrayList<>();
        if(enforceWriteAccess) {
            writableDirs.add(source);
            writableDirs.add(destination);
            writableDirs.add(destinationParent);
            writableDirs.add(tmp);
        }
    }

    @Override public void checkPermission(Permission perm) { }
    @Override public void checkPermission(Permission perm, Object context) { }
    @Override public void checkCreateClassLoader() { }
    @Override public void checkAccess(Thread t) { }
    @Override public void checkAccess(ThreadGroup g) { }
    @Override public void checkExit(int status) { }
    @Override public void checkExec(String cmd) { }
    @Override public void checkLink(String lib) { }
    @Override public void checkRead(FileDescriptor fd) { }
    @Override public void checkWrite(FileDescriptor fd) { }
    @Override public void checkConnect(String host, int port) { }
    @Override public void checkConnect(String host, int port, Object context) { }
    @Override public void checkListen(int port) { }
    @Override public void checkAccept(String host, int port) { }
    @Override public void checkMulticast(InetAddress maddr) { }
    @Override public void checkMulticast(InetAddress maddr, byte ttl) { }
    @Override public void checkPropertiesAccess() { }
    @Override public void checkPropertyAccess(String key) { }
    @Override public void checkPrintJobAccess() { }
    @Override public void checkSystemClipboardAccess() { }
    @Override public void checkAwtEventQueueAccess() { }
    @Override public void checkPackageAccess(String pkg) { }
    @Override public void checkPackageDefinition(String pkg) { }
    @Override public void checkSetFactory() { }
    @Override public void checkMemberAccess(Class<?> clazz, int which) { }
    @Override public void checkSecurityAccess(String target) { }

    @Override public void checkRead(String file) { checkFilesystemReadAccess(file); }
    @Override public void checkRead(String file, Object context) { checkFilesystemReadAccess(file); }

    @Override public void checkWrite(String file) { checkFilesystemWriteAccess(file); }
    @Override public void checkDelete(String file) { checkFilesystemWriteAccess(file); }

    private void checkFilesystemWriteAccess(String file) {
        if(enforceWriteAccess) {
            String normalizedFilename = OrchidUtils.normalizePath(file);
            boolean inWritableDirs = false;

            for (String dir : writableDirs) {
                if (normalizedFilename.startsWith(dir)) {
                    inWritableDirs = true;
                    break;
                }
            }

            if (!inWritableDirs) {
                throw new SecurityException(Clog.format("Modifying file outside source, destination, and temp directories: {}", normalizedFilename));
            }
        }
    }

    private void checkFilesystemReadAccess(String file) {
        if(enforceReadAccess) {
            String normalizedFilename = OrchidUtils.normalizePath(file);
            boolean inReadableDirs = false;

            for (String dir : readableDirs) {
                if (normalizedFilename.startsWith(dir)) {
                    inReadableDirs = true;
                    break;
                }
            }

            if (!inReadableDirs) {
                throw new SecurityException(Clog.format("Reading file outside source, destination, current working, and temp directories: {}", normalizedFilename));
            }
        }
    }
}
