package com.example.lee.leelistviewe.JPullRefresh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lee.leelistviewe.JPullRefresh.widgets.JAdapter;
import com.example.lee.leelistviewe.JPullRefresh.widgets.JItem;
import com.example.lee.leelistviewe.JPullRefresh.widgets.JRefreshLayout;
import com.example.lee.leelistviewe.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    private JRefreshLayout mJRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<String> itemList;
    ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_my);

        mJRefreshLayout = (JRefreshLayout) findViewById(R.id.jrefreshLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mJRefreshLayout.setRecyclerView(mRecyclerView);

        //mJRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mJRefreshLayout.setColorSchemeResources(R.color.blue);
        mJRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter();
                        mJRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mJRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mJRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mJRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mJRefreshLayout.setOnLoadingMoreListener(new JRefreshLayout.LoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                Log.i("lee", "activity on load more");
                setupAdapter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("lee", "activity on load more finish");
                        mJRefreshLayout.setLoadingMoreFinish();
                    }
                }, 3000);
            }
        });
    }

    private void setupAdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (itemList == null) {
                    itemList = new ArrayList<>();
                }

                if (myAdapter == null) {
                    myAdapter = new ArrayAdapter(TestActivity.this, itemList);
                    mRecyclerView.setAdapter(myAdapter);
                }

                int len = itemList.size();
                for (int i = len; i < len + 20; i++) {
                    itemList.add(i + "");
                }

                myAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    public class ArrayAdapter extends JAdapter {

        private List<String> mArray;
        private Context mContext;

        public ArrayAdapter(Context context, List<String> array) {
            mContext = context;
            mArray = array;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (position >= mArray.size()) return;

            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.mTextView.setText(mArray.get(position));

            Log.i("lee", "onBindViewHolder");
        }

        public int getAdapaterItemCount() {
            return mArray.size();
        }

        @Override
        public RecyclerView.ViewHolder getNormalViewHolder(ViewGroup parent) {
            JItem item = new JItem(mContext);
            return new MyViewHolder(item);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }
}
