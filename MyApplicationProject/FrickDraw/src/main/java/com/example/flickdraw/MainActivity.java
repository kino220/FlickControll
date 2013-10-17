package com.example.flickdraw;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.view.View.*;


public class MainActivity extends Activity {

    float sx = 0, sy =0, tx = 0, ty =0;
    long startTime = 0, endTime = 0;
    private RelativeLayout mainLayout;
    private TextView text;
    private GraphicView gview; //試作用
    private MySurfaceView mySurfaceView;
    private WebView myWebView;
    private boolean isScrolled = false;
    private Toast toast = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        //paddingの設定。xmlのほうに直接記述しろ！（後で）
        mainLayout.setPadding(0,0,0,0);

        //webView初期化
        myWebView = new WebView(this);
        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());
        //最初表示するページの設定。
        myWebView.loadUrl("http://www.google.com/");
        myWebView.requestFocus(FOCUS_DOWN);
        myWebView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.addView(myWebView);

        /*
        //Textview初期化
        text = new TextView(this);




        String str = String.format("StartPoint:(%f,%f) \n" +
                "TerminalPoint:(%f,%f)",sx,sy,tx,ty);
        text.setText(str);

        //mainLayoutにTextViewを追加
        mainLayout.addView(text);
*/
        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display display = wm.getDefaultDisplay();

        //GraphicView初期化
        gview = new GraphicView(this, display.getWidth(), display.getHeight());

        //mainLayoutにGraphicViewを追加
        //mainLayout.addView(gview);


        //MySurfaceView初期化
        mySurfaceView = new MySurfaceView(this, display.getWidth(), display.getHeight());
        mySurfaceView.setPadding(0,0,0,0);

        //mainLayout.addView(mySurfaceView);
        addContentView(mySurfaceView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));



        //タッチリスナの設定
        //タップをどのように認識するかの記述はこの辺
        /*
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                String motion = "none";
                Log.i("TouchTest",String.valueOf(e.getAction()));
                switch (e.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        sx = e.getX();
                        sy = e.getY();

                        //gview.setInitialPointX((int)sx);
                        //gview.setInitialPointY((int)sy);

                        //指が触れた位置をセット
                        mySurfaceView.tapDown((int)sx, (int)sy);

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
                        endTime = System.currentTimeMillis();

                        //gview.setEndPointX((int)(tx - sx));
                        //gview.setEndPointY((int)(ty - sy));

                        //指が離れた位置、指が触れていた時間を記憶
                        mySurfaceView.tapUp((int)tx, (int)ty, endTime - startTime);

                        //gview.invalidate();
                        Log.i("TouchTest","ActionUp");
                        motion = "Action_Up";

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
        */



        myWebView.setOnTouchListener(new WebView.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                String motion = "none";
                Log.i("TouchTest", String.valueOf(e.getAction()));
                switch (e.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        sx = e.getX();
                        sy = e.getY();

                        //gview.setInitialPointX((int)sx);
                        //gview.setInitialPointY((int)sy);

                        Log.i("TouchTest", "ActionDown");
                        motion = "Action_Down";
                        startTime = System.currentTimeMillis();

                        //指が触れた位置をセット
                        if (mySurfaceView.inputArea.getArea().contains((int) sx, (int) sy)) {
                            mySurfaceView.tapDown((int) sx, (int) sy);
                            return true;
                        } else {

                            isScrolled = true;
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isScrolled) {
                        } else {
                            tx = e.getX();
                            ty = e.getY();

                            gview.setEndPointX((int) (tx - sx));
                            gview.setEndPointY((int) (ty - sy));

                            gview.invalidate();
                            Log.i("TouchTest", "ActionMove");
                            motion = "Action_Move";
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = System.currentTimeMillis();

                        if (isScrolled) isScrolled = false;
                        else {
                            tx = e.getX();
                            ty = e.getY();


                            if(endTime - startTime < 20) return true;
                            if( Math.sqrt((double) ((tx - sx) * (tx - sx) + (ty - sy) * (ty - sy))) > 300) return true;
                            //gview.setEndPointX((int)(tx - sx));
                            //gview.setEndPointY((int)(ty - sy));

                            //指が離れた位置、指が触れていた時間を記憶
                            mySurfaceView.tapUp((int) tx, (int) ty, endTime - startTime);

                            //gview.invalidate();

                            int action = mySurfaceView.flickLocus.getFlickDraw();

                            switch(action){
                                case 0:

                                    myWebView.reload();
                                    break;
                                case 1:
                                    myWebView.loadUrl("http://www.iplab.cs.tsukuba.ac.jp");
                                    break;
                                case 2:
                                    myWebView.goForward();
                                    break;
                                case 3:
                                    myWebView.goBack();
                                    break;
                                case 4:
                                    String str4 = "ブックマークを表示します";
                                    if(toast != null) toast.cancel();
                                    toast.makeText(getApplicationContext(), str4, Toast.LENGTH_SHORT).show();
                                    break;
                                case 5:
                                    String str = "ブックマークに登録しました";
                                    if(toast != null) toast.cancel();
                                    toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                                    break;
                                case 6:
                                    String str2 = "次のタブに移動します";
                                    if(toast != null) toast.cancel();
                                    toast.makeText(getApplicationContext(), str2, Toast.LENGTH_SHORT).show();
                                    break;
                                case 7:
                                    String str3 = "前のタブに移動します";
                                    if(toast != null) toast.cancel();
                                    toast.makeText(getApplicationContext(), str3, Toast.LENGTH_SHORT).show();
                                    break;
                               default: break;




                            }



                            Log.i("TouchTest", "ActionUp");
                            motion = "Action_Up";
                            return true;
                        }
                        break;

                }


                /*
                String str = String.format("StartPoint:(%f,%f) \n" +
                        "TerminalPoint:(%f,%f)\n" +
                        "Movement: %f \n" +
                        "Duration: %f \n" +
                        "motion:" + motion, sx, sy, tx, ty, (float) Math.sqrt((double) ((tx - sx) * (tx - sx) + (ty - sy) * (ty - sy))), (float) (endTime - startTime));
                text.setText(str);
                */
                mainLayout.requestFocus(FOCUS_DOWN);

                return false;
            }


        });

        myWebView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                myWebView.requestFocus(FOCUS_DOWN);
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
