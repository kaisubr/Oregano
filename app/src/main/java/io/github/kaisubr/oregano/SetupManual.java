package io.github.kaisubr.oregano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SetupManual extends AppCompatActivity {

    EditText rentfood, transport, savings, wants;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_manual);

        setTitle("Oregano - Budget Setup");

        rentfood = (EditText) findViewById(R.id.maneditText1);
        transport = (EditText) findViewById(R.id.maneditTextt2);
        savings = (EditText) findViewById(R.id.maneditTextt3);
        wants = (EditText) findViewById(R.id.maneditText2);

        submit = (Button) findViewById(R.id.manbutton);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            TextView instruct = (TextView) findViewById(R.id.mantextView4);
            instruct.setText(instruct.getText().toString().replace("$n$", name));
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rentfood.length() <= 0 || transport.length() <= 0 || savings.length() <= 0 || wants.length() <= 0) {
                    Toast.makeText(SetupManual.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progress = new ProgressDialog(SetupManual.this);
                    //progress.setTitle("Processing budget");
                    progress.setMessage("Processing your budget...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    long necessities = toLong(rentfood) + toLong(transport);
                    long save = toLong(savings);
                    long lifestyle = toLong(wants);
                    long sum = (necessities + save + lifestyle);

                    Log.d("TAG", "Read the values " + necessities + ", " + save + ", and " + lifestyle);

                    String[] res = new String[]{"" + sum, "" + necessities, "" + save, "" + lifestyle};
                    final Intent i = new Intent(SetupManual.this, BudgetChartActivity.class);
                    i.putExtra("budget", res);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progress.cancel();
                            startActivity(i);
                        }
                    }, 500);
                }
            }
        });
    }

    long toLong(EditText et) {
        return (long)(1.*Double.valueOf(et.getText().toString().replaceAll("[^\\d.]", "")));
    }
}
