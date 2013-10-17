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
        locus = new PointF();
        counter = 0;
        deltime = 30;
        finished = false;
        line = 0;
        flickDirection = FLICK_NEUTRAL;
        flickPower = FLICK_WEAK;


    }

    public void setSx(int sx) {
        this.sx = sx;
        locus.x = sx;
    }

    public void setSy(int sy) {
        this.sy = sy;
        locus.y = sy;
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

    public PointF deriveLocus(){

        float l = (float) Math.sqrt((ex - sx ) * (ex - sx) + (ey - sy) * (ey - sy));
        float cos = Math.abs((ex - sx) / l);
        float sin = Math.abs((ey - sy) / l);

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

        if(vx == 0 && vy == 0) counter++;
        if(counter > deltime) finished = true;

        return  locus;
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
        PointF locus = new PointF(this.locus.x, this.locus.y);

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
