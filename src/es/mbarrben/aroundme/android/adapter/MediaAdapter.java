package es.mbarrben.aroundme.android.adapter;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blinxbox.restinstagram.types.MediaPost;
import com.squareup.picasso.Picasso;

import es.mbarrben.aroundme.android.R;
import es.mbarrben.aroundme.android.map.Utils;

public class MediaAdapter extends BindableAdapter<MediaPost> {

    private List<MediaPost> mediaList;

    public MediaAdapter(Context context) {
        super(context);
    }

    public void setMediaList(List<MediaPost> list) {
        this.mediaList = list;
    }

    @Override
    public int getCount() {
        return mediaList == null ? 0 : mediaList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MediaPost getItem(int position) {
        return mediaList == null ? null : mediaList.get(position);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.row_mediapost, container, false);
        MediaRowHolder holder = new MediaRowHolder();
        holder.user = (TextView) view.findViewById(R.id.media_user);
        holder.thumb = (ImageView) view.findViewById(R.id.media_thumb);
        holder.caption = (TextView) view.findViewById(R.id.media_caption);
        holder.userPic = (ImageView) view.findViewById(R.id.media_user_pic);
        holder.distance = (TextView) view.findViewById(R.id.media_distance);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(MediaPost item, int position, View view) {
        MediaPost post = mediaList.get(position);
        MediaRowHolder holder = (MediaRowHolder) view.getTag();

        final String userName = post.getUser().getFullName();
        final String imageUrl = post.getImages().getStandardResolution().getUrl();
        final String caption = post.getCaption().getText();
        final String userPicUrl = post.getUser().getProfilePicture();

        holder.user.setText(userName);
        holder.caption.setText(caption);

        final int distance = getDistanceToUser(post);
        if (distance > 0) {
            holder.distance.setText(getContext().getString(R.string.distance_metres, distance));
            holder.distance.setVisibility(View.VISIBLE);
        } else {
            holder.distance.setText("");
            holder.distance.setVisibility(View.GONE);
        }

        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.placeholder_image).into(holder.thumb);
        Picasso.with(getContext()).load(userPicUrl).placeholder(R.drawable.placeholder_user_pic).into(holder.userPic);
    }

    private int getDistanceToUser(MediaPost post) {
        Location userLocation = Utils.getLastKnownLocation(getContext());
        if (userLocation != null) {
            final double latitude = post.getLocation().getLatitude();
            final double longitude = post.getLocation().getLongitude();
            Location postLocation = new Location(userLocation);
            postLocation.setLatitude(latitude);
            postLocation.setLongitude(longitude);
            return (int) userLocation.distanceTo(postLocation);
        } else {
            return 0;
        }
    }

    private final static class MediaRowHolder {
        TextView user;
        ImageView thumb;
        TextView caption;
        ImageView userPic;
        TextView distance;
    }

}
