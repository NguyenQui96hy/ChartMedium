package com.example.customviewchart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.chartmightysat.MightySatChart;
import com.example.customviewchart.custom.BadgeDrawable;



public class MainActivity extends AppCompatActivity {
    MightySatChart speedView;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedView = findViewById(R.id.speedView);
        speedView.setIndicatorWidth(10);

       // thiết lập min max giá trị
        speedView.setMaxSpeed(200);
        speedView.setMinSpeed(0);

        speedView.setUnitTextSize(100);
        speedView.setSpeedTextSize(50);

        speedView.setIconChart(R.drawable.ic_action_heart);

        speedView.setMediumPositionStart(30);
        speedView.setMediumPositionEnd(200);
//        speedView.setIndicatorColor(Color.BLACK);



        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isInterrupted()){
                    try {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count ++;
                                speedView.speedTo(count);
                                speedView.setMediumPositionStart(30);
                                if(count<=100){
                                    speedView.setMediumPositionEnd(60+count);
                                }

                            }
                        });
                    }
                    catch (Exception ex){

                    }
                }
            }
        };
        thread.start();

    }


    public void onClick(View view) {

        speedView.setMediumPositionStart(100);
        speedView.setMediumPositionEnd(120);
        speedView.speedTo(0);
    }
    public void onClick2(View view) {

        speedView.setMediumPositionStart(120);
        speedView.setMediumPositionEnd(130);
        speedView.speedTo(0);
    }
    public void onClick3(View view) {

    }
}
