package com.myname.deleteslid;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/1/18.
 */

public class SwipDeleteLayout extends FrameLayout {

    private View content;
    private View delete;
    private ViewDragHelper viewDragHelper;
    private int downX;
    private int downY;
    private int moveX;
    private int moveY;
    private OnOpenListen listener;
    private long startTime;

    public SwipDeleteLayout(Context context) {
        this(context, null);
    }

    public SwipDeleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        content = getChildAt(0);
        delete = getChildAt(1);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
        delete.layout(content.getMeasuredWidth(), 0, content.getMeasuredWidth() + delete.getMeasuredWidth(), delete.getMeasuredHeight());
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == content) {
                // 移动删除页面
            /*int newLeft = delete.getLeft()+dx;
                delete.layout(newLeft,0,newLeft+delete.getMeasuredWidth(),delete.getMeasuredHeight());*/
            //本来让 delete跟着content 平移 换简单 的方法时间
                ViewCompat.offsetLeftAndRight(delete,dx);

            }else if(changedView==delete){
                ViewCompat.offsetLeftAndRight(content,dx);
            }
            // 当自定义控件打开关闭的时候，将打开关闭的标示和条目的对象传递到 activity 中，方便进行操作
            if(listener!=null){
                if(content.getLeft()==-delete.getMeasuredWidth()){
                    listener.isOpen(true,SwipDeleteLayout.this);
                }else if(content.getLeft()==0){
                    listener.isOpen(false,SwipDeleteLayout.this);
                }
            }


        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(content.getLeft()>-delete.getMeasuredWidth()/2){
                closeLayout();
            }else{
                openLayout();
            }

        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //水平滑动对 返回进行限制
            if(child==content){
                if (left>0){
                    left=0;
                }else if(left<-delete.getMeasuredWidth()){
                    left=-delete.getMeasuredWidth();
                }
            }else if(child==delete){
                if (left>content.getMeasuredWidth()){
                    left=content.getMeasuredWidth();
                }else if(left<(content.getMeasuredWidth()-delete.getMeasuredWidth())){
                    left=(content.getMeasuredWidth()-delete.getMeasuredWidth());


                }
            }

            return left;
        }

        /**
         * 鸡肋方法 设置强制水平滑动方法
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /*
         *2017/1/18 21:45是否捕获触摸时间
         *
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }
    };

    public  void openLayout() {
        viewDragHelper.smoothSlideViewTo(content,-delete.getMeasuredWidth(),0);
        ViewCompat.postInvalidateOnAnimation(SwipDeleteLayout.this);
    }

    public void closeLayout() {
        viewDragHelper.smoothSlideViewTo(content,0,0);
        ViewCompat.postInvalidateOnAnimation(SwipDeleteLayout.this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断是左右还是上下移动,如果是左右就请求listview不拦截事件,放开事件让条目的自定义控件执行
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                startTime = System.currentTimeMillis();
                break;
            case  MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                if(Math.abs(downX-moveX)>Math.abs(downY-moveY)){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                //因为点击条目的时候,点击事件的按下时间会被自定义控件拦截,
                // 传递不到系统中,系统无法执行到条目的点击事件
                long endTime = System.currentTimeMillis();
                //获取按下和抬起的时间差,判断按下跟抬起之间的时间,实现时间限制
                long time = endTime - startTime;
                //距离的限制
                float dx = event.getX() - downX;
                float dy = event.getY() - downY;
                double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            //获取系统对控件的配置信息的操作
                ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
                //限制时间500秒认为是点击事件
                //距离显示8px
                if(time<viewConfiguration.getLongPressTimeout()&&distance<viewConfiguration.getScaledMaximumDrawingCacheSize()){
                    //实现控件的点击事件
                    performClick();//设置调用view的onclickListener的onclick方法
                }

                break;
        }


        viewDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SwipDeleteLayout.this);
        }
    }
    //实现打开一个条目关闭另一个条目
    public void setOnOpenListener(OnOpenListen listener){
        this.listener=listener;
    }

    public interface OnOpenListen{
        public void isOpen(boolean isOpen,SwipDeleteLayout swipDeleteLayout);
    }
}
