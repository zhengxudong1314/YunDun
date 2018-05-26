package com.dahua.searchandwarn.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 创建： ZXD
 * 日期 2018/5/9
 * 功能：
 */

/**
 * 手动控制viewpager是否可以左右滑动
 * 去除滑动动画
 * setNoScroll(boolean noScroll)
 *
 * @author ex-panyong
 */
public class SW_NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;

    public SW_NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public SW_NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
                /* return false;//super.onTouchEvent(arg0); */
        switch(arg0.getAction()){
        }


        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //false 去除滚动效果
        super.setCurrentItem(item,false);
    }

}


