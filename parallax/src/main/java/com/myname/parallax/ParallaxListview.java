package com.myname.parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class ParallaxListview extends ListView {
    private ImageView imageView;
    private int maxHeight;
    private int imageViewHeight;

    public ParallaxListview(Context context) {
        this(context,null);
    }

    public ParallaxListview(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public ParallaxListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setImageView(ImageView imageView){
        this.imageView = imageView;
        //获取设置给imageview的src的图片的高度
        maxHeight = this.imageView.getDrawable().getIntrinsicHeight();
    }

    /**
     *
     * @param deltaX
     * @param deltaY    手指移动的距离,顶部是负值,顶部是正值
     * @param scrollX
     * @param scrollY   移动到Y 的坐标
     * @param scrollRangeX
     * @param scrollRangeY  y轴的移动范围
     * @param maxOverScrollX
     * @param maxOverScrollY    当listview到头之后,Y轴可以移动的最大距离
     * @param isTouchEvent  是否到头,true到头 false 没有到头
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
       //判断listview是不是到头并且是顶部到头
        if(isTouchEvent&&deltaY<0){
            //获取imageview控件的高度
            //huo
            imageViewHeight = getResources().getDimensionPixelSize(R.dimen.imageheight);
            //listview顶部到头更改imageview的高度
            //顶部到头的时候继续下拉,下拉的距离是多少,图片高度就增加多少
            int height = imageView.getHeight() + Math.abs(deltaY);
            //设置图片的范围 如果高度大雨了图片的高度,图片就会放大,图片就会模糊
            if (height>maxHeight){
                height=maxHeight;
            }
            //图片高度的增加得 给imageview布局设置参数
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height= height;
            imageView.setLayoutParams(layoutParams);
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
    //松开手指imageview平缓回复原来的高度的实现

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case  MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case  MotionEvent.ACTION_UP:
                //设置imageview的高度平缓回到原来的高度
                //首先 需要获取imageview原来的高度
                //原来的高度imageviewHeight,现在的高度imageview.getheight()
                //值动画,实现从哪个值执行动画到哪个值的效果
                ValueAnimator valueAnimator = ValueAnimator.ofInt(imageView.getHeight(), imageViewHeight);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (int) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.height=value;
                        imageView.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.setInterpolator(new OvershootInterpolator());
                valueAnimator.setDuration(1000);
                valueAnimator.start();
                break;



        }


        return super.onTouchEvent(ev);
    }
}
