package es.mbarrben.aroundme.android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.location.Location;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.blinxbox.restig.auth.DialogError;
import com.blinxbox.restig.auth.InstagramAuthDialog.DialogListener;
import com.blinxbox.restinstagram.InstagramCollection;
import com.blinxbox.restinstagram.types.MediaPost;

import es.mbarrben.aroundme.android.R;
import es.mbarrben.aroundme.android.adapter.MediaAdapter;
import es.mbarrben.aroundme.android.fragment.AroundMeFragment;
import es.mbarrben.aroundme.android.fragment.AroundMeMapFragment;
import es.mbarrben.aroundme.android.fragment.AroundMeMapFragment.OnLocationChangeListener;
import es.mbarrben.aroundme.android.instagram.Instagram;
import es.mbarrben.aroundme.android.instagram.MediaPostComparator;
import es.mbarrben.aroundme.android.map.Utils;
import es.mbarrben.aroundme.android.task.SafeAsyncTask;

public class AroundMeActivity extends SherlockFragmentActivity implements OnLocationChangeListener {
    private static final int DISTANCE_METRES = 5000;

    private Instagram instagram;
    private AroundMeFragment aroundMeFragment;
    private AroundMeMapFragment mapFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroundme);

        getSherlock().getActionBar().setIcon(R.drawable.title_image);
        getSherlock().getActionBar().setDisplayShowTitleEnabled(false);
        getSherlock().getActionBar().setDisplayUseLogoEnabled(false);

        aroundMeFragment = (AroundMeFragment) getSupportFragmentManager().findFragmentByTag("aroundme");
        mapFragment = (AroundMeMapFragment) getSupportFragmentManager().findFragmentByTag("map");

        instagram = new Instagram(this);
        instagram.authorize(authDialogListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAuthenticated) {
            enableLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.aroundme, menu);
        return true;
    }

    @Override
    public void onLocationChange(final double latitude, final double longitude) {
        SafeAsyncTask<List<MediaPost>> task = new SafeAsyncTask<List<MediaPost>>() {

            @Override
            public List<MediaPost> call() throws Exception {
                InstagramCollection<MediaPost> collection = instagram.fetchNearMediaCollection(latitude, longitude,
                        DISTANCE_METRES);
                List<MediaPost> media = new ArrayList<MediaPost>(collection.getData());

                Location location = Utils.getLastKnownLocation(getApplicationContext());
                MediaPostComparator comparator = new MediaPostComparator(location);
                Collections.sort(media, comparator);

                return media;
            }

            @Override
            protected void onSuccess(List<MediaPost> media) throws Exception {
                MediaAdapter adapter = new MediaAdapter(getApplicationContext());
                adapter.setMediaList(media);
                aroundMeFragment.setListAdapter(adapter);
                mapFragment.setMediaPostCollection(media);
            }

        };
        task.execute();
    }

    private void enableLocation() {
        if (mapFragment != null) {
            mapFragment.enableLocation(AroundMeActivity.this);
        }
    }

    private void disableLocation() {
        if (mapFragment != null) {
            mapFragment.disableLocation();
        }
    }

}
