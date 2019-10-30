package com.sonicers.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.sonicers.commonlib.R;
import com.sonicers.commonlib.util.ToastUtil;

/**
 * SteerView for IR Control or others
 */
public class SteerView extends View {

    public static final int TOP_PRESS = 1;
    public static final int LEFT_PRESS = 2;
    public static final int BOTTOM_PRESS = 3;
    public static final int RIGHT_PRESS = 4;

    public static final int NONE_PRESS = -1;

    private int pressDirection = NONE_PRESS;

    private static final String TAG = SteerView.class.getSimpleName();

    private int ovalColor = -1;
    private int pressColor = -1;

    private Drawable drawLeft = null;
    private Drawable drawRight = null;
    private Drawable drawTop = null;
    private Drawable drawBottom = null;

    public SteerView(Context context) {
        super(context);

        initWork();
    }

    private void initWork() {
        if (getBackground() == null) {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

        defaultParentSize = dp2px(getContext(), 100);
    }

    public SteerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAzimuthCircleAttrs(attrs);
        initWork();
    }

    private void getAzimuthCircleAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SteerView);
        ovalColor = typedArray.getResourceId(R.styleable.SteerView_ovalColor, -1);
        pressColor = typedArray.getResourceId(R.styleable.SteerView_pressColor, -1);
        drawLeft = typedArray.getDrawable(R.styleable.SteerView_leftImg);
        drawRight = typedArray.getDrawable(R.styleable.SteerView_rightImg);
        drawTop = typedArray.getDrawable(R.styleable.SteerView_topImg);
        drawBottom = typedArray.getDrawable(R.styleable.SteerView_bottomImg);
    }

    private int defaultParentSize;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //parent size
        int maxParentSize = defaultParentSize;

        maxParentSize = Math.max(maxParentSize, getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec));
        setMeasuredDimension(maxParentSize, maxParentSize);
    }

    private boolean shouldIntercept(float x, float y) {
        int centerPoint = getMeasuredWidth() / 2;
        double minSqr = 0;

        float calculateX = x - centerPoint;
        float calculateY = y - centerPoint;

        Log.i(TAG, "centerPoint：" + centerPoint + " -- calculateX:" + calculateX + "calculateY:" + calculateY);

        double calculateSqr = Math.sqrt(Math.pow(calculateX, 2) + Math.pow(calculateY, 2));
        double centerSqr = centerPoint;

        Log.i(TAG, "calculateSqr：" + calculateSqr + " -- centerSqr:" + centerSqr + "minSqr:" + minSqr);

        return !(calculateSqr >= centerSqr || calculateSqr <= minSqr);
    }

    private boolean consumeTouchEvent;

    private boolean antiTouch;

    public void antiTouch(boolean b) {
        antiTouch = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (antiTouch) {
            ToastUtil.showToastShort(getContext(), "请停止监火方能操作~");
            return true;
        }
        super.onTouchEvent(event);
        Log.i(TAG, "event:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressDirection = getPressDirection(event.getX(), event.getY());
                consumeTouchEvent = shouldIntercept(event.getX(), event.getY());
                Log.i(TAG, "pressDirection:" + pressDirection);
                if (listener != null && (pressDirection != NONE_PRESS)) {
                    listener.onPressDirection(pressDirection);
                }
                if (consumeTouchEvent) {
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (consumeTouchEvent) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unClick();
                        }
                    }, 50);
                    if (SteerView.this.listener != null) {
                        listener.onRelease();
                    }
                }

                break;
        }

