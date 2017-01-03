package com.tony.gaodemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tony.gaodemap.activity.ClusterShowActivity;
import com.tony.gaodemap.activity.LocationActivity;
import com.tony.gaodemap.activity.SimpleUseActivity;
import com.tony.gaodemap.activity.WeatherSearchActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump1(View view ){
        startActivity(new Intent(this, SimpleUseActivity.class));
    }

    public void jump2(View view ){
        startActivity(new Intent(this,WeatherSearchActivity.class));
    }

    public void jump3(View view ){
        startActivity(new Intent(this,LocationActivity.class));
    }

    public void jump4(View view ){
        startActivity(new Intent(this,ClusterShowActivity.class));
    }

}
