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
import es.mbarrben.aroundme.android.instagram.Instagram;
import es.mbarrben.aroundme.android.task.SafeAsyncTask;

public class AroundMeActivity extends SherlockFragmentActivity {

    private Instagram instagram;
    private AroundMeFragment aroundMeFragment;
    private AroundMeMapFragment mapFragment;

    private DialogListener authDialogListener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onComplete(Bundle values) {
            final double lat = 40.4051016;
            final double lng = -3.9981401;
            final int distanceMetres = 10000;

            SafeAsyncTask<InstagramCollection<MediaPost>> task = new SafeAsyncTask<InstagramCollection<MediaPost>>() {

                @Override
                public InstagramCollection<MediaPost> call() throws Exception {
                    return instagram.fetchNearMediaCollection(lat, lng, distanceMetres);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.aroundme, menu);
        return true;
    }

}
