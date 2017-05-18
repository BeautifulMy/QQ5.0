package com.myname.slidingmenu;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/1/29.
 */

public class SlidingMenu extends FrameLayout{

    private View menu;
    private View main;
    private int maxWidth;
    private ViewDragHelper viewDragHelper;
    private onSlisingMenuListener listener;

    public SlidingMenu(Context context) {
        this(context,null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxWidth = (int) (getMeasuredWidth() * 0.6f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menu = getChildAt(0);
        main = getChildAt(1);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /**
         * 当控件位置改变的时候
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        if(changedView==menu){
            menu.layout(0,0,menu.getMeasuredWidth(),menu.getMeasuredHeight());
            int newLeft = main.getLeft() + dx;
             newLeft = getNewLeft(newLeft);
            main.layout(newLeft,0,newLeft+main.getMeasuredWidth(),main.getMeasuredHeight());
        }
            float fragction = main.getLeft() * 1f / maxWidth;
            //根据缩放比实现效果
            execAnim(fragction);
            if (listener!=null){
                listener.rotate(fragction);
            }


        }

        /**
         * 捕获触摸事件
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (main.getLeft()>maxWidth/2) {
                viewDragHelper.smoothSlideViewTo(main,maxWidth,0);
                ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
            }else{
                viewDragHelper.smoothSlideViewTo(main,0,0);
                ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /**
         * 设置是否捕获触摸事件
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 水平滑动
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child==main){
              left = getNewLeft(left);

            }
            return left;
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
        }
    }

    FloatEvaluator floatEvaluator =   new FloatEvaluator();
    ArgbEvaluator argbEvaluator =new ArgbEvaluator();
    private void execAnim(float fragction) {
        Float evaluate = floatEvaluator.evaluate(fragction, 1.0f, 0.8f);
        main.setScaleX(evaluate);
        main.setScaleY(evaluate);

        menu.setScaleX(floatEvaluator.evaluate(fragction,0.3f,1.0f));
        menu.setScaleY(floatEvaluator.evaluate(fragction,0.3f,1.0f));
        menu.setTranslationX(floatEvaluator.evaluate(fragction,-menu.getMeasuredWidth()/2,0));

        if (getBackground()!=null){
            int color = (int) argbEvaluator.evaluate(fragction, Color.BLACK, Color.TRANSPARENT);
            getBackground().setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        }


    }

    private int  getNewLeft(int newLeft) {
        if (newLeft<0){
            newLeft=0;
        }else if(newLeft>maxWidth){
                newLeft=maxWidth;
        }
        return newLeft; 
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean b = viewDragHelper.shouldInterceptTouchEvent(ev);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
    public void setOnSlidingmenuListener(onSlisingMenuListener listener){
        this.listener = listener;
    }
    public interface  onSlisingMenuListener{
        public void rotate(float fraction);
        
    }
}
