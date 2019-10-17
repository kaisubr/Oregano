package io.github.kaisubr.oregano;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_splash);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        new CountDownTimer(200, 100) {

            public void onTick(long millisUntilFinished) {
                Log.d("SPLASH", String.valueOf(millisUntilFinished));
            }

            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(i);
                finish();
            }

        }.start();


    }
}
