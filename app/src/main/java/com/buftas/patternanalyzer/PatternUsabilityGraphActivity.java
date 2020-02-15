//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//MPAndroidChart imports
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
//Java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatternUsabilityGraphActivity extends AppCompatActivity {

    private BarChart barChart;
    private BarDataSet dataSet;
    private BarData data;
    private String value, patternType;
    private HashMap<String, Float> map;
    private List<BarEntry> graphEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_usability_graph);
        barChart = findViewById(R.id.barChart);
        //Get the passed values from previous Activity
        value = getIntent().getStringExtra("Value");
        map = (HashMap) getIntent().getSerializableExtra("GraphData");
        patternType = getIntent().getStringExtra("PatternType");
        //Create a list with Graph entries and add new BarEntries using data from HashMap
        graphEntries = new ArrayList<>();
        int counter = 1;
        for (String key : map.keySet()) {
            graphEntries.add(new BarEntry(counter, map.get(key)));
            counter++;
        }
        //Then create a DataSet Object and add the entries and a label, plus add colors for the bars
        dataSet = new BarDataSet(graphEntries, "Pattern Frequency Reusability");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(16f);
        //Then create a BarData entry and pass the dataset as parameter
        data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new PercentFormatter());
        //Configure bar chart
        final ArrayList<String> xVals = new ArrayList<>();
        //Create a Description Object
        Description desc = new Description();
        desc.setTextSize(16f);
        desc.setText("Reusability Frequency of Patterns");
        //Add the labels of every bar on the Graph
        xVals.add("");
        xVals.addAll(map.keySet());
        //Set the last configurations for the Bar Chart and invalidate it to show the updated form
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChart.setVisibility(View.VISIBLE);
        barChart.animateY(2000);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.setDescription(desc);
        barChart.invalidate();
    }
}
