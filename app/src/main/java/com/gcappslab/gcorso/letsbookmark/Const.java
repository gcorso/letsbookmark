package com.gcappslab.gcorso.letsbookmark;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Alessandro.c on 28/06/2015.
 */
public final class Const {

    public static class IntentKeyConst {
        public static final String SITE_EXTRA = "SITE_EXTRA";
        public static final String PARENT_ACTIVITY_EXTRA = "PARENT_ACTIVITY_EXTRA";
    }

    public static class IntentRequest {
        /* Message use to send message string over activity with intents */
        public static final int CONFIRM_REQUEST = 0;
        public static final int ADD_SITE_REQUEST = 2;
        public static final int ADD_FOLDER_REQUEST = 3;
    }

    public static final String SEP_I = "<<<--->>>";
    public static final String SEP_II = "<<-->>";
    public static final String SEP_III = "<->";

    public static String getDomainUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain;
    }

    public static String getImageUrl(String url)  {
        String image;

        try {
            String domainUrl = getDomainUrl(url);
            image = "http://" + domainUrl + "/favicon.ico";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            image = "nothing";
        }
        return image;
    }

}
