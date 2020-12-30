package com.example.galleonsup.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.galleonsup.MainActivity;

public class GPSLocation {

    Context context;

    LocationChangedListener locationChangedListener;
    public  String presentLat = "", presentLon = "", presentAcc = "";
    public LocationManager locationManager;
    public GPSLocationListener listener;
    public Location previousBestLocation = null;

    public GPSLocation(Context context)
    {
        this.context = context;
    }


    public interface LocationChangedListener {
        public void locationChangeCallback(String lat, String lon, String acc);
    }

    public void setLocationChangedListener(LocationChangedListener locationChangedListener)
    {
        this.locationChangedListener = locationChangedListener;
    }

    public void GPS_Start() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            listener = new GPSLocationListener();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        } catch (Exception ex) {

        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        int TWO_MINUTES = 1000 * 60;
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    public class GPSLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.i("**********", "Location changed");
            if (isBetterLocation(location, previousBestLocation)) {


                location.getAccuracy();
                //location.setText(" " + loc.getAccuracy());

                presentLat = String.valueOf(location.getLatitude());
                presentLon = String.valueOf(location.getLongitude());
                presentAcc = String.valueOf(location.getAccuracy());
                locationChangedListener.locationChangeCallback(presentLat,presentLon,presentAcc);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(context, "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

}
