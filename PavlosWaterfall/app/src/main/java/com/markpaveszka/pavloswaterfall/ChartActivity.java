package com.markpaveszka.pavloswaterfall;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private Button skipBtn, postBtn, saveBtn;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> points = new ArrayList<>();
    private List<PieEntry> pieEntries = new ArrayList<>();
    private final int WRITE_EXTERAL_STORAGE_CODE=1;
    private int time;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        postBtn = (Button) findViewById(R.id.postBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        skipBtn = (Button) findViewById(R.id.skipBtn);

        //GET THE DATA FROM PREVIOUS ACTIVITY
        points = getIntent().getIntegerArrayListExtra("points");
        names = getIntent().getStringArrayListExtra("names");

        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();

        setUpChart();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>()
                {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ChartActivity.this, "Share successful",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ChartActivity.this, "Share cancelled",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(ChartActivity.this, "Share unsuccessful",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Bitmap bitmap = pieChart.getChartBitmap();
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                if(ShareDialog.canShow(SharePhotoContent.class))
                {
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto)
                            .build();
                    shareDialog.show(sharePhotoContent);
                }
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time= (int)System.currentTimeMillis();
                checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        WRITE_EXTERAL_STORAGE_CODE );
                Intent goToMainScreen = new Intent(ChartActivity.this,
                        MainActivity.class);
                startActivity(goToMainScreen);
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMainScreen = new Intent(ChartActivity.this,
                        MainActivity.class);
                startActivity(goToMainScreen);
                finish();
            }
        });







    }

    private void setUpChart()
    {
        for (int i=0; i<names.size(); i++)
        {
            pieEntries.add(new PieEntry(points.get(i), names.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);


        pieChart.getDescription().setEnabled(false);
        pieChart.setData(data);
        pieChart.animateY(1500);
        pieChart.invalidate();
    }


    private void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {

            //File write logic here
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
        else
        {

                pieChart.saveToGallery("pavlo's waterfall"+time, 720);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERAL_STORAGE_CODE:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        pieChart.saveToGallery("pavlo"+time, 720);


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ChartActivity.this,
                            "Permission denied to write your External storage",
                            Toast.LENGTH_SHORT).show();
                }
                return;


            // other 'case' lines to check for other
            // permissions this app might request
        }//SWITCH
    }
}
