package com.coolSchool.coolSchool.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class UriHelper {
    public static URI appendUri(String uri, String appendQuery) {
        URI oldUri = URI.create(uri);
        try {
            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }

            return new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment());
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
            System.out.println("ERROR BUILDING URI");
            return oldUri;
        }
    }
}
