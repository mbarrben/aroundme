package es.mbarrben.instagramaroundme.android.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import es.mbarrben.instagramaroundme.android.R;
import es.mbarrben.instagramaroundme.android.instagram.Instagram;

public class AroundMeActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aroundme);

        Instagram instagram = new Instagram(this);
        instagram.authorize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.aroundme, menu);
        return true;
    }

}
