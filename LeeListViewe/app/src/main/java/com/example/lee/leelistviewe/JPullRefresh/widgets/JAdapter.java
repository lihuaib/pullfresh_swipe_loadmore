package com.example.lee.leelistviewe.JPullRefresh.widgets;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lee.leelistviewe.R;

/**
 * Created by lee on 15/10/26.
 */
public abstract class JAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected RecyclerView.ViewHolder footerViewHolder = null;

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int FOOTER = 1;
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("lee", "onCreateViewHolder");

        if(viewType == VIEW_TYPES.FOOTER) {
            Log.i("lee", "type = footer");

            if(footerViewHolder == null) {
                View loadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progressbar, parent, false);
                loadMoreView.setVisibility(View.GONE);
                footerViewHolder = new FooterViewHolder(loadMoreView);
            }
            return footerViewHolder;
        }

        return getNormalViewHolder(parent);
    }

    @Override
    public int getItemCount() {
        return getAdapaterItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getAdapaterItemCount()) {
            return VIEW_TYPES.FOOTER;
        } else {
            return VIEW_TYPES.NORMAL;
        }
    }

    public void setLoadMore(boolean isLoadMore) {
        if(footerViewHolder != null) {
            footerViewHolder.itemView.setVisibility(isLoadMore ? View.VISIBLE : View.GONE);
        }
    }

    public abstract int getAdapaterItemCount();
    public abstract RecyclerView.ViewHolder getNormalViewHolder(ViewGroup parent);
}
