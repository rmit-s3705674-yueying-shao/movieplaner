package com.movie.myapplication.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.R;
import com.movie.myapplication.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Event> events;
    private HashMap<String, Long> timeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        timeMap=new HashMap<>();

        DBHelper dbh =new DBHelper(this, "movieplan.db", null, 1);
        events= dbh.selectEvent();

        for (int i=0;i<events.size();i++)
        {
            Date sdate = events.get(i).getSdate();
            long diff = sdate.getTime()-System.currentTimeMillis();
            if(diff<0)
            {
                continue;
            }

            timeMap.put(events.get(i).getId(),diff);
        }


        Set<Map.Entry<String, Long>> entrySet =  timeMap.entrySet();

        //注意 ArrayList<>() 括号里要传入map.entrySet()
        List<Map.Entry<String, Long>> list = new ArrayList<>(timeMap.entrySet());
        Collections.sort(list, (Comparator<? super Map.Entry<String, Long>>) new Comparator<Map.Entry<String, Long>>()
        {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {

                return (int) (o1.getValue()-o2.getValue());
            }
/*
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                //按照value值，重小到大排序
//                return o1.getValue() - o2.getValue();

                //按照value值，从大到小排序
//                return o2.getValue() - o1.getValue();

                //按照value值，用compareTo()方法默认是从小到大排序
                return o1.getValue().compareTo(o2.getValue());
            }*/
        });

        //注意这里遍历的是list，也就是我们将map.Entry放进了list，排序后的集合
        int j=0;
        for (Map.Entry s : list)
        {
            if (j>=3)
            {
                continue;
            }
            Event soonEvent = dbh.getEventbyID(s.getKey().toString());
            String substring[] = soonEvent.getLocation().trim().split(",");
            LatLng event = new LatLng(Double.parseDouble(substring[0]), Double.parseDouble(substring[1]));
            mMap.addMarker(new MarkerOptions().position(event).title( soonEvent.getTitle() ));

            if (j==0)
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(event));
                float zoomLevel = 11.0f; //This goes up to 21 ,
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(event, zoomLevel));
            }

            j++;
        }



        // Add a marker in Sydney and move the camera
       /*
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = 19.0f; //This goes up to 21 ,
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
        */
    }
}
