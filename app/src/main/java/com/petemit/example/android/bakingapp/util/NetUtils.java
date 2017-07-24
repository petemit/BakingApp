package com.petemit.example.android.bakingapp.util;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Peter on 7/22/2017.
 */

public class NetUtils {

    private static String baseURI="d17h27t6h515a5.cloudfront.net";
    private static String URIpt1="topher";
    private static String URIpt2="2017";
    private static String URIpt3="May";
    private static String URIpt4="59121517_baking";
    private static String filename="baking.json";

    public static URL getRecipeJsonURL(){
        Uri.Builder builder = new Uri.Builder();
        Uri uri=builder.scheme("http")
                .authority(baseURI)
                .appendPath(URIpt1)
                .appendPath(URIpt2)
                .appendPath(URIpt3)
                .appendPath(URIpt4)
                .appendPath(filename)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static String getResponseFromURL(URL url)throws  IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            byte[] buffer = new byte[1024];
            int length;
            InputStream in = conn.getInputStream();
            while ((length= in.read(buffer))!=-1){
                result.write(buffer,0,length);
            }
        }
        finally {
            conn.disconnect();
        }
        return result.toString();

    }
}
