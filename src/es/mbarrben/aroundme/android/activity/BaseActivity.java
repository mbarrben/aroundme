package es.mbarrben.aroundme.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import es.mbarrben.aroundme.android.R;

public abstract class BaseActivity extends SherlockFragmentActivity {
    private static final String PREFERENCE_ASK_FOR_GPS = "askForGps";

    private SharedPreferences preferences;

    private Dialog networkDialog;
    private Dialog gpsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    protected boolean isGpsEnabled() throws Exception {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    protected void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_no_internet);
        builder.setTitle(R.string.dialog_no_internet_title);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        networkDialog = builder.create();
        networkDialog.show();
    }

    protected void showEnableGpsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_gps, null);
        builder.setView(view);
        builder.setTitle(R.string.dialog_gps_not_enabled_title);
        builder.setCancelable(false);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.dialog_checkbox);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setHasToAskForGpsAgain(!isChecked);
            }
        });
        builder.setPositiveButton(R.string.yes, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, null);
        gpsDialog = builder.create();
        gpsDialog.show();
    }

    protected boolean hasToAskForGpsAgain() {
        return preferences.getBoolean(PREFERENCE_ASK_FOR_GPS, true);
    }

    protected void setHasToAskForGpsAgain(boolean ask) {
        preferences.edit().putBoolean(PREFERENCE_ASK_FOR_GPS, ask).commit();
    }

    protected boolean canShowNetworkDialog() {
        return networkDialog == null || !networkDialog.isShowing();
    }

    protected boolean canShowGpsDialog() {
        return hasToAskForGpsAgain() && (gpsDialog == null || !gpsDialog.isShowing());
    }
}
