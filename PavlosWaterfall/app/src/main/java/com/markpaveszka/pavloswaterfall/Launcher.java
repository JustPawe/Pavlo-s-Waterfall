package com.markpaveszka.pavloswaterfall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Launcher extends AppCompatActivity {

    private ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        /*android.widget.ImageView logoView =(android.widget.ImageView)findViewById(R.id.imageView);

        android.view.Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        int height = size.y;


        //logoView.getLayoutParams().width = height * 2048 / 1356;
        //logoView.requestLayout();*/

        logoView = (ImageView) findViewById(R.id.logoIMGV);


        Thread timer = new Thread(){
            public void run(){
                try{sleep(5000);}
                catch (InterruptedException e) {
                    android.util.Log.e("EXCEPTION", e.getMessage());}
                finally {finishActivity();}
            }
        };
        timer.start();
        startFadeInAnimation(logoView);
    }


    private void finishActivity ()
    {
        android.content.Intent toMainactivityIntent = new android.content.Intent(this,
                MainActivity.class);
        startActivity(toMainactivityIntent);

        this.finish();
    }

    private void startFadeInAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_fade_bottom));

    }
}
