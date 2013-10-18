package com.example.flickdraw;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by iplab on 13/10/15.
 */
public class FlickLocus implements Cloneable {
    private int sx, sy, ex, ey;
    private long duration;
    private float vx, vy, acceleration;
    private PointF locus;
    private float locusX,locusY;
    private float mag = 20;
    private int counter;
    private int deltime;
    private  boolean finished;
    private float line;
    private int flickDirection;
    private int flickPower;

    public static final int FLICK_NEUTRAL = -1;
    public static final int FLICK_UP = 0;
    public static final int FLICK_DOWN = 1;
    public static final int FLICK_RIGHT = 2;
    public static final int FLICK_LEFT = 3;
    public static final int FLICK_WEAK = 4;
    public static final int FLICK_STRONG = 5;

    private boolean isNAN = false;


    public FlickLocus(){
        sx = 0;
        sy = 0;
        ex = 0;
        ey = 0;
        duration = 0;
        vx = 0;
        vy = 0;
        acceleration = -1;
        locus = new PointF(0,0);
        locusX = 0;
        locusY = 0;
        counter = 0;
        deltime = 30;
        finished = false;
        line = 0;
        flickDirection = FLICK_NEUTRAL;
        flickPower = FLICK_WEAK;


    }

    public void setSx(int sx) {
        this.sx = sx;
        //locus.x = (float)sx;
    }

    public void setSy(int sy) {
        this.sy = sy;
        //locus.y = (float)sy;
    }

    public void setLocus(PointF locus) {
        this.locus = locus;

    }

    public void setLocusX(float locusX) {
        this.locusX = locusX;
    }

    public void setLocusY(float locusY) {
        this.locusY = locusY;
    }



    public int getSx() {
        return sx;
    }

    public int getSy() {
        return sy;
    }

    public void setEx(int ex) {
        this.ex = ex;
    }

    public void setEy(int ey) {
        this.ey = ey;
    }

    public float getLocusX() {
        return locusX;
    }

    public float getLocusY() {
        return locusY;
    }

    /*
            必ず、sx,sy,ex,eyをセットした後に実行すること！
             */
    public void setDuration(long duration) {
        this.duration = duration;
        vx = mag * (float)(Math.abs(ex - sx))/ (float)duration;
        vy = mag * (float)(Math.abs(ey - sy)) / (float)duration;


        Log.i("FlickLocus","vx,vy:" + vx+","+vy);

        calcDirection();
        calcPower();

        counter = 0;
    }


    public  boolean isFinished(){
        return  finished;
    }

    public void deriveLocus(){

        float l = (float) Math.sqrt((ex - sx ) * (ex - sx) + (ey - sy) * (ey - sy));
        Float cos = Math.abs((ex - sx) / l);
        float sin = Math.abs((ey - sy) / l);

        if(!(cos.isNaN())){
            vx += acceleration * cos;
            vy += acceleration * sin;

            if(vx < 0)  vx = 0;
            if(vy < 0)  vy = 0;



            if(sx < ex){
                locusX += vx;
            }else {
                locusX -= vx;
            }

            if(sy < ey){
                locusY += vy;
            }else{
                locusY -= vy;
            }

            if(vx == 0 && vy == 0) counter++;
            if(counter > deltime) finished = true;
        }

    }

    //フリックの方向を決定
    private void calcDirection(){
        float mx = ex -sx;
        float my = ey - sy;

        if(Math.abs(mx) > Math.abs(my)){
            if (mx > 0) flickDirection = FLICK_RIGHT;
            else flickDirection = FLICK_LEFT;
        }
        else{
            if(my < 0) flickDirection = FLICK_UP;
            else flickDirection = FLICK_DOWN;

        }

    }


    //フリックの強さを決定
    private  void calcPower(){
        float l = (float) Math.sqrt((ex - sx ) * (ex - sx) + (ey - sy) * (ey - sy));
        float cos = Math.abs((ex - sx) / l);
        float sin = Math.abs((ey - sy) / l);

        float vx = this.vx;
        float vy = this.vy;


        float test = (float)sx;

        PointF locus = new PointF((float)sx, (float)sy);


        int counter = 0;



        while(vx > 0 && vy > 0){

            if(counter > 600) break;

            counter++;

            vx += acceleration * cos;
            vy += acceleration * sin;

            if(vx < 0)  vx = 0;
            if(vy < 0)  vy = 0;

            if(sx < ex){
                locus.x += vx;
            }else {
                locus.x -= vx;
            }

            if(sy < ey){
                locus.y += vy;
            }else{
                locus.y -= vy;
            }
        }

        Float p = (float) Math.sqrt((locus.x - (float)sx ) * (locus.x - (float)sx) + (locus.y - (float)sy) * (locus.y - (float)sy));

        Log.i("FlickLocus","movement:" + p);

        if(p.isNaN()) isNAN =true;

        //フリックの強弱を描いた線の長さによって判別
        if( p < 200) flickPower = FLICK_WEAK;
        else flickPower = FLICK_STRONG;

    }



    public int getFlickDraw(){
        int flickDraw = -1;
        if(isNAN){
            isNAN = false;
            return -1;
        }

        switch (flickDirection){
            case FLICK_UP:
                if (flickPower == FLICK_WEAK) flickDraw = 0;
                    else flickDraw = 4;
                break;
            case FLICK_DOWN:
                if (flickPower == FLICK_WEAK)  flickDraw = 1;
                else flickDraw = 5;
                break;
            case FLICK_RIGHT:
                if (flickPower == FLICK_WEAK) flickDraw = 2;
                else flickDraw = 6;
                break;
            case FLICK_LEFT:
                if (flickPower == FLICK_WEAK) flickDraw = 3;
                else flickDraw = 7;
                break;
            default: break;
        }
        Log.i("FlickLocus","flickDraw:" + flickDraw);
        return flickDraw;
    }
}
