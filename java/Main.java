package com.riskfrontiers.rrd.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.json.*;

public class Main {

    // copy/paste your credentials here
    private static final String ClientId = "<my client_id>";
    private static final String ClientSecret = "<my client_secret>";

    public static void main(String[] args) {

        // Get a JWT token from the authentication service
        JSONObject obj = new JSONObject(ObtainToken());
        String myToken = obj.getString("access_token");

        // The token may be re-used multiple times for an hour
        String myRiskRating = RetrieveRiskRatingsByGnaf(myToken, "GANSW703873411");
        System.out.println("GNAF-ID number 1:");
        System.out.println(myRiskRating);

        myRiskRating = RetrieveRiskRatingsByGnaf(myToken, "GASA_414890230");
        System.out.println("GNAF-ID number 2:");
        System.out.println(myRiskRating);

        myRiskRating = RetrieveRiskRatingsByGnaf(myToken, "GANT_702910374");
        System.out.println("GNAF-ID number 3:");
        System.out.println(myRiskRating);
    }

    private static String RetrieveRiskRatingsByGnaf(String authToken, String GnafToGet) {
        String urlString = "https://rrd.riskfrontiers.com/v1/gnaf_risk_ratings/" + GnafToGet;
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Authorization", "Bearer " + authToken);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = reader.readLine();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String ObtainToken() {

        String urlString = "https://auth.riskfrontiers.com/connect/token";
        Map<String, String> bodyParams = Map.of("client_id", ClientId,
                "client_secret", ClientSecret,
                "scope", "rrd1",
                "grant_type", "client_credentials");

        URL url = null;
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlString);
            byte[] postData = makeParamString(bodyParams).getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            urlConnection.setUseCaches(false);

            urlConnection.connect();

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();

            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
            String result = reader.readLine();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String makeParamString(Map<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
