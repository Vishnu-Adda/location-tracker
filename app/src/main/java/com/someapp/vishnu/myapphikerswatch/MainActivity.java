package com.someapp.vishnu.myapphikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latitudeText;
    TextView longitudeText;
    TextView accuracyText;
    TextView altitudeText;
    TextView addressText;

    final static String LATITUDE_STRING;
    final static String LONGITUDE_STRING;
    final static String ACCURACY_STRING;
    final static String ALTITUDE_STRING;
    final static String ADDRESS_STRING;

    static {

        LATITUDE_STRING = "Latitude: ";
        LONGITUDE_STRING = "Longitude: ";
        ACCURACY_STRING = "Accuracy: ";
        ALTITUDE_STRING = "Altitude: ";
        ADDRESS_STRING = "Address: ";

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0, 0, locationListener);

                }
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        accuracyText = findViewById(R.id.accuracyText);
        altitudeText = findViewById(R.id.altitudeText);
        addressText = findViewById(R.id.addressText);

        locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) { // Regular updates from GPS change

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses =  geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    latitudeText.setText(LATITUDE_STRING +
                            Float.toString((float)location.getLatitude()));

                    longitudeText.setText(LONGITUDE_STRING +
                            Float.toString((float)location.getLongitude()));

                    accuracyText.setText(ACCURACY_STRING +
                            Integer.toString((int)location.getAccuracy()) + " meters");

                    altitudeText.setText(ALTITUDE_STRING +
                            Integer.toString((int)location.getAltitude()) + " meters" );

                    if(listAddresses != null && listAddresses.size() > 0) {

                        if (listAddresses.get(0).getLocality() != null
                                && listAddresses.get(0).getAdminArea() != null) {

                            addressText.setText(ADDRESS_STRING +
                                    listAddresses.get(0).getLocality() + " "
                                    + listAddresses.get(0).getAdminArea());

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // When user agrees or disagrees to working with this
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);

        } else {

            // This code will ask the user for permission aka if permission wasn't granted
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // This generates the popup to allow access to the location
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, locationListener);

                Location lastKnownLocation =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses =  geocoder.getFromLocation(lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude(), 1);

                    latitudeText.setText(LATITUDE_STRING +
                            Float.toString((float)lastKnownLocation.getLatitude()));

                    longitudeText.setText(LONGITUDE_STRING +
                            Float.toString((float)lastKnownLocation.getLongitude()));

                    accuracyText.setText(ACCURACY_STRING +
                            Integer.toString((int)lastKnownLocation.getAccuracy()) + " meters");

                    altitudeText.setText(ALTITUDE_STRING +
                            Integer.toString((int)lastKnownLocation.getAltitude()) + " meters" );

                    if(listAddresses != null && listAddresses.size() > 0) {

                        if (listAddresses.get(0).getLocality() != null
                                && listAddresses.get(0).getAdminArea() != null) {

                            addressText.setText(ADDRESS_STRING +
                                    listAddresses.get(0).getLocality() + " "
                                    + listAddresses.get(0).getAdminArea());

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
