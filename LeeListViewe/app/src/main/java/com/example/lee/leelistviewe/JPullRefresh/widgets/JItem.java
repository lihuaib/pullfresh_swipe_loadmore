package com.example.lee.leelistviewe.JPullRefresh.widgets;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import com.example.lee.leelistviewe.R;

/**
 * Created by lee on 15/10/23.
 */
public class JItem extends RelativeLayout {

    // 主要布局
    RelativeLayout rlContent;
    // 右边布局
    RelativeLayout rlRightContent;

    private ScrollerCompat mOpenScroller;
    private GestureDetectorCompat mGestureDetector;
    int duration = 200;
    float mDownX = 0;

    public JItem(Context context) {
        this(context, null);
    }

    public JItem(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public JItem(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.comp_jitem, this);

        //mOpenScroller = ScrollerCompat.create(this.getContext(), new BounceInterpolator());
        mOpenScroller = ScrollerCompat.create(this.getContext(), new AccelerateInterpolator());
        rlContent = (RelativeLayout) this.findViewById(R.id.lyContent);
        rlRightContent = (RelativeLayout) this.findViewById(R.id.lyRight);

        bindEvent();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i("lee", "on action up");
            int dis = (int) (mDownX - event.getX());
            swipeEffect(dis);
        }

        return res;
    }

    private void bindEvent() {
        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                Log.i("lee", "on Down");
                mDownX = event.getX();

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                int dpx = (int) (e1.getX() - e2.getX());
                mDownX = e1.getX();

                onSwipe(dpx);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    /*
    * 在移动的过程中， 重新布局整个 item
    * */
    private void onSwipe(int dis) {
        Log.i("lee", "onSwipe dis=" + dis);

        int maxDis = rlRightContent.getWidth();
        boolean toLeft = dis >= 0;
        dis = Math.abs(dis);
        if (dis > maxDis)
            dis = maxDis;

        int rl_left;
        int rl_rleft;
        if (toLeft) { // 向左移动
            rl_left = -dis;
            rl_rleft = rlContent.getWidth() - dis;

            rlContent.layout(rl_left, rlContent.getTop(), rlContent.getWidth() + rl_left, rlContent.getBottom());
            rlRightContent.layout(rl_rleft, rlRightContent.getTop(), rl_rleft + rlRightContent.getWidth(), rlRightContent.getBottom());
        } else { // 向右移动
            rl_left = dis - maxDis;
            rl_rleft = rlContent.getWidth() + dis - maxDis;

            if (rlContent.getLeft() >= 0) return;
            rlContent.layout(rl_left, rlContent.getTop(), rlContent.getWidth() + rl_left, rlContent.getBottom());
            rlRightContent.layout(rl_rleft, rlRightContent.getTop(), rl_rleft + rlRightContent.getWidth(), rlRightContent.getBottom());
        }
    }

    private void swipeEffect(int dis) {
        Log.i("lee", "swipeEffect dis=" + dis);

        boolean toLeft = dis >= 0;
        dis = Math.abs(dis);
        int rwidth = rlRightContent.getWidth();
        if (dis > rwidth) dis = rwidth;

        int rl_left;
        int rl_rleft;

        if (dis >= rwidth / 2) {
            if (toLeft) { // 向左滑动
                rl_left = -rwidth;
                rl_rleft = rlContent.getWidth() - rwidth;
                mOpenScroller.startScroll(-(rwidth - dis), 0, (rwidth - dis), 0, duration);
            } else { // 向右滑动
                rl_left = 0;
                rl_rleft = rlContent.getWidth();
                mOpenScroller.startScroll(rwidth - dis, 0, -(rwidth - dis), 0, duration);
            }
        } else {
            // 恢复到默认位置
            if (toLeft) { // 向左滑动, 则恢复到右边的原来状态
                rl_left = 0;
                rl_rleft = rlContent.getWidth();
                mOpenScroller.startScroll(dis, 0, -dis, 0, duration);
            } else { // 向右滑动 则恢复到左边的原来状态
                rl_left = -rwidth;
                rl_rleft = rlContent.getWidth() - rwidth;
                mOpenScroller.startScroll(-dis, 0, dis, 0, duration);
            }
        }
        rlContent.layout(rl_left, rlContent.getTop(), rlContent.getWidth() + rl_left, rlContent.getBottom());
        rlRightContent.layout(rl_rleft, rlRightContent.getTop(), rl_rleft + rlRightContent.getWidth(), rlRightContent.getBottom());

        invalidate();
    }

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mOpenScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mOpenScroller.getCurrX(), mOpenScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }
}