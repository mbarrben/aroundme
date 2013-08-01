package es.mbarrben.aroundme.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;

import es.mbarrben.aroundme.android.R;
import es.mbarrben.aroundme.android.adapter.MediaAdapter;

public class AroundMeFragment extends SherlockListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aroundme, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View header = getLayoutInflater(savedInstanceState).inflate(R.layout.header_pull_up, null);
        getListView().addHeaderView(header);
        setListAdapter(new MediaAdapter(getActivity()));
    }

}
