package com.abdelgawad.DMS.Task.mRecycler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by abdelgawad on 1/3/18.
 */

public class HttpHandler {
    public HttpHandler() {
    }

    //
    public String makeServiceCall(String reqUrl) {
        String response = null;

        try {
            URL url = new URL(reqUrl);
            //open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //read input stream
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            //save inputStream as String in response
            response = convertStreamToString(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String convertStreamToString(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                //separate between  line
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
