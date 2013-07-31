package es.mbarrben.aroundme.android.map;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;

public abstract class OnSignificantLocationChangeListener implements OnMyLocationChangeListener {
    private static final int SIGNIFICANT_DISTANCE_METRES = 1000;

    private Location lastLocation = null;

    @Override
    public void onMyLocationChange(Location location) {
        Log.i(getClass().getName(), "Location: " + location.getLatitude() + ", " + location.getLongitude());

        if (lastLocation == null || lastLocation.distanceTo(location) > SIGNIFICANT_DISTANCE_METRES) {
            lastLocation = location;
            onSignificantLocationChange(location);
        }
    }

    public abstract void onSignificantLocationChange(Location location);

}
