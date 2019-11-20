package io.github.kaisubr.oregano;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Setup extends Activity {

    Button manual, automatic;
    EditText nameText;
    RelativeLayout header, input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setup);

        automatic = (Button) findViewById(R.id.autobutton);
        manual = (Button) findViewById(R.id.manualbutton);
        nameText = (EditText) findViewById(R.id.nameId);
        header = (RelativeLayout) findViewById(R.id.relativeLayout5);
        input = (RelativeLayout) findViewById(R.id.relativeLayout);

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
                Intent i = new Intent(Setup.this, SetupManual.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });
    }
}
