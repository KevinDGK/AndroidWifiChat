package com.dgk.wifichat.module.main.recycleview;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Recycle的条目点击响应监听类
 *   如果想要给recycle设置条目点击监听，有两种方式：
 *      - 第一种，在Adapter中写接口，然后在Activity实现，通过接口回调的方式实现；
 *               这种方式可以监听条目中的个别控件，比如按钮和头像等；
 *      - 第二种，给recycleview设置ItemTouchListener，传入本类的实现类，通过判断手势，来实现点击条目的事件传递。
 *               这种方式可以监听的是点击整个条目的响应。
 */
public abstract class RecycleItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public RecycleItemTouchListener(RecyclerView rv) {
        this.recyclerView = rv;
        this.mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),
                new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh, int position);

    public abstract void onLongClick(RecyclerView.ViewHolder vh, int position);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int pos = recyclerView.getChildAdapterPosition(child);
                onLongClick(vh, pos);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                int pos = recyclerView.getChildAdapterPosition(child);
                onItemClick(vh, pos);
            }
            return true;
        }
    }
}
