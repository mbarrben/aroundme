package es.mbarrben.aroundme.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import es.mbarrben.aroundme.android.R;

public class AroundMeInfoWindowAdapter implements InfoWindowAdapter {

    private Context context;

    public AroundMeInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.infowindow_aroundme, null);
        TextView user = (TextView) view.findViewById(R.id.infowindow_user);
        ImageView pic = (ImageView) view.findViewById(R.id.infowindow_pic);

        user.setText(marker.getTitle());

        Picasso.with(context).load(marker.getSnippet()).placeholder(R.drawable.placeholder_user_pic).into(pic);

        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}
