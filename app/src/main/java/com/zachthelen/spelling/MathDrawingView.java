package com.zachthelen.spelling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MathDrawingView extends View {

    private Paint paint;
    private Path path;

    public MathDrawingView(Context context) {
        super(context);
        init();
    }
    public MathDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getRandomColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // Do something when the user lifts their finger
                break;
            default:
                return false;
        }

        invalidate(); // Trigger a redraw
        return true;
    }

    public void clearWork() {
        path.reset();
        invalidate();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getRandomColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        path = new Path();
    }

    private int getRandomColor() {
        int[] backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        int randomIndex = (int) (Math.random() * backgroundColors.length);
        return backgroundColors[randomIndex];
    }
}