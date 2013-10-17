package com.example.flickdraw;

/**
 * Created by Iplab on 13/10/07.
 */
import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.view.*;
import android.util.*;
import android.graphics.PixelFormat;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {


    static final long FPS = 30;
    static final long FRAME_TIME = 1000 / FPS;

    Thread thread;
    SurfaceHolder holder;

    int screenWidth,screenHeight;
    InputArea inputArea = new InputArea();
    FlickLocus flickLocus = new FlickLocus();

    private boolean isTapDown = false;


   public MySurfaceView(Context context,int width, int height){
       super(context);
       this.holder = getHolder();

       screenWidth = width;
       screenHeight = height;




       this.holder.addCallback(this);
       // 半透明を設定
       this.holder.setFormat(PixelFormat.TRANSLUCENT);
       //サイズの設定
       this.holder.setFixedSize(screenWidth,screenHeight);
       //最前面に設定
       setZOrderOnTop(true);
       Log.i("MySurfaceView","width:"+width+"height:"+height);


   }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("MySurfaceView","create");

        //スレッド作成
        thread = new Thread(this);
        //スレッドスタート
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
        Log.i("MySurfaceView","changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("MySurfaceView","destroy");
        thread = null;
    }

    @Override
    //ループ実行されるメソッド
    //このへんで値を計算してonDrawで描画
    public void run() {
        long loopCount = 0;
        long waitTime = 0;
        long startTime = System.currentTimeMillis();
        Log.i("MySurfaceView","start main loop");

        while(thread != null){

            try {
                loopCount++;
                if(flickLocus.isFinished()) flickLocus = new FlickLocus();
                onDraw(holder);
                waitTime = (loopCount * FRAME_TIME)
                        - System.currentTimeMillis() - startTime;
                if( waitTime > 0 ){
                    Thread.sleep(waitTime);
                }
            }
            catch (Exception e){}
        }
    }

    //描画用メソッド
    private void onDraw(SurfaceHolder holder){
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){

            //背景を透明色で塗りつぶす
            //第二引数省略不可
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


            PointF locus = flickLocus.deriveLocus();

            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            //入力領域の矩形を描く
            canvas.drawRect(inputArea.getArea(), paint);

            //線の幅とアンチエリアスをセット
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(20);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeCap(Paint.Cap.ROUND);


            //Flickの軌跡を描画
            canvas.drawLine((float) flickLocus.getSx(), (float) flickLocus.getSy(), locus.x, locus.y, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }


    public void tapDown(int x, int y){
        Log.i("MySurfaceView","contains? "+inputArea.getArea().contains(x,y));
        if(inputArea.getArea().contains(x,y)){
            flickLocus.setSx(x);
            flickLocus.setSy(y);
            isTapDown = true;
        }
    }

    public void tapUp(int x, int y, long d){
        if(isTapDown){
            flickLocus.setEx(x);
            flickLocus.setEy(y);
            flickLocus.setDuration(d);

        }
        isTapDown = false;
    }


}

