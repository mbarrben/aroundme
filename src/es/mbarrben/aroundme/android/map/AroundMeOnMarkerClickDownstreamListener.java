package es.mbarrben.aroundme.android.map;

import java.lang.ref.WeakReference;

import android.content.Context;

import com.google.android.gms.maps.model.Marker;
import com.twotoasters.clusterkraf.ClusterPoint;
import com.twotoasters.clusterkraf.OnMarkerClickDownstreamListener;

public class AroundMeOnMarkerClickDownstreamListener implements OnMarkerClickDownstreamListener {

    private final WeakReference<Context> contextRef;

    public AroundMeOnMarkerClickDownstreamListener(Context context) {
        this.contextRef = new WeakReference<Context>(context);
    }

    @Override
    public boolean onMarkerClick(Marker marker, ClusterPoint clusterPoint) {
        Context context = contextRef.get();
        if (context != null && marker != null && clusterPoint != null && clusterPoint.size() == 1) {
            return true;
        }
        return false;
    }

}
