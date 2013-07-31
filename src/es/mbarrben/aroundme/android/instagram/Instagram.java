package es.mbarrben.aroundme.android.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.blinxbox.restig.auth.DialogError;
import com.blinxbox.restig.auth.InstagramAuthDialog;
import com.blinxbox.restig.auth.InstagramAuthDialog.DialogListener;
import com.blinxbox.restinstagram.DefaultInstagramClient;
import com.blinxbox.restinstagram.InstagramClient;
import com.blinxbox.restinstagram.InstagramCollection;
import com.blinxbox.restinstagram.Parameter;
import com.blinxbox.restinstagram.types.MediaPost;

public class Instagram {
    private static final String CLIENT_ID = "5b755d7249b6432bb37d2c5ff2dfaca3";
    private static final String CLIENT_SECRET = "9efc92c0af5a423391520c0ff3a12188";
    private static final String CALLBACK_URL = "aroundme://callback";

    private Activity mActivity;
    private String mAccessToken;
    private DialogListener mExternalDialogListener;
    private InstagramAuthDialog mAuthDialog;
    private InstagramClient mInstagramClient;

    private DialogListener mAuthDialoglistener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            Log.e(getClass().getName(), "Error authenticating: " + error.getMessage());

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onError(error);
            }
        }

        @Override
        public void onComplete(Bundle values) {
            mAccessToken = values.getString("access_token");
            Log.i(getClass().getName(), "access token = " + mAccessToken);

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onComplete(values);
            }
        }

        @Override
        public void onCancel() {
            Log.e(getClass().getName(), "User cancelled auth process");

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onCancel();
            }
        }
    };

    public Instagram(Activity activity) {
        this.mActivity = activity;
    }

    public void setAuthDialogListener(DialogListener listener) {
        this.mAuthDialoglistener = listener;
    }

    public void authorize() {
        authorize(null);
    }

    public void authorize(DialogListener listener) {
        this.mExternalDialogListener = listener;
        mAuthDialog = new InstagramAuthDialog(mActivity, mAuthDialoglistener, Instagram.CLIENT_ID,
                Instagram.CALLBACK_URL);
        mAuthDialog.setCancelable(false);
        mAuthDialog.show();
    }

    public InstagramCollection<MediaPost> fetchNearMediaCollection(double latitude, double longitude, int distanceMetres) {
        final String endPoint = "media/search";
        final Parameter lat = new Parameter("lat", latitude);
        final Parameter lng = new Parameter("lng", longitude);
        final Parameter distance = new Parameter("distance", distanceMetres);
        return getInstagramClient().fetchCollection(endPoint, MediaPost.class, lat, lng, distance);
    }

    private InstagramClient getInstagramClient() {
        if (mInstagramClient == null) {
            mInstagramClient = new DefaultInstagramClient(Instagram.CLIENT_ID, mAccessToken);
        }

        return mInstagramClient;
    }
}
