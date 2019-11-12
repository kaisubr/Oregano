package io.github.kaisubr.oregano;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BudgetChartActivity extends AppCompatActivity {
    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgetchart);

        setTitle("Oregano - Current Budget Allocations");
        chart = (PieChart) findViewById(R.id.chart);

        Bundle extras = getIntent().getExtras();
        long[] alloc; float expectedTotal = 0, total = 0; long necessities = 0, savings = 0, lifestyle = 0;

        if (extras != null) {
            String[] allocations = extras.getStringArray("budget");
            alloc = new long[allocations.length];

            for (int i = 0; i < allocations.length; i++) {
                alloc[i] = Long.valueOf(allocations[i]);
            }

            expectedTotal = alloc[0];
            necessities = alloc[1];
            savings = alloc[2];
            lifestyle = alloc[3];

            total = (necessities + savings + lifestyle);


            long leftover = (long) (expectedTotal - necessities - savings - lifestyle);

            Log.d("budgetchart xp", "before: " + necessities + " " + savings + " " + lifestyle + " with total " + total + " so leftover " + leftover);

            if (leftover > 0) savings += leftover; //> 0 means under-budget. prioritize savings
            else {
                lifestyle += leftover; //currently over budget. take from lifestyle 635
                leftover = 0;
                Log.d("budgetchart xp", "Lifestyle is now " + lifestyle);

                if (lifestyle < 0) {//still not enough. take from savings
                    savings += lifestyle;
                    lifestyle = 0;
                    Log.d("budgetchart xp", "Savings is now " + savings);
                }

                if (savings < 0){ //still not enough. take from necessities
                    necessities += savings;
                    savings = 0;
                    Log.d("budgetchart xp", "Necessities is now " + necessities);
                }
            }
            total = (necessities + savings + lifestyle);

            Log.d("budgetchart xp", "after: " + necessities + " " + savings + " " + lifestyle + " with total " + total);
        }

        chart.setUsePercentValues(true);
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(2000, Easing.EaseOutQuad);
        chart.setCenterText("$" + total + "/month");
        chart.setCenterTextSize(19f);
        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        chart.setDrawCenterText(true);

//        chart.setOnChartValueSelectedListener(this);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        PieDataSet dataSet = new PieDataSet(entries, "Budget");

        entries.add(new PieEntry(necessities, "Necessities: " + necessities));
        entries.add(new PieEntry(savings, "Savings: " + savings));
        entries.add(new PieEntry(lifestyle, "Lifestyle: " + lifestyle));

        for (int c : ColorTemplate.PASTEL_COLORS)
            dataSet.addColor(c);

            PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.highlightValues(null);
        chart.invalidate();
    }
}
