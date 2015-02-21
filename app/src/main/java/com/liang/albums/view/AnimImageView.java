package com.liang.albums.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.liang.albums.R;

/**
 * Created by liang on 15/2/17.
 */
public class AnimImageView extends View {

    private static String TAG = "AnimImageView";

    private Bitmap mBitmap;
    private Bitmap dstbmp;
    private Paint mPaint;

    private int xPivot = 0;
    private int yPivot = 0;
    private int mDirection = 1;

//    private Thread mAnimThread;

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.csy)).getBitmap();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        new Thread(mAnimRunnable, "AnimThread").start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 定义矩阵对象
        Matrix matrix = new Matrix();
        if(mBitmap.getWidth()>mBitmap.getHeight()) {
            // 缩放原图
            matrix.postScale(getWidth()/(float)mBitmap.getWidth(), getWidth()/(float)mBitmap.getWidth());
        }else{
            matrix.postScale(getHeight()/(float)mBitmap.getHeight(), getHeight()/(float)mBitmap.getHeight());
        }
        //bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
        dstbmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),
                matrix, true);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(20);         //canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        //canvas.drawText("Hello View", 0, getHeight() / 2, mPaint);
        canvas.drawBitmap(dstbmp, xPivot, yPivot, mPaint);

        Log.d(TAG, "mBitmap height = "+mBitmap.getHeight()+" width = "+mBitmap.getWidth());
        Log.d(TAG, "dstbmp  height = "+dstbmp.getHeight()+" width = "+dstbmp.getWidth());
        Log.d(TAG, "canvas  height = "+canvas.getHeight()+" width = "+canvas.getWidth());
        Log.d(TAG, "view    height = "+getHeight()+" width = "+getWidth());

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
                    Thread.sleep(200);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }

                if(mBitmap.getWidth()>mBitmap.getHeight()) {
                    // 缩放原图
                    if(mDirection==1){
                        yPivot--;
                        if(yPivot <= getHeight()-dstbmp.getHeight()){
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
                        if(xPivot <= getWidth()-dstbmp.getWidth()){
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

}
