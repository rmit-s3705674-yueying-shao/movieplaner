package com.movie.myapplication.model;

import android.content.Context;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistanceJson {

    private String Latitude;
    private String Longitude;

    public DistanceJson() {

        // default location
        this.Latitude = "-37.807390";
        this.Longitude = "144.963300";
    }

    public int getRequireTime(String Location, Context ctx) {
        int min = 9999;
        try {
            JSONObject mapInfo = new JSONObject(getHtml(Location.replace(" ", ""), ctx));
            JSONArray rows = mapInfo.getJSONArray("rows");
            JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
            String time = elements.getJSONObject(0).getJSONObject("duration").getString("value");
            min = Integer.parseInt(time);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        min = min / 60;
        return min;
    }


    private String getHtml(String Location, Context ctx) throws Exception {

        Gps gps = new Gps(ctx);
        Location location = gps.getLocation();

        if (location != null) {
            this.Latitude = (location.getLatitude() + "").substring(0, 7);
            this.Longitude = (location.getLongitude() + "").substring(0, 7);
        }


        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + this.Latitude + "," + this.Longitude + "&destinations=" + Location + "&mode=driving&key=AIzaSyDeDD1rYEX9NtQdsVYLBcOAMwpBHL59KI8");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        byte[] data = readInputStream(inStream);
        String html = new String(data, "UTF-8");
        return html;
    }

    private byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
