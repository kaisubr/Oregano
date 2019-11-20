package io.github.kaisubr.oregano;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import io.github.kaisubr.oregano.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    public static int calls = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        calls++;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        prefs = getSharedPreferences("io.github.kaisubr.forecast_budgeting", MODE_PRIVATE);
        if (calls < 1) prefs.edit().putBoolean("first", true).commit();//debug purposes
        else {
            //here we simulate user entering main activity as a returning user.

            Bundle extras = getIntent().getExtras();
            long[] alloc;
            long necessities = 0, savings = 0, lifestyle = 0;

            if (extras != null) {
                long[] allocations = extras.getLongArray("budget");
                alloc = new long[allocations.length];

                for (int i = 0; i < allocations.length; i++) {
                    alloc[i] = Long.valueOf(allocations[i]);
                }

                necessities = alloc[0];
                savings = alloc[1];
                lifestyle = alloc[2];
            }

            Toast.makeText(MainActivity.this, necessities + ", " + savings + ", " + lifestyle, Toast.LENGTH_LONG).show();

            //Let's get the ListView.
            ListView lv = (ListView) findViewById(R.id.listview);
            String[] items = new String[]{"Test"};

            ArrayList<String> theItems = new ArrayList<>();
            theItems.add("[NECESSITIES]\t['House']\t[ -50 USD ]");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listrow, R.id.ListText, theItems); //you have to import io.github.kaisubr.oregano.R to make this work. R.id.ListText is the id to push to in the layout file. If the layout file only has one textview, you can remove this paramter. (github arrayadapter requires the resource id to be a textview)
            //add some more dynamically, using adapter instead of arraylist...
            adapter.add("[SAVINGS]\t['One-time savings']\t[ +400 USD ]");
            adapter.add("[NECESSITIES]\t['Food']\t[ -50 USD ]");

            lv.setAdapter(adapter);

            //more dynamic testing
            adapter.add("[LIEFSTYLE]\t['Video Game']\t[ -74 USD ]");
        }
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
