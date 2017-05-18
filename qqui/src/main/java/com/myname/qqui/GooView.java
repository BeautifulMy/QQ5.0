package com.myname.qqui;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/1/23.
 */

public class GooView extends View {

    private double linx;
    private Paint paint;
    /**
     * 拖拽的最大距离
     **/
    float maxInstance = 80;
    private boolean isDraw;


    public GooView(Context context) {
        this(context, null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    //拖拽圆
    PointF dragCenter = new PointF(100, 150);
    float dragRadius = 10;
    //固定圆
    PointF stickyCenter = new PointF(150, 150);
    float stickyRadius = 10;
    PointF controlCenter = new PointF(125, 150);
    PointF[] dragCenters = new PointF[]{new PointF(100, 140), new PointF(100, 160)};
    PointF[] stickyCenters = new PointF[]{new PointF(150, 140), new PointF(150, 160)};

    @Override
    protected void onDraw(Canvas canvas) {
        stickyRadius = getStickyRedius();
        float dy = dragCenter.y - stickyCenter.y;
        float dx = dragCenter.x - stickyCenter.x;
        if (dx != 0) {
            linx = dy / dx;
        }
        //调用工具类实现坐标的换算
        dragCenters = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, linx);
        stickyCenters = GeometryUtil.getIntersectionPoints(stickyCenter, stickyRadius, linx);
        controlCenter = GeometryUtil.getPointByPercent(dragCenter, stickyCenter, 0.618f);

        //绘制一个空心的圆,半径是最大拖拽距离,圆心跟固定元一样
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(stickyCenter.x, stickyCenter.y, maxInstance, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, paint);
        if (!isDraw) {

            canvas.drawCircle(stickyCenter.x, stickyCenter.y, stickyRadius, paint);
            Path path = new Path();
            path.moveTo(stickyCenters[0].x, stickyCenters[0].y);
            path.quadTo(controlCenter.x, controlCenter.y, dragCenters[0].x, dragCenters[0].y);
            path.lineTo(dragCenters[1].x, dragCenters[1].y);
            path.quadTo(controlCenter.x, controlCenter.y, stickyCenters[1].x, stickyCenters[1].y);
            canvas.drawPath(path, paint);
        }

    }

    private float getStickyRedius() {
        float distance = GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter);
        float v = distance / maxInstance;
        return new FloatEvaluator().evaluate(v, 10, 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //按住哪里就设置圆点在哪里

                dragCenter.set(event.getX(), event.getY());
                isDraw = GeometryUtil.getDistanceBetween2Points(dragCenter, stickyCenter) > maxInstance;
                break;
            case MotionEvent.ACTION_UP:
                if (isDraw) {
                    //超出返回拖拽圆挥发哦原点,同时实现爆炸效果
                    palyAnim(dragCenter.x, dragCenter.y);
                    dragCenter.set(stickyCenter.x, stickyCenter.y);
                } else {
                    //平缓回弹
                    //获取圆心位置
//                    因为随着拖拽元的拖拽,圆心改变,
                    final PointF pointF = new PointF(dragCenter.x, dragCenter.y);//
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 3);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //获取更新动画更新的百分比
                            float fraction = valueAnimator.getAnimatedFraction();
                            //根据动画的百分比,获取对应的点,将拖拽圆的坐标改为获取的点的坐标
                            PointF p = GeometryUtil.getPointByPercent(pointF, stickyCenter, fraction);
                            dragCenter.set(p.x, p.y);
                            invalidate();
                        }
                    });
                    valueAnimator.setInterpolator(new OvershootInterpolator());
                    valueAnimator.setDuration(500);
                    valueAnimator.start();

                }
                break;

        }
        invalidate();
        return true;
    }

    private void palyAnim(float x, float y) {
        //创建imageview将帧动画设置给imageview做北京
        final ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(70, 70));
        imageView.setBackgroundResource(R.drawable.anim);
        //获取帧动画执行帧动画
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
        final ViewGroup parent = (ViewGroup) getParent();
        parent.addView(imageView);
        imageView.setTranslationX(x - 35);
        imageView.setTranslationY(y - 35);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.removeView(imageView);
            }
        }, 600);

    }
}
