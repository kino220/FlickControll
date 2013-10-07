package com.example.frickdraw; /**
 * Created by Iplab on 13/10/01.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;


public class GraphicView extends View {

    private int initialPointX, initialPointY,endPointX,endPointY;

    public GraphicView(Context context, int width, int height) {
        super(context);



        initialPointX = (int)(width/2);
        initialPointY = (int)(height/2);
        endPointX = initialPointX;
        endPointY = initialPointY;

    }


    @Override
    public void onDraw(Canvas canvas) {
        // 座標系がわかるような罫線を引く
        Paint paint = new Paint();
        paint.setColor(Color.argb(75, 255, 255, 255));
        paint.setStrokeWidth(1);
        for (int y = 0; y < 800; y = y + 10) {
            canvas.drawLine(0, y, 479, y, paint);
        }
        for (int x = 0; x < 480; x = x + 10) {
            canvas.drawLine(x, 0, x, 799, paint);
        }

        // 線を書く
        /*
        paint.setColor(Color.RED);
        // だんだん大きくしていく。
        for (int i = 1; i <= 10; i++) {
            paint.setStrokeWidth(i);
            canvas.drawLine(i * 20 - 10, 10, i * 20 + 10, 10, paint);
        }
        */

        paint.setColor(Color.rgb(0, 255, 0));

        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);


        //フリックの軌跡描画はこの辺
        canvas.drawLine(initialPointX, initialPointY, endPointX, endPointY, paint);

    }

    public void setInitialPointX(int initialPointX) {
        this.initialPointX = initialPointX - 200;
    }

    public void setInitialPointY(int initialPointY) {
        this.initialPointY = initialPointY - 200;
    }

    public void setEndPointX(int endPointX) {
        this.endPointX =  initialPointX + endPointX ;
    }

    public void setEndPointY(int endPointY) {
        this.endPointY = initialPointY + endPointY ;
    }
}