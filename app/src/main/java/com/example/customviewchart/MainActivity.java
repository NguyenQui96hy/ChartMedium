package com.example.customviewchart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.customviewchart.customview.SpeedView;

public class MainActivity extends AppCompatActivity {
    SpeedView speedView;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedView = findViewById(R.id.speedView);
        speedView.setNameChart("Beats/ min");
        int colorBackground= Color.parseColor("#E2E2E2");
        speedView.setChartBackGroundColor(colorBackground);
        int color = Color.parseColor("#049297");
        speedView.setColorContentChart(color);
        speedView.setChartBackGroundWidth(20);
        //getLowSpeedOffset
        speedView.setMediumSpeedPercent(80);
        // thiết lập giá trị
        speedView.setLowSpeedPercent(20);

        //Thiết lập padding của biểu đồ
        speedView.setIndicatorWidth(10);

       // thiết lập min max giá trị
        speedView.setMaxSpeed(200);
        speedView.setMinSpeed(0);

        speedView.setUnitTextSize(100);
        speedView.setSpeedTextSize(100f);

        speedView.setIconChart(R.drawable.ic_action_heart);

        speedView.setMediumPositionStart(.4f);
        speedView.setMediumPositionEnd(.7f);



        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count ++;
                                speedView.speedTo(count);

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
        speedView.speedTo(0);
    }
}
