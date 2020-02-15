//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
//java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointFrequencyGraphActivity extends AppCompatActivity {

    private BarChart barChart;
    private BarDataSet dataSet;
    private BarData data;
    private String value, patternType;
    private HashMap<Integer, Float> map;
    private List<BarEntry> graphEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_frequency_graph);
        barChart = findViewById(R.id.barChart);
        //Get the passed values from previous Activity
        value = getIntent().getStringExtra("Value");
        try {
            map = (HashMap) getIntent().getSerializableExtra("GraphData");
        } catch (ClassCastException e) {
            Log.d("Casting Debug", e.toString());
        }
        patternType = getIntent().getStringExtra("PatternType");
        //Create a list with Graph entries and add new BarEntries using data from HashMap
        graphEntries = new ArrayList<>();
        for (Integer key : map.keySet()) {
            graphEntries.add(new BarEntry(key, map.get(key)));
        }
        //Then create a DataSet Object and add the entries and a label, plus add colors for the bars
        if (patternType.equals("Color")) {
            dataSet = new BarDataSet(graphEntries, "Color Usage Frequency");
        } else {
            dataSet = new BarDataSet(graphEntries, "Note Usage Frequency");
        }
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(16f);
        //Then create a BarData entry and pass the dataset as parameter
        data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new PercentFormatter());
        //Configure bar chart
        final ArrayList<String> xVals = new ArrayList<>();
        //We check what type of data we have, if it is Color or Piano pattern data
        //And add the correct labels
        //Create a Description Object
        Description desc = new Description();
        desc.setTextSize(16f);
        if (patternType.equals("Color")) {
            desc.setText("Usability of the Pattern Colors");
            xVals.add("");
            xVals.add("Purple");
            xVals.add("Green");
            xVals.add("Yellow");
            xVals.add("Red");
            xVals.add("Blue");
            xVals.add("Navy");
            xVals.add("Orange");
            xVals.add("White");
            xVals.add("Gray");
            xVals.add("Dark Green");
            xVals.add("Black");
            xVals.add("Sand");
        } else {
            desc.setText("Usability of the Pattern Notes");
            xVals.add("");
            xVals.add("C");
            xVals.add("C#");
            xVals.add("D");
            xVals.add("D#");
            xVals.add("E");
            xVals.add("F");
            xVals.add("A#");
            xVals.add("G");
            xVals.add("G#");
            xVals.add("A");
            xVals.add("A#");
            xVals.add("B");
            xVals.add("C");
        }
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
