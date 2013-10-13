package com.example.frickdraw;

import android.graphics.Rect;

/**
 * Created by Satoru on 13/10/14.
 */
public class InputArea {
    int ax,ay,width,height;

    public InputArea(){
        //DefaultArea
        ax = 200;
        ay = 500;
        width = 100;
        height = 100;

    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rect getArea(){
        return new Rect(ax,ay, ax + width, ay + height);
    }

}
