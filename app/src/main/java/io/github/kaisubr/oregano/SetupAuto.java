package io.github.kaisubr.oregano;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SetupAuto extends AppCompatActivity {

    String name;
    TextView instruct;
    EditText datepick, salary, savings;
    Button submit;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_auto);

        setTitle("Oregano - Budget Setup");

        instruct = (TextView) findViewById(R.id.textView4);
        datepick = (EditText) findViewById(R.id.editText3);
        salary = (EditText) findViewById(R.id.editText1);
        savings = (EditText) findViewById(R.id.editText2);
        submit = (Button) findViewById(R.id.button);

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

        datepick.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                datepick.setText(getDateString());
                return false;
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (salary.getText().length() <= 0) {
                    Toast.makeText(SetupAuto.this, "Salary is empty", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progress = new ProgressDialog(SetupAuto.this);
                    progress.setTitle("Generating budget");
                    progress.setMessage("You'll be directed shortly...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    final long sal = (long)(1. * Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", "")));
                    final long n = ctr(necessities(), 0, sal), lt = ctr(longTerm(), 0, sal), lf = ctr(lifestyle(), 0, sal);

                    String[] res = new String[]{String.valueOf(sal), String.valueOf(n), String.valueOf(lt), String.valueOf(lf)};
                    final Intent i = new Intent(SetupAuto.this, BudgetChartActivity.class);
                    i.putExtra("budget", res);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progress.cancel();
                            startActivity(i);
                        }
                    }, 500);



//                    new AlertDialog.Builder(SetupAuto.this)
//                            .setTitle("Suggested budget")
//                            .setMessage(((ctr(necessities(), 0, sal) < 0 || lifestyle() < 0)? "Warning! Your salary isn't enough to save that much per month.\n" : "") +
//                                    ((ctr(necessities(), 0, sal) < 1857)? "Warning! You may not afford necessities. Try lowering your savings.\n\n" : "") +
//                                    "Click VIEW to look at your suggested budget.\n" //+
////                                    "$" + n + "/month on necessities,\n" +
////                                    "$" + lt + "/month on long term savings, and\n" +
////                                    "$" + lf + "/month on entertainment or lifestyle choices."
//                            )
//                            .setPositiveButton("View", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    String[] res = new String[]{String.valueOf(sal), String.valueOf(n), String.valueOf(lt), String.valueOf(lf)};
//                                    Intent i = new Intent(SetupAuto.this, BudgetChartActivity.class);
//                                    i.putExtra("budget", res);
//                                    startActivity(i);
//                                }
//                            })
//                            .setNegativeButton("Fix inputs", null)
////                            .setIcon(android.R.drawable.ic_dialog_info)
//                            .show();

                }

            }
        });

    }

    private long ctr(long i, long m, long mm) { return compressToRange(i, m, mm); };

    private long compressToRange(long i, long min, long max) {
        return Math.max(Math.min(i, max), min);
    }

    private long longTerm() {
        if (savings.getText().length() > 0) {
            Date st = Calendar.getInstance().getTime();
            Date en = cal.getTime();
            long daydif = TimeUnit.DAYS.convert(Math.abs(en.getTime() - st.getTime()), TimeUnit.MILLISECONDS);

            double dpday = Double.valueOf(savings.getText().toString().replaceAll("[^\\d.]", "")) / daydif;

            return (long)(dpday * 30.5); //about 30.5 days a month
        } else
            return (long)(0.2 * (Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", ""))));

    }

    private long necessities() {
        return (long)(0.5 * (Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", "")) - (longTerm() - (long)(0.2 * (Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", "")))))));
    }

    private long lifestyle() {
        return (long)(0.3 * (Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", "")) - (longTerm() - (long)(0.2 * (Double.valueOf(salary.getText().toString().replaceAll("[^\\d.]", "")))))));
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
        datepick.setText(getDateString());
    }

    private String getDateString() {
        if (cal.getTime() != null) {
            String ff = "MM/dd/yyyy";
            SimpleDateFormat f = new SimpleDateFormat(ff, Locale.US);
            return f.format(cal.getTime());
        }
        return "";
    }
}
