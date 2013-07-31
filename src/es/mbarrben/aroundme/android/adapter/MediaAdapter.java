package es.mbarrben.aroundme.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blinxbox.restinstagram.types.MediaPost;
import com.squareup.picasso.Picasso;

import es.mbarrben.aroundme.android.R;

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
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(MediaPost item, int position, View view) {
        MediaPost post = mediaList.get(position);
        MediaRowHolder holder = (MediaRowHolder) view.getTag();

        final String userName = post.getUser().getFullName();
        final String imageUrl = post.getImages().getThumbnail().getUrl();

        holder.user.setText(userName);

        Picasso.with(getContext()).load(imageUrl).into(holder.thumb);
    }

    private final static class MediaRowHolder {
        TextView user;
        ImageView thumb;
    }

}
