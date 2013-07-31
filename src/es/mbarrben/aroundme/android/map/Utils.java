package es.mbarrben.aroundme.android.map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public final class Utils {
    private Utils() {
    }
    
    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        return locationManager.getLastKnownLocation(provider);
    }
}
