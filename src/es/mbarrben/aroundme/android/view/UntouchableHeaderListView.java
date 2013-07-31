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

    private int untouchableHeaderRes = 0;

    private View untouchableItem;
    private View behindView;

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

    public void setBehindView(View view) {
        behindView = view;
    }

    private void initUntouchableView(AttributeSet attrs) {
        Context context = getContext();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UntouchableHeaderListView);
        untouchableHeaderRes = a.getResourceId(R.styleable.UntouchableHeaderListView_untouchableViewResource,
                untouchableHeaderRes);
        a.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        untouchableItem = inflater.inflate(untouchableHeaderRes, this, false);
        untouchableItem.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (behindView != null) {
                    behindView.onTouchEvent(event);
                }
                return true;
            }
        });
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

    @Override
    public boolean removeHeaderView(View v) {
        boolean result = false;

        if (getAdapter() != null) {
            result = super.removeHeaderView(v);
        }

        return result;
    }

}
