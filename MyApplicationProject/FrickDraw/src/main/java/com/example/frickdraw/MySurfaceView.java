package com.example.frickdraw;

/**
 * Created by Iplab on 13/10/07.
 */
import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.*;
import android.os.Bundle;
import android.util.*;
import android.graphics.PixelFormat;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    Thread thread;
    SurfaceHolder holder;
    int screenWidth,screenHeight;
    InputArea inputArea = new InputArea();

   public MySurfaceView(Context context,int width, int height){
       super(context);
       holder = getHolder();
       holder.addCallback(this);
       screenWidth = width;
       screenHeight = height;
       Log.i("MySurfaceView","width:"+width+"height:"+height);



       holder.setFixedSize(screenWidth,screenHeight);

       // 半透明を設定
       getHolder().setFormat(PixelFormat.TRANSLUCENT);
       //最前面に設定
       setZOrderOnTop(true);
   }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new Thread(this);
        thread.start();
    }

    /*
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(screenWidth, screenHeight, oldw, oldh);
        Log.i("onSizeChanged","width:"+getWidth()+"height:"+getHeight());
        //holder.setFixedSize(w,h);

        //Viewのサイズ変更
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = screenHeight;

    }
    */

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int f, int w, int h) {



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread = null;
    }

    @Override
    //ループ実行されるメソッド
    //このへんで値を計算してonDrawで描画
    public void run() {
        if(thread != null){
            onDraw(holder);
        }
    }

    //描画用メソッド
    private void onDraw(SurfaceHolder holder){
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){

            //背景を透明色で塗りつぶす
            //第二引数省略不可
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            canvas.drawRect(inputArea.getArea(),paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}

