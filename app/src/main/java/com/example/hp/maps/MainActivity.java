package com.example.hp.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "chanho";

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    GoogleMap mGoogleMap;
    GoogleMap GoogleMap2;

    private RSdbHelper rsDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (!checkLocationPermissions()) {
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
        } else
            getLastLocation();

    rsDbHelper = new RSdbHelper(this); //DB


        Button lastLocation = (Button) findViewById(R.id.Find);
        lastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getAddress();
                }

        });

    }
//액션바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//액션바 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MyLocation:
               getLastLocation();
                return true;
            case R.id.action_subactivity:
                startActivity(new Intent(this, SubActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkLocationPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLastLocation();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }

        }

    }

    //현재 위치 가져오기
        @SuppressWarnings("MissingPermission")
    private void getLastLocation(){

            Task task = mFusedLocationClient.getLastLocation();
            task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                    mLastLocation = location;
                LatLng location1 = new LatLng(location.getLatitude(),location.getLongitude());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location1, 15));
                    } else
                        Toast.makeText(getApplicationContext(),"no location detected"  +location.getLongitude() ,
                                Toast.LENGTH_SHORT)
                                .show();
                }
            });
    }

    //검색 했을 때 위,경도 찾아서 마커 표시
    private void getAddress() {

        TextView addressTextView = (TextView) findViewById(R.id.newLocation);
        EditText search = (EditText)findViewById(R.id.search);
        String SearchLo = search.getText().toString();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
  List<Address> addresses = geocoder.getFromLocationName(SearchLo,1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);

                addressTextView.setText(String.format("[ %s , %s ]",
                        bestResult.getLatitude(),
                        bestResult.getLongitude()));

                LatLng location2 = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                long Save = rsDbHelper.insertMarkerByMethod(SearchLo , bestResult.getLatitude() ,bestResult.getLongitude());

                mGoogleMap.addMarker(
                        new MarkerOptions().
                                position(location2).
                                title(SearchLo).
                                alpha(0.8f).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp))
                );
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 15));
                if(Save >0) {
                    Toast.makeText(getApplicationContext(), "ffdw" + bestResult.getLongitude() +
                                    bestResult.getLatitude(),
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }

    }

    public void LoadMaker(){

        Cursor Marker = rsDbHelper.getMakerByMethod();

        while(Marker.moveToNext()) {
            double Lati = Double.parseDouble(Marker.getString(2));
            double Long = Double.parseDouble(Marker.getString(3));

                Toast.makeText(getApplicationContext(), "ffdw" +Marker.getString(1)+" "+ Lati + " " + Long,
                        Toast.LENGTH_SHORT).show();

            LatLng location3 = new LatLng(Lati, Long);
            mGoogleMap.addMarker(
                    new MarkerOptions().
                            position(location3).
                            title(Marker.getString(1)).
                            alpha(0.8f).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp))
            );
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location3, 15));
        }

/*        if(Marker.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "ffdw" + Marker.getString(1) +
                            Marker.getString(2) +
                            Marker.getString(3),
                    Toast.LENGTH_SHORT).show();

            while(Marker.moveToNext()) {
                double Lati = Double.parseDouble(Marker.getString(2));
                double Long = Double.parseDouble(Marker.getString(3));

//                Toast.makeText(getApplicationContext(), "ffdw" +Marker.getString(1)+" "+ Lati + " " + Long,
//                        Toast.LENGTH_SHORT).show();

                LatLng location3 = new LatLng(Lati, Long);
                mGoogleMap.addMarker(
                        new MarkerOptions().
                                position(location3).
                                title(Marker.getString(1)).
                                alpha(0.8f).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp))
                );
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location3, 15));
            }

        }else{
            Toast.makeText(getApplicationContext(), "ffdw" ,
                    Toast.LENGTH_SHORT).show();
        }*/


    }


    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LoadMaker();
    }
}
