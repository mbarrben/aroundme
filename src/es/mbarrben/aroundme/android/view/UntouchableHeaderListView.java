package es.mbarrben.aroundme.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import es.mbarrben.aroundme.android.R;

public class UntouchableHeaderListView extends ListView {

    private static final int NO_RESOURCE = 0;

    private View untouchableItem;

    public UntouchableHeaderListView(Context context) {
        this(context, null);
    }

    public UntouchableHeaderListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public UntouchableHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUntouchableView(attrs);
    }

    private void initUntouchableView(AttributeSet attrs) {
        Context context = getContext();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UntouchableHeaderListView);
        final int untouchableHeaderRes = a.getResourceId(R.styleable.UntouchableHeaderListView_untouchableViewResource,
                NO_RESOURCE);
        a.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        untouchableItem = inflater.inflate(untouchableHeaderRes, this, false);
        addHeaderView(untouchableItem);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (untouchableItem != null && event.getY() < untouchableItem.getBottom()
                && event.getY() > untouchableItem.getTop()) {
            return false;
        }

        return super.dispatchTouchEvent(event);
    }

}
