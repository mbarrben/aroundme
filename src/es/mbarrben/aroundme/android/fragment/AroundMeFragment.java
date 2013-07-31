package es.mbarrben.aroundme.android.fragment;

import java.util.List;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.blinxbox.restig.auth.DialogError;
import com.blinxbox.restig.auth.InstagramAuthDialog.DialogListener;
import com.blinxbox.restinstagram.InstagramCollection;
import com.blinxbox.restinstagram.types.MediaPost;

import es.mbarrben.aroundme.android.instagram.Instagram;
import es.mbarrben.aroundme.android.task.SafeAsyncTask;

public class AroundMeFragment extends SherlockListFragment {

    private Instagram instagram;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        instagram = new Instagram(getActivity());
        instagram.authorize(authDialogListener);
    }

    private DialogListener authDialogListener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onComplete(Bundle values) {
            final double lat = 40.4051016;
            final double lng = -3.9981401;

            SafeAsyncTask<InstagramCollection<MediaPost>> task = new SafeAsyncTask<InstagramCollection<MediaPost>>() {

                @Override
                public InstagramCollection<MediaPost> call() throws Exception {
                    return instagram.fetchNearMediaCollection(lat, lng);
                }

                @Override
                protected void onSuccess(InstagramCollection<MediaPost> media) throws Exception {
                    List<MediaPost> mediaList = media.getData();
                    
                }

            };
            task.execute();
        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub

        }
    };
}
