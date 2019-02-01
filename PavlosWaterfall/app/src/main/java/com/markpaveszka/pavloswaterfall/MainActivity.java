package com.markpaveszka.pavloswaterfall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button toNamesBtn,  howToPlayBtn;

    private ImageView beer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        beer = (ImageView) findViewById(R.id.beerIMGV);


        toNamesBtn = (Button) findViewById(R.id.toNamesBtn);

        //editRulesBtn = (Button) findViewById(R.id.editRulesBtn);
        howToPlayBtn = (Button) findViewById(R.id.howToPlayBtn);


        startFadeInAnimation(beer);

        toNamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toNames = new Intent(MainActivity.this,
                        AddPlayersActivity.class);

                startActivity(toNames);
                finish();
            }
        });

        howToPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToHowToPlayActivity = new Intent(MainActivity.this,
                        HowToPlayActivity.class);

                startActivity(goToHowToPlayActivity);
                finish();
            }
        });

        /*editRulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editRules = new Intent(MainActivity.this,
                        EditRulesActivity.class);

                startActivity(editRules);
                finish();
            }
        });*/


    }
    private void startFadeInAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_anim));

    }
}
