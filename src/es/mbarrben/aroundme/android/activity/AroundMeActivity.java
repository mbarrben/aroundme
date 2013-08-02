package es.mbarrben.aroundme.android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.blinxbox.restig.auth.DialogError;
import com.blinxbox.restig.auth.InstagramAuthDialog.DialogListener;
import com.blinxbox.restinstagram.InstagramCollection;
import com.blinxbox.restinstagram.types.MediaPost;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.UiThread;

import es.mbarrben.aroundme.android.R;
import es.mbarrben.aroundme.android.adapter.MediaAdapter;
import es.mbarrben.aroundme.android.fragment.AroundMeFragment;
import es.mbarrben.aroundme.android.fragment.AroundMeMapFragment;
import es.mbarrben.aroundme.android.fragment.AroundMeMapFragment.OnLocationChangeListener;
import es.mbarrben.aroundme.android.instagram.InstagramManager;
import es.mbarrben.aroundme.android.instagram.MediaPostComparator;
import es.mbarrben.aroundme.android.map.Utils;

@EActivity(R.layout.activity_aroundme)
public class AroundMeActivity extends BaseActivity {
    private static final int DISTANCE_METRES = 5000;

    private InstagramManager instagram;

    @FragmentById(R.id.fragment_aroundme)
    protected AroundMeFragment aroundMeFragment;
    @FragmentById(R.id.fragment_map)
    protected AroundMeMapFragment mapFragment;

    private boolean isAuthenticated = false;

    private DialogListener authDialogListener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
        }

        @Override
        public void onComplete(Bundle values) {
            isAuthenticated = true;
            enableLocation();
        }

        @Override
        public void onCancel() {
            finish();
        }
    };

    private OnLocationChangeListener locationChangeListener = new OnLocationChangeListener() {

        @Override
        public void onLocationChange(double latitude, double longitude) {
            fetchPosts(latitude, longitude);
        }
    };

    @AfterInject
    protected void init() {
        instagram = new InstagramManager(this);

        getSherlock().getActionBar().setIcon(R.drawable.title_image);
        getSherlock().getActionBar().setDisplayShowTitleEnabled(false);
        getSherlock().getActionBar().setDisplayUseLogoEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isOnline()) {
            if (canShowNetworkDialog()) {
                showNetworkErrorDialog();
            }
            return;
        }

        if (isAuthenticated) {
            enableLocation();
        } else {
            instagram.authorize(authDialogListener);
        }
    }

    @Background
    protected void fetchPosts(double latitude, double longitude) {
        InstagramCollection<MediaPost> collection = instagram.fetchNearMediaCollection(latitude, longitude,
                DISTANCE_METRES);
        List<MediaPost> media = new ArrayList<MediaPost>(collection.getData());

        Location location = Utils.getLastKnownLocation(getApplicationContext());
        MediaPostComparator comparator = new MediaPostComparator(location);
        Collections.sort(media, comparator);

        fillList(media);
        fillMap(media);
    }

    @UiThread
    protected void fillList(List<MediaPost> media) {
        MediaAdapter adapter = new MediaAdapter(getApplicationContext());
        adapter.setMediaList(media);
        aroundMeFragment.setListAdapter(adapter);
    }

    @UiThread
    protected void fillMap(List<MediaPost> media) {
        mapFragment.setMediaPostCollection(media);
    }

    private void enableLocation() {
        try {
            if (!isGpsEnabled() && canShowGpsDialog()) {
                showEnableGpsDialog();
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "GPS not available");
        }

        if (mapFragment != null) {
            mapFragment.enableLocation(locationChangeListener);
        }
    }

    private void disableLocation() {
        if (mapFragment != null) {
            mapFragment.disableLocation();
        }
    }

}
