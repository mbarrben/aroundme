package es.mbarrben.aroundme.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.blinxbox.restinstagram.types.MediaPost;
import com.blinxbox.restinstagram.types.MediaPost.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.Options;

import es.mbarrben.aroundme.android.map.ClusterMapOptions;
import es.mbarrben.aroundme.android.map.OnSignificantLocationChangeListener;
import es.mbarrben.aroundme.android.map.Utils;

public class AroundMeMapFragment extends SupportMapFragment {

    private ArrayList<InputPoint> inputPoints;
    private Options options;
    private Clusterkraf clusterkraf;
    private OnLocationChangeListener onLocationChangeListener;
    private boolean isLocationEnabled = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        moveToLastKownLocation();
    }

    private void moveToLastKownLocation() {
        android.location.Location lastKnownLocation = Utils.getLastKnownLocation(getActivity());
        double latitude = lastKnownLocation.getLatitude();
        double longitude = lastKnownLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    @Override
    public void onResume() {
        super.onResume();
        enableLocationIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableLocation();
    }

    private void enableLocationIfNeeded() {
        if (getMap() == null) {
            return;
        }

        if (isLocationEnabled) {
            getMap().setMyLocationEnabled(true);
            getMap().setOnMyLocationChangeListener(new OnSignificantLocationChangeListener() {
                @Override
                public void onSignificantLocationChange(android.location.Location location) {
                    onLocationChangeListener.onLocationChange(location.getLatitude(), location.getLongitude());
                }
            });
        }
    }

    public void enableLocation(OnLocationChangeListener listener) {
        onLocationChangeListener = listener;
        isLocationEnabled = true;
        enableLocationIfNeeded();
    }

    public void disableLocation() {
        onLocationChangeListener = null;
        isLocationEnabled = false;
        if (getMap() != null) {
            getMap().setMyLocationEnabled(false);
            getMap().setOnMyLocationChangeListener(null);
        }
    }

    public void setMediaPostCollection(List<MediaPost> mediaList) {
        if (getMap() != null && mediaList != null && !mediaList.isEmpty()) {
            getMap().setOnCameraChangeListener(new OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition arg0) {
                    initCluster();
                }
            });

            inputPoints = new ArrayList<InputPoint>();
            for (MediaPost post : mediaList) {
                Location location = post.getLocation();
                if (location != null) {
                    LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    inputPoints.add(new InputPoint(coordinates, post));
                    Log.i(getClass().getName(), "coordenadas: " + coordinates.toString());
                }
            }

            initCluster();
        }
    }

    private void initCluster() {
        if (inputPoints != null) {
            if (options == null) {
                options = new ClusterMapOptions(getActivity(), inputPoints);
            }

            clusterkraf = new Clusterkraf(getMap(), options, inputPoints);
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChange(double latitude, double longitude);
    }
}
