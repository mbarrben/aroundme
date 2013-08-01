package es.mbarrben.aroundme.android.map;

import java.util.List;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.animation.DecelerateInterpolator;

import com.twotoasters.clusterkraf.Clusterkraf.ProcessingListener;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.Options;

import es.mbarrben.aroundme.android.R;

public class ClusterMapOptions extends Options {

    private static final int DURATION_MILLIS = 500;
    private static final int MIN_DP_TO_CREATE_CLUSTER = 100;
    private static final double EXPAND_FACTOR = 0.5d;

    public ClusterMapOptions(Activity activity, List<InputPoint> inputPoints) {
        this(activity, inputPoints, null);
    }

    public ClusterMapOptions(Activity activity, List<InputPoint> inputPoints, ProcessingListener processingListener) {
        super();
        setTransitionDuration(DURATION_MILLIS);
        setTransitionInterpolator(new DecelerateInterpolator());
        setPixelDistanceToJoinCluster(getPixelDistanceToJoinCluster(activity));
        setZoomToBoundsAnimationDuration(DURATION_MILLIS);
        setShowInfoWindowAnimationDuration(DURATION_MILLIS);
        setExpandBoundsFactor(EXPAND_FACTOR);
        setSinglePointClickBehavior(SinglePointClickBehavior.SHOW_INFO_WINDOW);
        setClusterClickBehavior(ClusterClickBehavior.ZOOM_TO_BOUNDS);
        setClusterInfoWindowClickBehavior(ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS);
        setZoomToBoundsPadding(activity.getResources().getDrawable(R.drawable.ic_map_pin_cluster).getIntrinsicHeight());
        setMarkerOptionsChooser(new AroundMeMarkerOptionsChooser(activity));
        setProcessingListener(processingListener);
    }

    private int getPixelDistanceToJoinCluster(Activity activity) {
        return convertDeviceIndependentPixelsToPixels(activity, MIN_DP_TO_CREATE_CLUSTER);
    }

    private int convertDeviceIndependentPixelsToPixels(Activity activity, int dip) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return Math.round(displayMetrics.density * dip);
    }

}
