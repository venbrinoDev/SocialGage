package com.company.socialgage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME=2000;
    public ImageView view ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.trnsition_View);
        splashScreen();

    }

    public void splashScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            startReavealActivity(view);

            }
        },SPLASH_TIME);
    }

    private void startReavealActivity(View v){
        int reavealX=(int)(v.getX()+v.getWidth()/2);
        int reavealY=(int)(v.getY()+v.getHeight()/2);

        Intent intent = new Intent(SplashScreen.this, LoginScreen.class);

        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X,reavealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y,reavealY);
        ActivityCompat.startActivity(this,intent,null);

        overridePendingTransition(0,0);

    }
}
