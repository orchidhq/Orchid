package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.parseltongue.Spell;
import com.eden.orchid.Orchid;

import java.net.MalformedURLException;
import java.net.URL;

public class ClogSpells {

    @Spell
    public static String baseUrl(Object object) {
        return Orchid.getInstance().getContext().getSite().getBaseUrl();
    }

    @Spell
    public static String baseUrlScheme(Object object) throws MalformedURLException {
        return new URL(ClogSpells.baseUrl(object)).getProtocol();
    }

    @Spell
    public static String baseUrlHost(Object object) throws MalformedURLException {
        return new URL(ClogSpells.baseUrl(object)).getHost();
    }

    @Spell
    public static int baseUrlPort(Object object) throws MalformedURLException {
        return new URL(ClogSpells.baseUrl(object)).getPort();
    }

    @Spell
    public static String baseUrlRoot(Object object) throws MalformedURLException {
        URL url = new URL(ClogSpells.baseUrl(object));

        String urlRoot = "";
        urlRoot += url.getProtocol() + "://";
        urlRoot += url.getHost();

        if(url.getPort() != -1 && url.getPort() != 80) {
            urlRoot += ":" + url.getPort();
        }

        return urlRoot;
    }

    @Spell
    public static String orchidVersion(Object object) {
        return Orchid.getInstance().getContext().getSite().getOrchidVersion();
    }

    @Spell
    public static String env(Object object) {
        return Orchid.getInstance().getContext().getSite().getEnvironment();
    }

    @Spell
    public static String version(Object object) {
        return Orchid.getInstance().getContext().getSite().getVersion();
    }

}
