package es.mbarrben.aroundme.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;

import es.mbarrben.aroundme.android.R;

public class AroundMeFragment extends SherlockListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aroundme, container, false);
    }

    @Override
    public void setEmptyText(CharSequence text) {

    }

}