//        Log.i(TAG, "consumeTouchEvent:" + consumeTouchEvent);
//        LogAs.i(TAG, "onTouch:" + onTouch);

        return consumeTouchEvent;
    }

    private void unClick() {
        pressDirection = NONE_PRESS;
        invalidate();
    }

    @Override
    public boolean performClick() {
        boolean result = super.performClick();
        unClick();
        return result;
    }

    public int getPressDirection() {
        return pressDirection;
    }

    //measure press area
    private int getPressDirection(float x, float y) {
        int centerPoint = getMeasuredWidth() / 2;

        float calculateX = x - centerPoint;
        float calculateY = y - centerPoint;

        if (!shouldIntercept(x, y)) {
            return NONE_PRESS;
        }

        double targetTan = (double) calculateY / calculateX;
        double targetDegree = Math.toDegrees(Math.atan(targetTan));

        if (calculateX > 0 && calculateY < 0) {
            targetDegree += 360;
        } else if (calculateX < 0) {
            targetDegree += 180;
        }

//        LogAs.i(TAG, "targetTan：" + targetTan + " -- targetDegree:" + targetDegree);
        if (targetDegree < 225 && targetDegree >= 135) {
            return LEFT_PRESS;
        } else if (targetDegree < 315 && targetDegree >= 225) {
            return TOP_PRESS;
        } else if (targetDegree > 315) {
            return RIGHT_PRESS;
        } else if (targetDegree < 135 && targetDegree >= 45) {
            return BOTTOM_PRESS;
        } else {
            return RIGHT_PRESS;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawDirectImage(canvas);
        super.onDraw(canvas);
    }

    private void drawDirectImage(Canvas canvas) {
        int painDistance = dp2px(getContext(), 12);

        if (drawLeft != null) {
            drawLeft.setBounds(painDistance, getMeasuredWidth() / 2 - drawLeft.getIntrinsicHeight() / 2, drawLeft.getIntrinsicWidth() + painDistance, getMeasuredWidth() / 2 + drawLeft.getIntrinsicHeight() / 2);
            drawLeft.draw(canvas);
        }

        if (drawRight != null) {
            drawRight.setBounds(getMeasuredWidth() - painDistance - drawRight.getIntrinsicWidth(), getMeasuredWidth() / 2 - drawRight.getIntrinsicHeight() / 2, getMeasuredWidth() - painDistance, getMeasuredWidth() / 2 + drawRight.getIntrinsicHeight() / 2);
            drawRight.draw(canvas);
        }

        if (drawTop != null) {
            drawTop.setBounds(getMeasuredWidth() / 2 - drawTop.getIntrinsicWidth() / 2, painDistance, getMeasuredWidth() / 2 + drawTop.getIntrinsicWidth() / 2, painDistance + drawTop.getIntrinsicHeight());
            drawTop.draw(canvas);
        }

        if (drawBottom != null) {
            drawBottom.setBounds(getMeasuredWidth() / 2 - drawBottom.getIntrinsicWidth() / 2, getMeasuredWidth() - drawBottom.getIntrinsicHeight() - painDistance, getMeasuredWidth() / 2 + drawBottom.getIntrinsicHeight() / 2, getMeasuredWidth() - painDistance);
            drawBottom.draw(canvas);
        }
    }

    public int makeAlpha(int alpha, @ColorInt int color) {
        int blue = Color.blue(color);
        int green = Color.green(color);
        int red = Color.red(color);
        int resultColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            resultColor = Color.argb(alpha, red, green, blue);
        } else {
            resultColor = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        return resultColor;
    }

    public int dp2px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    private Paint circlePaint = null;
    private Paint crossPaint = null;
    private RectF rectF = null;

    private void drawCircle(Canvas canvas) {
        if (ovalColor <= 0 || pressColor <= 0) {
            return;
        }

        int centerPoint = getMeasuredWidth() / 2;
        canvas.save();
        canvas.rotate(45, centerPoint, centerPoint);

        if (circlePaint == null) {
            int paintWidth = dp2px(getContext(), 1);
            circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setStrokeWidth(paintWidth);
            circlePaint.setStyle(Paint.Style.FILL);
        }

        int angle = -1;

        Log.d(TAG, "pressDirection===" + pressDirection);

        switch (pressDirection) {
            case TOP_PRESS:
                angle = 180;
                break;
            case RIGHT_PRESS:
                angle = -90;
                break;
            case BOTTOM_PRESS:
                angle = 0;
                break;
            case LEFT_PRESS:
                angle = 90;
                break;
        }

        if (rectF == null) {
            rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        for (int i = -90; i < 270; i = i + 90) {
            if (i != angle) {
                circlePaint.setColor(getResources().getColor(ovalColor));
                canvas.drawArc(rectF, i, 90, true, circlePaint);
            } else {
                circlePaint.setColor(getResources().getColor(pressColor));
                canvas.drawArc(rectF, i, 90, true, circlePaint);
            }
        }

        int paintWidth = dp2px(getContext(), 4);
        if (crossPaint == null) {
            crossPaint = new Paint();
            crossPaint.setAntiAlias(true);
            crossPaint.setStrokeWidth(paintWidth);
            crossPaint.setStyle(Paint.Style.STROKE);
            crossPaint.setColor(Color.parseColor("#3388ff"));
        }

        canvas.drawLine(centerPoint, 0, centerPoint, getMeasuredHeight(), crossPaint);
        canvas.drawLine(0, getMeasuredHeight() / 2f, getMeasuredWidth(), getMeasuredHeight() / 2f, crossPaint);

        canvas.rotate(-45);
        canvas.restore();
    }

    public interface ISteerListener {

        void onPressDirection(int direction);

        void onRelease();
    }

    ISteerListener listener;

    public void setSteerListener(ISteerListener l) {
        this.listener = l;
    }
}
