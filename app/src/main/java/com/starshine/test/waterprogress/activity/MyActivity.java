package com.starshine.test.waterprogress.activity;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.starshine.test.waterprogress.R;
import com.starshine.test.waterprogress.view.WaterProgress;


public class MyActivity extends ActionBarActivity {

    private WaterProgress mWaterProgress;
    private int mProgress;
    private Handler mHandler; // the handler
    private Runnable mRunnable; // the runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mWaterProgress = (WaterProgress) findViewById(R.id.wpb);
        mWaterProgress.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_wallpaper))
                .setFirstWaterARGB(255, 255, 0, 0)
                .setFirstWaterHeight(10)
                .setFirstWaterWidth(0.5f)
                .setFirstWaterSpeed(40)
                .isNeedDoubleWater(true)
                .setSecondWaterARGB(128, 0, 255, 0)
                .setSecondWaterHeight(7)
                .setSecondWaterWidth(0.7f)
                .setSecondWaterSpeed(25)
                .setRefreshTime(10)
                .setProgress(mProgress)
                .start();


        mHandler = new Handler();
        mRunnable = new Run();
        mHandler.post(mRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Run implements Runnable{

        @Override
        public void run() {
            try{
                if (mProgress > 100){
                    mProgress -= 100;
                }else{
                    mProgress += 1;
                }
                mWaterProgress.setProgress(mProgress);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.post(this);
        }
    }
}
