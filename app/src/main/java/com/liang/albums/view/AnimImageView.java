package com.liang.albums.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liang on 15/2/17.
 */
public class AnimImageView extends View {

    private static String TAG = "AnimImageView";

//    private Bitmap mBitmap;
    private Bitmap mBitmap;
    private Paint mPaint;

    private int xPivot = 0;
    private int yPivot = 0;
    private int mDirection = 1;

//    private Thread mAnimThread;

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.csy)).getBitmap();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        new Thread(mAnimRunnable, "AnimThread").start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(20);         //canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        //canvas.drawText("Hello View", 0, getHeight() / 2, mPaint);
        canvas.drawBitmap(mBitmap, xPivot, yPivot, mPaint);
//
//        Log.d(TAG, "mBitmap height = "+mBitmap.getHeight()+" width = "+mBitmap.getWidth());
//        Log.d(TAG, "mBitmap height = "+mBitmap.getHeight()+" width = "+mBitmap.getWidth());
//        Log.d(TAG, "canvas  height = "+canvas.getHeight()+" width = "+canvas.getWidth());
//        Log.d(TAG, "view    height = "+getHeight()+" width = "+getWidth());

    }

    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            while(!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }

                if(mBitmap.getWidth()> mBitmap.getHeight()) {
                    // 缩放原图
                    if(mDirection==1){
                        yPivot--;
                        if(yPivot <= getHeight()- mBitmap.getHeight()){
                            mDirection = 0;
                        }
                    }else {
                        yPivot++;
                        if(yPivot >= 0){
                            mDirection = 1;
                        }
                    }
                }else{
                    if(mDirection==1){
                        xPivot--;
                        if(xPivot <= getWidth()- mBitmap.getWidth()){
                            mDirection = 0;
                        }
                    }else {
                        xPivot++;
                        if(xPivot >= 0){
                            mDirection = 1;
                        }
                    }
                }

                //使用PostInvalidate可以直接在线程中更新视图
                postInvalidate();
            }
        }
    };

    public void setBitmap(Bitmap bitmap){
        Bitmap srcBitmap = bitmap;

        if(srcBitmap == null) return;

        // 定义矩阵对象
        Matrix matrix = new Matrix();
        if(srcBitmap.getWidth()>srcBitmap.getHeight()) {
            // 缩放原图
            matrix.postScale(getWidth()/(float)srcBitmap.getWidth(), getWidth()/(float)srcBitmap.getWidth());
        }else{
            matrix.postScale(getHeight()/(float)srcBitmap.getHeight(), getHeight()/(float)srcBitmap.getHeight());
        }
        //bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
        mBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(),
                matrix, true);

        invalidate();
        new Thread(mAnimRunnable, "AnimThread").start();
    }

}
