package com.markpaveszka.pavloswaterfall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HowToPlayActivity extends AppCompatActivity {

    private Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);


        backBtn = (Button) findViewById(R.id.backToMainBtn);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMain = new Intent(HowToPlayActivity.this,
                        MainActivity.class);

                startActivity(backToMain);
                finish();
            }
        });
    }
}
