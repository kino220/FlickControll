package com.example.frickdraw;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    float sx = 0, sy =0, tx = 0, ty =0;
    long startTime = 0, endTime = 0;
    private RelativeLayout mainLayout;
    private TextView text;
    private GraphicView gview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);






        //Textview初期化
        text = new TextView(this);
        String str = String.format("StartPoint:(%f,%f) \n" +
                "TerminalPoint:(%f,%f)",sx,sy,tx,ty);
        text.setText(str);

        //mainLayoutにTextViewを追加
        mainLayout.addView(text);

        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display display = wm.getDefaultDisplay();

        //GraphicView初期化
        gview = new GraphicView(this, display.getWidth(), display.getHeight());

        //mainLayoutにGraphicViewを追加
        mainLayout.addView(gview);

        //タッチリスナの設定
        //タップをどのように認識するかの記述はこの辺
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                String motion = "none";
                Log.i("TouchTest",String.valueOf(e.getAction()));
                switch (e.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        sx = e.getX();
                        sy = e.getY();

                        gview.setInitialPointX((int)sx);
                        gview.setInitialPointY((int)sy);

                        Log.i("TouchTest","ActionDown");
                        motion = "Action_Down";
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        tx = e.getX();
                        ty = e.getY();

                        gview.setEndPointX((int)(tx - sx));
                        gview.setEndPointY((int)(ty - sy));

                        gview.invalidate();
                        Log.i("TouchTest","ActionMove");
                        motion = "Action_Move";
                        break;
                    case MotionEvent.ACTION_UP:
                        tx = e.getX();
                        ty = e.getY();

                        gview.setEndPointX((int)(tx - sx));
                        gview.setEndPointY((int)(ty - sy));

                        gview.invalidate();
                        Log.i("TouchTest","ActionUp");
                        motion = "Action_Up";
                        endTime = System.currentTimeMillis();
                        break;

                }

                String str = String.format("StartPoint:(%f,%f) \n" +
                        "TerminalPoint:(%f,%f)\n" +
                        "Movement: %f \n"+
                        "Duration: %f \n" +
                        "motion:"+motion ,sx,sy,tx,ty,(float)Math.sqrt( (double)((tx - sx)*(tx - sx) + (ty - sy)*(ty - sy)) ),(float)(endTime - startTime));
                text.setText(str);


                return true;
            }
        });




    }


/*
//  メニューの記述はこの辺。必要になったら編集するべし
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}
