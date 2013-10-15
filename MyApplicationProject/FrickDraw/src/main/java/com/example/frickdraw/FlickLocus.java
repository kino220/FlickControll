package com.example.frickdraw;

import android.graphics.PointF;

/**
 * Created by iplab on 13/10/15.
 */
public class FlickLocus implements Cloneable {
    private int sx, sy, ex, ey;
    private long duration;
    private float vx, vy, acceleration;
    private PointF locus;
    private float mag = 30;
    private int counter;
    private int deltime;
    private  boolean finished;
    private float line;


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

    public void setDuration(long duration) {
        this.duration = duration;
        vx = mag * (float)(Math.abs(ex - sx))/ (float)duration;
        vy = mag * (float)(Math.abs(ey - sy)) / (float)duration;



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


}
