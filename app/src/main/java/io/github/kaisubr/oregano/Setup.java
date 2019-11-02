package io.github.kaisubr.oregano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Setup extends AppCompatActivity {

    Button manual, automatic;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        automatic = (Button) findViewById(R.id.autobutton);
        manual = (Button) findViewById(R.id.manualbutton);
        nameText = (EditText) findViewById(R.id.nameId);

        automatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                Intent i = new Intent(Setup.this, SetupAuto.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                Intent i = new Intent(Setup.this, SetupAuto.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });
    }
}
