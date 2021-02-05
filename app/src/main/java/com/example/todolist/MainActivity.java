package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView ic_logo= findViewById(R.id.ic_logo);
        // this is a final object and not a variable.so, you can call methods on this.
        ic_logo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_in));
        // this happen on top of image view.
        // this code will take 300ms to complete because of animation. animation duration 300ms
        //if splash out is not there this will stay forever.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ic_logo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.splash_out));
                Handler handle = new Handler(Looper.getMainLooper());
                // so we let image view stay for 1200ms and call splash out.splash out take 500ms to complete
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ic_logo.setVisibility(View.GONE);
                        // if you want this to work set fill after as false.
                        Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }
                },500);// this handler post run immediately after splash out.
            }
        },1500);// this make the logo wait for 1.2 sec. which is 1.5 - 0.3
    }

}

