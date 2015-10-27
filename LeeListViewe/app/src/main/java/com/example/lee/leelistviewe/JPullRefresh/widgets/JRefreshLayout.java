package com.example.lee.leelistviewe.JPullRefresh.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by lee on 15/10/26.
 */
public class JRefreshLayout extends SwipeRefreshLayout {

    private final int mTouchSlop;
    private float firstTouchY;
    private float lastTouchY;

    private boolean loadingMore = false;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private JAdapter mJAdapter;
    private LoadingMoreListener mLoadingMoreListener;

    public JRefreshLayout(Context context) {
        this(context, null);
    }

    public JRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
    }

    public void setOnLoadingMoreListener(LoadingMoreListener loadingMoreListener) {
        mLoadingMoreListener = loadingMoreListener;
    }

    public void setLoadingMoreFinish() {
        loadingMore = false;
        setLoadingStatus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                firstTouchY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                lastTouchY = event.getRawY();
                if (canLoadMore()) {
                    onLoadMore();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean canLoadMore() {
        return isBottom() && !loadingMore && isPullingUp();
    }

    private boolean isBottom() {
        int count = mLinearLayoutManager.getItemCount();
        if (count > 0) {
            if (mLinearLayoutManager.findLastVisibleItemPosition() == count - 1 &&
                    mLinearLayoutManager.getChildAt(mLinearLayoutManager.getChildCount() - 1).getBottom() <= mRecyclerView.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPullingUp() {
        return (firstTouchY - lastTouchY) >= mTouchSlop;
    }

    private void onLoadMore() {
        if (mLoadingMoreListener != null) {
            mLoadingMoreListener.onLoadingMore();
            loadingMore = true;

            setLoadingStatus();
        }
    }

    private void setLoadingStatus() {
        mJAdapter = (JAdapter) mRecyclerView.getAdapter();
        mJAdapter.setLoadMore(loadingMore);
    }

    public interface LoadingMoreListener {
        void onLoadingMore();
    }
}
