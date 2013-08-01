package es.mbarrben.aroundme.android.instagram;

import java.util.Comparator;

import android.location.Location;

import com.blinxbox.restinstagram.types.MediaPost;

public class MediaPostComparator implements Comparator<MediaPost> {

    private Location userLocation;

    public MediaPostComparator(Location userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public int compare(MediaPost left, MediaPost right) {
        float[] resultsLeft = new float[3];
        float[] resultsRight = new float[3];

        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), right.getLocation()
                .getLatitude(), right.getLocation().getLongitude(), resultsRight);
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), left.getLocation()
                .getLatitude(), left.getLocation().getLongitude(), resultsLeft);

        if (resultsLeft[0] == resultsRight[0]) {
            return 0;
        } else if (resultsLeft[0] < resultsRight[0]) {
            return -1;
        } else {
            return 1;
        }
    }

}
