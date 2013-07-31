package es.mbarrben.aroundme.android.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.blinxbox.restig.auth.DialogError;
import com.blinxbox.restig.auth.InstagramAuthDialog;
import com.blinxbox.restig.auth.InstagramAuthDialog.DialogListener;

public class Instagram {
    private static final String CLIENT_ID = "5b755d7249b6432bb37d2c5ff2dfaca3";
    private static final String CLIENT_SECRET = "9efc92c0af5a423391520c0ff3a12188";
    private static final String CALLBACK_URL = "aroundme://callback";

    private Activity mActivity;
    private String mAuthToken;
    private DialogListener mExternalDialogListener;
    private InstagramAuthDialog mAuthDialog;

    private DialogListener mAuthDialoglistener = new DialogListener() {

        @Override
        public void onError(DialogError error) {
            Log.e(getClass().getName(), "Error authenticating: " + error.getMessage());

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onError(error);
            }

            // dismissDialogIfNeeded();
        }

        @Override
        public void onComplete(Bundle values) {
            mAuthToken = values.getString("access_token");
            Log.i(getClass().getName(), "access token = " + mAuthToken);

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onComplete(values);
            }

            // dismissDialogIfNeeded();
        }

        @Override
        public void onCancel() {
            Log.e(getClass().getName(), "User cancelled auth process");

            if (mExternalDialogListener != null) {
                mExternalDialogListener.onCancel();
            }

            // dismissDialogIfNeeded();
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

    private void dismissDialogIfNeeded() {
        if (mAuthDialog != null && mAuthDialog.isShowing()) {
            mAuthDialog.dismiss();
        }
    }
}
