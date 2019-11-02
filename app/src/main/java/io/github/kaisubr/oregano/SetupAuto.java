package io.github.kaisubr.oregano;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetupAuto extends AppCompatActivity {

    String name;
    TextView instruct;
    EditText datepick, salary, savings;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_auto);

        instruct = (TextView) findViewById(R.id.textView4);
        datepick = (EditText) findViewById(R.id.editText3);
        salary = (EditText) findViewById(R.id.editText1);
        savings = (EditText) findViewById(R.id.editText2);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            instruct.setText(instruct.getText().toString().replace("$n$", name));
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, y);
                cal.set(Calendar.MONTH, m);
                cal.set(Calendar.DAY_OF_MONTH, d);
                dateEdit();
            }

        };

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SetupAuto.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        datepick.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    new DatePickerDialog(SetupAuto.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        salary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    reformat(salary, "###,###.##");
            }
        });

        savings.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    reformat(savings, "###,###");
            }
        });

    }

    private void reformat(EditText t, String pattern) {
        if (t.getText().toString().length() > 0)
            t.setText(new DecimalFormat(pattern)
                    .format(Double.valueOf(
                                t.getText().toString()
                                .replace(",", "")
                                .replace(".", "")
                            )
                    )
                    .toString()
            );
    }

    private void dateEdit() {
        String ff = "MM/dd/yyyy";
        SimpleDateFormat f = new SimpleDateFormat(ff, Locale.US);
        datepick.setText(f.format(cal.getTime()));
    }
}
