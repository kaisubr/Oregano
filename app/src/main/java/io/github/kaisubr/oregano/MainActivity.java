package io.github.kaisubr.oregano;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        prefs = getSharedPreferences("io.github.kaisubr.forecast_budgeting", MODE_PRIVATE);
        prefs.edit().putBoolean("first", true).commit();//debug purposes
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("first", true)) {
            Intent i = new Intent(getApplicationContext(), Setup.class);
            startActivity(i);
            prefs.edit().putBoolean("first", false).commit();
            
            finish();
        }
    }
}
