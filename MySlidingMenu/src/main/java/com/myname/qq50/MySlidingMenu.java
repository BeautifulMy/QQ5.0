package com.myname.qq50;

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
 * Created by Administrator on 2017/1/18.
 */

public class MySlidingMenu extends FrameLayout {

    private View menu;
    private View main;
    private ViewDragHelper viewDragHelper;
    private int maxLeft;
    private onSlidingMenuListen listener;

    public MySlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MySlidingMenu(Context context) {
        this(context, null);
    }

    public MySlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//加viewdrawhelper 是为了处理 触摸事件
        //ViewDragHelper是一个用于编写自定义ViewGroups的实用程序类。它提供了一些
        // 有用的操作和状态允许用户拖动和重新定位，追踪
        //他们的父母的ViewGroup之内的意见。
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    /*
     *2017/1/18 18:47
     *当加载完布局结束标签的时候,调用的方法,表示布局加载完控件
     * 这个时候是没有执行测量款到,所以获取不到宽高
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menu = getChildAt(0);
        main = getChildAt(1);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        private int newLeft;

        /*
                         *2017/1/18 18:56
                         *
                         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /**
         * 当view位置改变的时候调用的方法
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //当 滑动内容页的时可以滑动,菜单页的位置不改变 只是缩放
            if (changedView == menu) {
                //设置菜单页位置不能改变
                //对 子控件重新排版,是位置固定
                menu.layout(0, 0, menu.getMeasuredWidth(), menu.getMeasuredHeight());
                //更改内容页面的位置
                newLeft = main.getLeft() + dx;
                newLeft = getLeft(newLeft);
                main.layout(newLeft, 0, main.getMeasuredWidth() + newLeft, main.getMeasuredHeight());
            }

            //根据缩放的百分比进行缩放效果的实现
            float fraction = main.getLeft() * 1f / maxLeft;
            execAnim(fraction);
            //通过回调将百分比传递给activity
            if (listener != null) {
                listener.setRotate(fraction);
                if (fraction == 0f) {

                    listener.setIsOpen(false);
                } else if (fraction == 1f) {
                    listener.setIsOpen(true);
                }
            }
        }

        //捕获触摸事件调用的       @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 手指抬起的时候
         * @param releasedChild
         * @param xvel  速度
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //当手指松开的时候,回弹
            if (main.getLeft() > maxLeft / 2) {
                //平滑回弹效果
                viewDragHelper.smoothSlideViewTo(main, maxLeft, 0);
                //安卓提供的兼容性方法,效果跟invalidate一样是重新触发滚动
                ViewCompat.postInvalidateOnAnimation(MySlidingMenu.this);
            } else {
                viewDragHelper.smoothSlideViewTo(main, 0, 0);
                ViewCompat.postInvalidateOnAnimation(MySlidingMenu.this);
            }
        }

        /**
         * 设置是否强制进行水平滑动,如果需要强制水平滑动,返回大于0的值
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /*
         *2017/1/18 18:57        设置时候可以捕获view的触摸事件
         *  触摸事件的view对象        多点触摸的索引
         *
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * 当view滑动的时候调用的方法
         * @param child
         * @param left  viewdraghelper认为我们要将控件移动的距离
         * @param dx 手指滑动的距离
         * @return实际我们想让控件移动的距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == main) {
                left = getLeft(left);
            }
            return left;
        }
    };

    private void execAnim(float fraction) {
        //floatevaluator float类型的估值器根据百分比计算出对应的值
        //argbRvaluator 颜色的估值器
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        //缩放的值=开始的值+(结束的值-开始的值)*缩放的百分比
        Float evaluate = new FloatEvaluator().evaluate(fraction, 1.0f, 0.8f);
        //首页缩放的操作
        main.setScaleX(evaluate);
        main.setScaleY(evaluate);
        //菜单页 的缩放操作  0.3f->1.0f 除了有缩放 还有偏移从一半开始偏移
        menu.setScaleX(new FloatEvaluator().evaluate(fraction, 0.3, 1.0f));
        menu.setScaleY(new FloatEvaluator().evaluate(fraction, 0.3, 1.0f));
        menu.setTranslationX(new FloatEvaluator().evaluate(fraction, -menu.getMeasuredWidth() / 2, 0));
        //设置背景的滤镜效果  渐变操作
        if (getBackground() != null) {
            int color = (int) argbEvaluator.evaluate(fraction, Color.BLACK, Color.TRANSPARENT);
            //覆盖的类型.PorterDuff.Mode.SRC
            //显示上层绘制图片
            getBackground().setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        }

    }

    //重写 -computescroll才会实现回弹
    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断是否可以继续滑动
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(MySlidingMenu.this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxLeft = (int) (getMeasuredWidth() * 0.6f);
    }

    /**
     * 将触摸事件 传递给viewdraghelper ,方便进行触摸 滑动操作
     * 如果ominterceptouchevent拦截事件,ontouch里面的方法不执行
     * 所以为了保证触摸时间可以完全传递到viewdraghelper 我们需要对两个方法进行重写
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //需要将出迷时间传递给viewdrawhelper
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public int getLeft(int left) {
        if (left < 0) {
            left = 0;
        } else if (left > maxLeft) {
            left = maxLeft;
        }
        return left;
    }

    public void setonSlidingMenuListener(onSlidingMenuListen listener) {
        this.listener = listener;
    }

    //通过设置回调设置imageview的旋转和打开操作  在自定义方法中
    //创建回调方法 将百分比传给activity使用 
    public interface onSlidingMenuListen {
        public void setRotate(float fraction);

        public void setIsOpen(boolean isOpen);

    }

}
