package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.model.Setting;

public class ZoomableViewGroup extends RelativeLayout {

    private static final int INVALID_POINTER_ID = 1;
    public float mPosX;
    public float mPosY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mScaleFactor = 1;
    private ScaleGestureDetector mScaleDetector;
    private Matrix mScaleMatrix = new Matrix();
    private Matrix mScaleMatrixInverse = new Matrix();
    private Matrix mTranslateMatrix = new Matrix();
    private Matrix mTranslateMatrixInverse = new Matrix();

    private float mLastTouchX;
    private float mLastTouchY;

    private float mFocusY;
    private float mFocusX;

    private float[] mInvalidateWorkingArray = new float[6];
    private float[] mDispatchTouchEventWorkingArray = new float[2];
    private float[] mOnTouchEventWorkingArray = new float[2];


    public ZoomableViewGroup(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mTranslateMatrix.setTranslate(0, 0);
        mScaleMatrix.setScale(1, 1);
    }

    public ZoomableViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mTranslateMatrix.setTranslate(0, 0);
        mScaleMatrix.setScale(1, 1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.layout(child.getLeft(), child.getTop(), child.getLeft() + child.getMeasuredWidth(), child.getTop() + child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDispatchTouchEventWorkingArray[0] = ev.getX();
        mDispatchTouchEventWorkingArray[1] = ev.getY();
        mDispatchTouchEventWorkingArray = screenPointsToScaledPoints(mDispatchTouchEventWorkingArray);
        ev.setLocation(mDispatchTouchEventWorkingArray[0],
                mDispatchTouchEventWorkingArray[1]);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Although the docs say that you shouldn't override this, I decided to do
     * so because it offers me an easy way to change the invalidated area to my
     * likening.
     */
    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {

        mInvalidateWorkingArray[0] = dirty.left;
        mInvalidateWorkingArray[1] = dirty.top;
        mInvalidateWorkingArray[2] = dirty.right;
        mInvalidateWorkingArray[3] = dirty.bottom;


        mInvalidateWorkingArray = scaledPointsToScreenPoints(mInvalidateWorkingArray);
        dirty.set(Math.round(mInvalidateWorkingArray[0]), Math.round(mInvalidateWorkingArray[1]),
                Math.round(mInvalidateWorkingArray[2]), Math.round(mInvalidateWorkingArray[3]));

        location[0] *= mScaleFactor;
        location[1] *= mScaleFactor;
        return super.invalidateChildInParent(location, dirty);
    }

    public void reset(float screenshotScale) {
        mPosX = 0.0f;
        mPosY = 0.0f;
        mFocusX = 0.0f;
        mFocusY = 0.0f;
        mLastTouchX = 0.0f;
        mLastTouchY = 0.0f;

        float values[] = new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
        mTranslateMatrix.setValues(values);
        mTranslateMatrixInverse.setValues(values);
        setScaleFactor(screenshotScale, true);
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float mScaleFactor, boolean force) {
        if (!force) {
            float minZoom = Setting.getFloat(Constants.SETTING_MIN_ZOOM);
            float maxZoom = Setting.getFloat(Constants.SETTING_MAX_ZOOM);

            if (mScaleFactor > maxZoom) {
                mScaleFactor = maxZoom;
            } else if (mScaleFactor < minZoom) {
                mScaleFactor = minZoom;
            }
        }

        this.mScaleFactor = mScaleFactor;
        mScaleMatrix.setScale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        mScaleMatrix.invert(mScaleMatrixInverse);
        invalidate();
    }

    private float[] scaledPointsToScreenPoints(float[] a) {
        mScaleMatrix.mapPoints(a);
        mTranslateMatrix.mapPoints(a);
        return a;
    }

    private float[] screenPointsToScaledPoints(float[] a) {
        mTranslateMatrixInverse.mapPoints(a);
        mScaleMatrixInverse.mapPoints(a);
        return a;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Consume all touches, unless they are action_up, or a small move (a cancel, apparently).
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mOnTouchEventWorkingArray[0] = ev.getX();
            mOnTouchEventWorkingArray[1] = ev.getY();
            mOnTouchEventWorkingArray = scaledPointsToScreenPoints(mOnTouchEventWorkingArray);

            mLastTouchX = mOnTouchEventWorkingArray[0];
            mLastTouchY = mOnTouchEventWorkingArray[1];
            mActivePointerId = ev.getPointerId(0);
            return false;
        }

        return ev.getAction() != MotionEvent.ACTION_UP && ev.getAction() != MotionEvent.ACTION_CANCEL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            mOnTouchEventWorkingArray[0] = ev.getX();
            mOnTouchEventWorkingArray[1] = ev.getY();

            mOnTouchEventWorkingArray = scaledPointsToScreenPoints(mOnTouchEventWorkingArray);

            ev.setLocation(mOnTouchEventWorkingArray[0], mOnTouchEventWorkingArray[1]);
            mScaleDetector.onTouchEvent(ev);

            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;

                    // Save the ID of this pointer
                    mActivePointerId = ev.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);

                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;
                    mTranslateMatrix.preTranslate(dx, dy);
                    mTranslateMatrix.invert(mTranslateMatrixInverse);

                    mLastTouchX = x;
                    mLastTouchY = y;
                    invalidate();

                    break;
                }

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_POINTER_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            if (detector.isInProgress()) {
                mFocusX = detector.getFocusX();
                mFocusY = detector.getFocusY();
            }
            setScaleFactor(mScaleFactor, false);
            requestLayout();

            return true;
        }
    }
}