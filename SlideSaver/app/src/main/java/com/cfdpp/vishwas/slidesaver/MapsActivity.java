package com.cfdpp.vishwas.slidesaver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cfdpp.vishwas.slidesaver.model.SensorResponse;
import com.evernote.android.job.JobManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.maps.android.projection.SphericalMercatorProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean runThread = true;
    private List<WeightedLatLng> currList;

    String BASE_URL = "http://13.71.4.119:3000/";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.client(httpClient.build())
            .build();
    SensorServiceInterface client = retrofit.create(SensorServiceInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        currList = null;

//        service = new AzureService();
//        AzureService service = new AzureService();
//        startService(new Intent(this, AzureService.class));
        JobManager.create(this).addJobCreator(new AzureJobCreator());
        AzureService.scheduleJob();
    }

    Marker m, m2;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m2 = null;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    if (m2 != null) {
                        m2.remove();
                    }
                    m2 = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("Your Location")
                            .snippet("Risk: <Some probability"));
                    m2.showInfoWindow();
                }
            });
        }
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//
//            @Override
//            public void onMyLocationChange(Location arg0) {
//                // TODO Auto-generated method stub
//                mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
//            }
//        });

        Call<List<SensorResponse>> call = client.getResponse();
        call.enqueue(new Callback<List<SensorResponse>>() {
            @Override
            public void onResponse(Call<List<SensorResponse>> call, Response<List<SensorResponse>> response) {
                List<SensorResponse> sensorResponseList = response.body();
                List<WeightedLatLng> list = new ArrayList<>();
                for (SensorResponse i : sensorResponseList) {
                    Log.e(i.getDeviceID(), String.valueOf(i.getRiskFactor()));
                    list.add(new WeightedLatLng(new LatLng(i.get_lat(), i.get_long()), i.getRiskFactor()));
                }
                currList = list;
                addHeatMap(list);
                startUpdateThread();
            }

            @Override
            public void onFailure(Call<List<SensorResponse>> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (m2 != null) {
                    m2.remove();
                }
                m2 = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Risk")
                        .snippet(String.valueOf(getCurrRisk(latLng)))
                );
                m2.showInfoWindow();
            }
        });


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Zoom into users location
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                centreMapOnLocation(location, "Your Location");
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//            }
//        };
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            centreMapOnLocation(lastKnownLocation, "Your Location");
//            LatLng sydney = new LatLng(-34, 151);
//            m = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").visible(false));
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
    }


    public void centreMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.clear();
        m.remove();
        m = mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
    }

    private static final SphericalMercatorProjection sProjection = new SphericalMercatorProjection(1.0D);

    public void startUpdateThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (runThread) {
                    Call<List<SensorResponse>> call = client.getResponse();
                    call.enqueue(new Callback<List<SensorResponse>>() {
                        @Override
                        public void onResponse(Call<List<SensorResponse>> call, Response<List<SensorResponse>> response) {
                            List<SensorResponse> sensorResponseList = response.body();
                            List<WeightedLatLng> list = new ArrayList<>();
                            for (SensorResponse i : sensorResponseList) {
                                list.add(new WeightedLatLng(new LatLng(i.get_lat(), i.get_long()), i.getRiskFactor()));
                            }
                            addHeatMap(list);
                            currList = list;
                            mProvider.setWeightedData(list);
                            mOverlay.clearTileCache();
                        }

                        @Override
                        public void onFailure(Call<List<SensorResponse>> call, Throwable t) {
                            Log.e("Error", t.toString());
                        }
                    });

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    private void addHeatMap(List<WeightedLatLng> list) {
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

//    @Override
//    public void onBackPressed() {
//        finishAffinity();
//        System.exit(0);
//    }

    public double getCurrRisk(LatLng latLng) {
        // calculate the risk factor based on the nearest location
        List<WeightedLatLng> l = currList;
        double[] distances = new double[l.size()];
        for (int i = 0; i < l.size(); i++) {
            double distance = getDistance(latLng, sProjection.toLatLng(l.get(i).getPoint()), 'M');
            distances[i] = distance;
            Log.e("distance", String.valueOf(distance));
        }

        double dsq = 0;
        double min_dist = distances[0];
        for (double d : distances) {
            dsq += 1 / (d * d);
//            avg_dist += d;
            if (min_dist > d){
                min_dist = d;
            }
        }
//        avg_dist /= l.size();

        double result = 0;
        if (min_dist > 100000) {
            result = 0;
        } else {
            for (int i = 0; i < l.size(); ++i) {
                if (distances[i] <= 50) {
                    result = l.get(i).getIntensity() * dsq;
                    break;
                }
                result += l.get(i).getIntensity() / (distances[i] * distances[i]);
            }
            result /= dsq;
        }
        Log.e("CurrRiskFactor", String.valueOf(result));

        return result;
    }

    private double getDistance(LatLng l1, LatLng l2, char unit) {
        double lat1 = l1.latitude, lat2 = l2.latitude;
        double lon1 = l1.longitude, lon2 = l2.longitude;
        Log.e("coordinates", String.valueOf(lat1) + " " + String.valueOf(lat2));
        Log.e("coordinates", String.valueOf(lon1) + " " + String.valueOf(lon2));
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        } else {
            dist = dist * 1.609344 * 1000;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
