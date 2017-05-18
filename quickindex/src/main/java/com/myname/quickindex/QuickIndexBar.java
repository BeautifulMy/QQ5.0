package com.myname.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/1/19.
 */

public class QuickIndexBar extends View {
    private String[] letterArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    private int index = -1;
    private Paint paint;
    private float cellHeight;
    private int x;
    int defColor = Color.WHITE;
    /**
     * 文本按住的颜色
     **/
    int pressColor = Color.BLACK;
    private OnPressLetterListener listener;

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letterArr.length; i++) {
            String s = letterArr[i];
            int textHeight = getTextHeight(s);
            cellHeight = getMeasuredHeight() * 1f / letterArr.length;
            x = getMeasuredWidth() / 2;
            float y = cellHeight / 2 + textHeight / 2 + cellHeight * i;
            paint.setColor(index == i ? pressColor : defColor);
            canvas.drawText(s, x, y, paint);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private int getTextHeight(String s) {
        Rect rect = new Rect();
        paint.getTextBounds(s, 0, s.length(), rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int tempindex = (int) (event.getY() / cellHeight);
                if (index != tempindex) {
                    index = tempindex;
                    if (index > -1 && index < letterArr.length) {
                        String letter = letterArr[index];
                        if (listener != null) {
                            listener.getLetter(letter);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                index = -1;
                if (listener != null) {
                    listener.release();
                }
                break;


        }
        invalidate();
        return true;
    }

    public void setOnPressLetterListener(OnPressLetterListener listener) {
        this.listener = listener;
    }

    public interface OnPressLetterListener {
        public void getLetter(String letter);

        public void release();
    }
}
