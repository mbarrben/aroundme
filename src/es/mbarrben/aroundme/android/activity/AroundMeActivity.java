package es.mbarrben.aroundme.android.activity;

import java.util.List;

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
import es.mbarrben.aroundme.android.task.SafeAsyncTask;

public class AroundMeActivity extends SherlockFragmentActivity implements OnLocationChangeListener {
    private static final int DISTANCE_METRES = 10000;

    private Instagram instagram;
    private AroundMeFragment aroundMeFragment;
    private AroundMeMapFragment mapFragment;

    private boolean isAuthenticated = false;

    private DialogListener authDialogListener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onComplete(Bundle values) {
            isAuthenticated = true;
            enableLocation();
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroundme);

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
        SafeAsyncTask<InstagramCollection<MediaPost>> task = new SafeAsyncTask<InstagramCollection<MediaPost>>() {

            @Override
            public InstagramCollection<MediaPost> call() throws Exception {
                return instagram.fetchNearMediaCollection(latitude, longitude, DISTANCE_METRES);
            }

            @Override
            protected void onSuccess(InstagramCollection<MediaPost> media) throws Exception {
                List<MediaPost> mediaList = media.getData();
                MediaAdapter adapter = new MediaAdapter(getApplicationContext());
                adapter.setMediaList(mediaList);
                // aroundMeFragment.setListAdapter(adapter);
                mapFragment.setMediaPostCollection(mediaList);
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
