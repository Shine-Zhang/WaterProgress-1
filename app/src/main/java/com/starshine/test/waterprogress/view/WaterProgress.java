package com.starshine.test.waterprogress.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by starshine on 2014/9/8.
 */
public class WaterProgress extends ImageView {

    /**
     * if true,the view will draw two water at the same time
     */
    private boolean isNeedDoubleWater = true;

    /**
     * a bitmap to cover the water
     */
    private Bitmap mBitmap = null;

    /**
     * the first paint
     */
    private Paint mPaint1 = null;

    /**
     * the second paint
     */
    private Paint mPaint2 = null;

    /**
     * the first water's height
     */
    private float mWaterHeight1 = 100.0f;

    /**
     * the second water's height
     */
    private float mWaterHeight2 = 100.0f;

    /**
     * the first water's width
     */
    private float mWaterWidth1 = 1.0f;

    /**
     * the second water's width
     */
    private float mWaterWidth2 = 1.0f;

    /**
     * the first speed
     */
    private int mSpeed1 = 5;

    /**
     * the second speed
     */
    private int mSpeed2 = 5;

    /**
     * progress
     */
    private float mProgress = 0.0f;

    /**
     * the time to refresh screen
     */
    private int mRefreshTime = 50;

    // do not change following variables,they will be calculate by above variables
    private int mDegree1 = 0; // the first degree
    private int mDegree2 = 0; // the second degree
    private int mCycleDegree1 = 360; // the first cycle degree
    private int mCycleDegree2 = 360; // the second cycle degree
    private Handler mHandler; // the handler
    private Runnable mRunnable; // the runnable

    /**
     * constructed function
     */
    public WaterProgress(Context context) {
        super(context);
        init();
    }

    /**
     * constructed function
     */
    public WaterProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * constructed function
     */
    public WaterProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * init paint
     */
    private void init(){
        mPaint1 = new Paint();
        // AntiAlias
        mPaint1.setAntiAlias(true);
        mPaint1.setFilterBitmap(true);
        // if need the second water,the second paint should init here
        if (isNeedDoubleWater){
            mPaint2 = new Paint();
            // AntiAlias
            mPaint2.setAntiAlias(true);
            mPaint2.setFilterBitmap(true);
        }
    }

    /**
     * is need double water
     */
    public WaterProgress isNeedDoubleWater(boolean isNeed){
        isNeedDoubleWater = isNeed;
        return this;
    }

    /**
     * set the paint to draw the first water
     */
    public WaterProgress setFirstPaint(Paint paint){
        mPaint1 = paint;
        return this;
    }

    /**
     * set the paint to draw the second water
     */
    public WaterProgress setSecondPaint(Paint paint){
        mPaint2 = paint;
        return this;
    }

    /**
     * set the bitmap to cover the water
     */
    public WaterProgress setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        return this;
    }

    /**
     * set the first water's height
     */
    public WaterProgress setFirstWaterHeight(float waterHeight) {
        mWaterHeight1 = waterHeight;
        return this;
    }

    /**
     * set the second water's height
     */
    public WaterProgress setSecondWaterHeight(float waterHeight) {
        mWaterHeight2 = waterHeight;
        return this;
    }

    /**
     * set the first water's width
     */
    public WaterProgress setFirstWaterWidth(float waterWidth) {
        mWaterWidth1 = 1 / waterWidth;
        mCycleDegree1 = (int) (Math.toDegrees(Math.PI * 2) / mWaterWidth1);
        return this;
    }

    /**
     * set the second water's width
     */
    public WaterProgress setSecondWaterWidth(float waterWidth) {
        mWaterWidth2 = 1 / waterWidth;
        mCycleDegree2 = (int) (Math.toDegrees(Math.PI * 2) / mWaterWidth2);
        return this;
    }

    /**
     * set the speed of first water
     */
    public WaterProgress setFirstWaterSpeed(int speed){
        mSpeed1 = speed;
        return this;
    }

    /**
     * set the speed of second water
     */
    public WaterProgress setSecondWaterSpeed(int speed){
        mSpeed2 = speed;
        return this;
    }

    /**
     * set the progress
     */
    public WaterProgress setProgress(float progress) {
        mProgress = progress;
        return this;
    }

    /**
     * set the speed of the water
     */
    public WaterProgress setRefreshTime(int time) {
        mRefreshTime = time;
        return this;
    }

    /**
     * set the alpha,red,green,blue of the first water
     */
    public WaterProgress setFirstWaterARGB(int alpha, int red, int green, int blue){
        mPaint1.setARGB(alpha, red, green, blue);
        return this;
    }

    /**
     * set the alpha,red,green,blue of the second water
     */
    public WaterProgress setSecondWaterARGB(int alpha, int red, int green, int blue){
        mPaint2.setARGB(alpha, red, green, blue);
        return this;
    }

    /**
     * run the water progress
     */
    public void start(){
        // init handler
        if (mHandler == null) {
            mHandler = new Handler();
        }
        // init runnable
        if (mRunnable == null) {
            mRunnable = new WaterRunnable();
        }
        mHandler.post(mRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // clip the canvas to a circle
        Path path = new Path();
        path.addCircle(getWidth() / 2, getHeight() / 2, (getWidth() + getHeight()) / 4 - 2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        // draw water
        for (int width = 0; width < this.getWidth(); width++) {
            int bottom = getHeight();
            if (isNeedDoubleWater) {
                // use formula : h=a*sin(b*(x+t))
                int top2 = getHeight() - (int) (mWaterHeight2 * Math.sin(Math.toRadians(mWaterWidth2 * (width + mDegree2))) + mProgress * getHeight() / 100);
                canvas.drawLine(width, top2, width, bottom, mPaint2);
            }
            // use formula : h=a*sin(b*(x+t))
            int top1 = getHeight() - (int) (mWaterHeight1 * Math.sin(Math.toRadians(mWaterWidth1 * (width + mDegree1))) + mProgress * getHeight() / 100);
            canvas.drawLine(width, top1, width, bottom, mPaint1);
        }
        // draw bitmap
        if (mBitmap != null){
            int left = (getWidth() - mBitmap.getWidth()) / 2;
            int top = (getHeight() - mBitmap.getHeight()) / 2;
            canvas.drawBitmap(mBitmap, left, top, null);
        }
        // show canvas
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap != null) {
            setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
        }
    }

    private class WaterRunnable implements Runnable {
        private final String LOG_TAG = WaterRunnable.class.getSimpleName();

        @Override
        public void run() {
            try {
                // the first water's degree
                if (mDegree1 > mCycleDegree1){
                    // a cycle end
                    mDegree1 -= mCycleDegree1;
                }else{
                    mDegree1 += mSpeed1;
                }
                // the second water's degree
                if (isNeedDoubleWater) {
                    if (mDegree2 > mCycleDegree2) {
                        // a cycle end
                        mDegree2 -= mCycleDegree2;
                    } else {
                        mDegree2 += mSpeed2;
                    }
                }
                // to sleep
                Thread.sleep(mRefreshTime);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "thread sleep error");
                e.printStackTrace();
            }
            postInvalidate();
            mHandler.post(this);
        }
    }
}
