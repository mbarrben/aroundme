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

import es.mbarrben.aroundme.android.R;

public class InstagramManager {
    private Activity activity;
    private String accessToken;
    private DialogListener externalDialogListener;
    private InstagramAuthDialog authDialog;
    private InstagramClient instagramClient;

    private final String clientId;
    private final String clientSecret;
    private final String callbackUrl;

    private DialogListener authDialoglistener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            Log.e(getClass().getName(), "Error authenticating: " + error.getMessage());

            if (externalDialogListener != null) {
                externalDialogListener.onError(error);
            }
        }

        @Override
        public void onComplete(Bundle values) {
            accessToken = values.getString("access_token");
            Log.i(getClass().getName(), "access token = " + accessToken);

            if (externalDialogListener != null) {
                externalDialogListener.onComplete(values);
            }
        }

        @Override
        public void onCancel() {
            Log.e(getClass().getName(), "User cancelled auth process");

            if (externalDialogListener != null) {
                externalDialogListener.onCancel();
            }
        }
    };

    public InstagramManager(Activity activity) {
        this.activity = activity;
        clientId = activity.getString(R.string.instagram_client_id);
        clientSecret = activity.getString(R.string.instagram_client_secret);
        callbackUrl = activity.getString(R.string.instagram_callback_url);
    }

    public void setAuthDialogListener(DialogListener listener) {
        this.authDialoglistener = listener;
    }

    public void authorize() {
        authorize(null);
    }

    public void authorize(DialogListener listener) {
        this.externalDialogListener = listener;
        authDialog = new InstagramAuthDialog(activity, authDialoglistener, clientId, callbackUrl);
        authDialog.setCancelable(false);
        authDialog.show();
    }

    public InstagramCollection<MediaPost> fetchNearMediaCollection(double latitude, double longitude, int distanceMetres) {
        final String endPoint = "media/search";
        final Parameter lat = new Parameter("lat", latitude);
        final Parameter lng = new Parameter("lng", longitude);
        final Parameter distance = new Parameter("distance", distanceMetres);
        return getInstagramClient().fetchCollection(endPoint, MediaPost.class, lat, lng, distance);
    }

    private InstagramClient getInstagramClient() {
        if (instagramClient == null) {
            instagramClient = new DefaultInstagramClient(clientId, accessToken);
        }

        return instagramClient;
    }
}
