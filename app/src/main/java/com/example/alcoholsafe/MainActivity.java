package com.example.alcoholsafe;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.splashscreen.SplashScreen;




public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        ImageView intro_smile = findViewById(R.id.introSmile);
        TextView intro_name = findViewById(R.id.intro_name);
        TextView intro_text = findViewById(R.id.textView);


        Animation anim1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.falling);
        Animation anim2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.texview);

        intro_smile.startAnimation(anim1);
        intro_name.startAnimation(anim2);
        intro_text.startAnimation(anim2);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);
                finish();
            }
        }, 3000);





    }

}