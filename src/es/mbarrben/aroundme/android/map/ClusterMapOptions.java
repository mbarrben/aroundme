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

    public ClusterMapOptions(Activity activity, List<InputPoint> inputPoints) {
        this(activity, inputPoints, null);
    }

    public ClusterMapOptions(Activity activity, List<InputPoint> inputPoints, ProcessingListener processingListener) {
        super();
        setTransitionDuration(500);
        setTransitionInterpolator(new DecelerateInterpolator());
        setPixelDistanceToJoinCluster(getPixelDistanceToJoinCluster(activity));
        setZoomToBoundsAnimationDuration(500);
        setShowInfoWindowAnimationDuration(500);
        setExpandBoundsFactor(0.5d);
        setSinglePointClickBehavior(SinglePointClickBehavior.SHOW_INFO_WINDOW);
        setClusterClickBehavior(ClusterClickBehavior.ZOOM_TO_BOUNDS);
        setClusterInfoWindowClickBehavior(ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS);

        /**
         * Device Independent Pixel measurement should be converted to pixels
         * here too. In this case, we cheat a little by using a Drawable's
         * height. It's only cheating because we don't offer a variant for that
         * Drawable for every density (xxhdpi, tvdpi, others?).
         */
        setZoomToBoundsPadding(activity.getResources().getDrawable(R.drawable.ic_map_pin_cluster).getIntrinsicHeight());
        setMarkerOptionsChooser(new AroundMeMarkerOptionsChooser(activity));
        setOnMarkerClickDownstreamListener(new AroundMeOnMarkerClickDownstreamListener(activity));
        setProcessingListener(processingListener);
    }

    private int getPixelDistanceToJoinCluster(Activity activity) {
        return convertDeviceIndependentPixelsToPixels(activity, 100);
    }

    private int convertDeviceIndependentPixelsToPixels(Activity activity, int dip) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return Math.round(displayMetrics.density * dip);
    }

}
