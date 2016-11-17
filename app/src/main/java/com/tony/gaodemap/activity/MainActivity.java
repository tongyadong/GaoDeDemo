package com.tony.gaodemap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.tony.gaodemap.R;

import com.tony.gaodemap.LocationActivity;



public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump(View view ){
        startActivity(new Intent(this, BasicMapActivity.class));
    }
    public void jump2(View view ){
        startActivity(new Intent(this,WeatherSearchActivity.class));
    }

    public void jump3(View view ){
        startActivity(new Intent(this,LocationActivity.class));
    }


}
