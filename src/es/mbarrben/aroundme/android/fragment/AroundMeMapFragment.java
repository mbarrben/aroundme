package es.mbarrben.aroundme.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.blinxbox.restinstagram.types.MediaPost;
import com.blinxbox.restinstagram.types.MediaPost.Location;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.Options;

import es.mbarrben.aroundme.android.map.ClusterMapOptions;

public class AroundMeMapFragment extends SupportMapFragment {

    private ArrayList<InputPoint> inputPoints;
    private Options options;
    private Clusterkraf clusterkraf;

    @Override
    public void onPause() {
        super.onPause();

        if (clusterkraf != null) {
            clusterkraf.clear();
            clusterkraf = null;
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
}
