package com.example.FitnessTracker;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import FitnessTracker_database.fitnessDB;
import kotlin.Pair;

public class SensorActivity_History extends AppCompatActivity {

    private Toolbar toolbar;
    private BarChart bar;
    private Button steps;
    private Button heart_points;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor_history);

        toolbar=findViewById(R.id.materialToolbar);
        bar=findViewById(R.id.barchart);
        steps=findViewById(R.id.chip_step);
        heart_points=findViewById(R.id.chip_point);
        fitnessDB dbhelper =new fitnessDB(SensorActivity_History.this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            Drawable upArrow= AppCompatResources.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);
            if (upArrow!=null)
            {
                int getUiMode= getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                int arrowColor;

                if (getUiMode==Configuration.UI_MODE_NIGHT_YES)
                {
                    arrowColor = Color.WHITE;
                }
                else
                {
                    arrowColor=Color.BLACK;
                }
               upArrow.setColorFilter(arrowColor, PorterDuff.Mode.SRC_ATOP);

                getSupportActionBar().setHomeAsUpIndicator(upArrow);//This line applies your custom black arrow to the toolbar's left side (where the back button usually is).
                // Without this line, Android uses the default icon and its default tint.
            }
        }

        Map<String, Pair<Integer, Integer>> weeklyData = dbhelper.getWeeklyStepsAndPointsAsMap();

        final  String[] days={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};//  creating a string arrray which will be entered as the x label data as days like monday , tuesday etc
        ArrayList<BarEntry> stepEntries=new ArrayList<>(); // making an arraylist object  of type barentry just like int float etc here barentry as the data will be enetered in bar chart

        for (int i=0; i<days.length; i++)
        {
            Pair<Integer, Integer> data = weeklyData.get(days[i]);
            Log.d("DEBUG_WEEKLY_DATA", "Day: " + days[i] + ", Data: " + (data != null ? data.getFirst() : "null"));
            if (data!=null)
            {
                stepEntries.add(new BarEntry(i,data.getFirst()));
            }
            else
            {
                stepEntries.add(new BarEntry(i, 0));
            }
        }


        XAxis xAxis = bar.getXAxis(); // creating an object of XAxis
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days)); // set label on the x axis using days array
        xAxis.setGranularity(1f);// Set the minimum interval between two labels (1f = one bar per label)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// position the x axis label at the bottom of the chart

        BarDataSet dataset=new BarDataSet(stepEntries,"Steps"); //Create a new dataset from the list of BarEntry objects with the label "Steps"
        int getUiMode= getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK; // getting the ui mode means it detects whether the mode is dark or white
        if (getUiMode==Configuration.UI_MODE_NIGHT_YES) {
            dataset.setColor(Color.parseColor("#006A71")); // it sets the color of the bar
            dataset.setValueTextColor(Color.WHITE);
            xAxis.setTextColor(Color.WHITE);
            bar.getAxisLeft().setTextColor(Color.WHITE);
            bar.getAxisRight().setTextColor(Color.WHITE);
            bar.getLegend().setTextColor(Color.WHITE);
        }
        else
        {
            dataset.setColor(Color.parseColor("#402E7A"));
            dataset.setValueTextColor(Color.BLACK);
            xAxis.setTextColor(Color.BLACK);
            bar.getAxisLeft().setTextColor(Color.BLACK);
            bar.getAxisRight().setTextColor(Color.BLACK);
            bar.getLegend().setTextColor(Color.BLACK);
        }
        dataset.setValueTextSize(15f); // Set font size for value labels on top of bars
        xAxis.setTextSize(15f);// Set font size for X-axis labels (Mon, Tue, etc.)
        bar.getAxisLeft().setTextSize(15f);
        bar.getAxisRight().setTextSize(15f);
        bar.getLegend().setTextSize(18f);// Set font size for the legend text ("Steps")
        bar.getDescription().setEnabled(false);
        BarData barData=new BarData(dataset);// Create BarData object using the dataset
        barData.setBarWidth(0.4f);// Set the width of each bar (between 0.0 and 1.0)
// A smaller number creates more space between bars......the default number is 0.85
        barData.setValueFormatter(new ValueFormatter() { // Set a custom value formatter for the bar data
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.valueOf((int) barEntry.getY());// Get the Y value (height) of the bar, cast it to int to remove decimal part,
                // then convert it to String so it can be shown as a label on the bar
            }
        });
        bar.setFitBars(true);// Ensures the bars fit nicely within the chart view
        bar.setData(barData);// Attach the BarData to the chart
        bar.animateY(3000);// Animate the chart with a vertical rise-in effect over 3 seconds
        xAxis.setYOffset(15f);// Move the X-axis labels slightly upward to reduce gap from bars
        xAxis.setCenterAxisLabels(false);// This ensures each label (Mon, Tue, etc.) appears directly under its corresponding bar,
// rather than being centered between two bars. Set to false for normal bar charts.
        bar.setExtraBottomOffset(10f);// Adds 10 pixels of extra space below the X-axis labels (at the bottom of the chart).
// This helps prevent the day labels from appearing too close to or getting cut off at the bottom edge of the screen.
        bar.invalidate();// Refresh and redraw the chart with the latest settings

        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BarEntry> info =new ArrayList<>(); // making an arraylist object  of type barentry just like int float etc here barentry as the data will be enetered in bar chart
                //adding values
                info.add(new BarEntry(0f,2000));
                info.add(new BarEntry(1f,5000));
                info.add(new BarEntry(2f,1000));
                info.add(new BarEntry(3f,500));
                info.add(new BarEntry(4f,1500));
                info.add(new BarEntry(5f,10000));
                info.add(new BarEntry(6f,2500));

                final  String[] days={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};//  creating a string arrray which will be entered as the x label data as days like monday , tuesday etc
                XAxis xAxis = bar.getXAxis(); // creating an object of XAxis
                xAxis.setValueFormatter(new IndexAxisValueFormatter(days)); // set label on the x axis using days array
                xAxis.setGranularity(1f);// Set the minimum interval between two labels (1f = one bar per label)
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// position the x axis label at the bottom of the chart

                BarDataSet dataset=new BarDataSet(info,"Steps"); //Create a new dataset from the list of BarEntry objects with the label "Steps"
                int getUiMode= getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK; // getting the ui mode means it detects whether the mode is dark or white
                if (getUiMode==Configuration.UI_MODE_NIGHT_YES) {
                    dataset.setColor(Color.parseColor("#006A71")); // it sets the color of the bar
                    dataset.setValueTextColor(Color.WHITE);
                    xAxis.setTextColor(Color.WHITE);
                    bar.getAxisLeft().setTextColor(Color.WHITE);
                    bar.getAxisRight().setTextColor(Color.WHITE);
                    bar.getLegend().setTextColor(Color.WHITE);
                }
                else
                {
                    dataset.setColor(Color.parseColor("#402E7A"));
                    dataset.setValueTextColor(Color.BLACK);
                    xAxis.setTextColor(Color.BLACK);
                    bar.getAxisLeft().setTextColor(Color.BLACK);
                    bar.getAxisRight().setTextColor(Color.BLACK);
                    bar.getLegend().setTextColor(Color.BLACK);
                }
                dataset.setValueTextSize(15f); // Set font size for value labels on top of bars
                xAxis.setTextSize(15f);// Set font size for X-axis labels (Mon, Tue, etc.)
                bar.getAxisLeft().setTextSize(15f);
                bar.getAxisRight().setTextSize(15f);
                bar.getLegend().setTextSize(18f);// Set font size for the legend text ("Steps")
                bar.getDescription().setEnabled(false);
                BarData barData=new BarData(dataset);// Create BarData object using the dataset
                barData.setBarWidth(0.4f);// Set the width of each bar (between 0.0 and 1.0)
// A smaller number creates more space between bars......the default number is 0.85
                barData.setValueFormatter(new ValueFormatter() {  // Set a custom value formatter for the bar data
                    @Override
                    public String getBarLabel(BarEntry barEntry) {
                        return String.valueOf((int) barEntry.getY());// Get the Y value (height) of the bar, cast it to int to remove decimal part,
                        // then convert it to String so it can be shown as a label on the bar
                    }
                });
                bar.setFitBars(true);// Ensures the bars fit nicely within the chart view
                bar.setData(barData);// Attach the BarData to the chart
                bar.animateY(3000);// Animate the chart with a vertical rise-in effect over 3 seconds
                xAxis.setYOffset(15f);// Move the X-axis labels slightly upward to reduce gap from bars
                xAxis.setCenterAxisLabels(false);// This ensures each label (Mon, Tue, etc.) appears directly under its corresponding bar,
// rather than being centered between two bars. Set to false for normal bar charts.
                bar.setExtraBottomOffset(10f);// Adds 10 pixels of extra space below the X-axis labels (at the bottom of the chart).
// This helps prevent the day labels from appearing too close to or getting cut off at the bottom edge of the screen.
                bar.invalidate();// Refresh and redraw the chart with the latest settings
            }
            });

        heart_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BarEntry> informat =new ArrayList<>(); // making an arraylist object  of type barentry just like int float etc here barentry as the data will be enetered in bar chart
                //adding values
                informat.add(new BarEntry(0f,39));
                informat.add(new BarEntry(1f,50));
                informat.add(new BarEntry(2f,20));
                informat.add(new BarEntry(3f,40));
                informat.add(new BarEntry(4f,25));
                informat.add(new BarEntry(5f,10));
                informat.add(new BarEntry(6f,15));

                final  String[] days={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};//  creating a string arrray which will be entered as the x label data as days like monday , tuesday etc
                XAxis xAxis = bar.getXAxis(); // creating an object of XAxis
                xAxis.setValueFormatter(new IndexAxisValueFormatter(days)); // set label on the x axis using days array
                xAxis.setGranularity(1f);// Set the minimum interval between two labels (1f = one bar per label)
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// position the x axis label at the bottom of the chart

                BarDataSet dataset=new BarDataSet(informat,"Heart Points"); //Create a new dataset from the list of BarEntry objects with the label "Steps"
                int getUiMode= getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK; // getting the ui mode means it detects whether the mode is dark or white
                if (getUiMode==Configuration.UI_MODE_NIGHT_YES) {
                    dataset.setColor(Color.parseColor("#006A71")); // it sets the color of the bar
                    dataset.setValueTextColor(Color.WHITE);
                    xAxis.setTextColor(Color.WHITE);
                    bar.getAxisLeft().setTextColor(Color.WHITE);
                    bar.getAxisRight().setTextColor(Color.WHITE);
                    bar.getLegend().setTextColor(Color.WHITE);
                }
                else
                {
                    dataset.setColor(Color.parseColor("#402E7A"));
                    dataset.setValueTextColor(Color.BLACK);
                    xAxis.setTextColor(Color.BLACK);
                    bar.getAxisLeft().setTextColor(Color.BLACK);
                    bar.getAxisRight().setTextColor(Color.BLACK);
                    bar.getLegend().setTextColor(Color.BLACK);
                }
                dataset.setValueTextSize(15f); // Set font size for value labels on top of bars
                xAxis.setTextSize(15f);// Set font size for X-axis labels (Mon, Tue, etc.)
                bar.getAxisLeft().setTextSize(15f);
                bar.getAxisRight().setTextSize(15f);
                bar.getLegend().setTextSize(18f);// Set font size for the legend text ("Steps")
                bar.getDescription().setEnabled(false);
                BarData barData=new BarData(dataset);// Create BarData object using the dataset
                barData.setBarWidth(0.4f);// Set the width of each bar (between 0.0 and 1.0)
// A smaller number creates more space between bars......the default number is 0.85
                barData.setValueFormatter(new ValueFormatter() {// Set a custom value formatter for the bar data
                    @Override
                    public String getBarLabel(BarEntry barEntry) {
                        return String.valueOf((int)barEntry.getY()); // Get the Y value (height) of the bar, cast it to int to remove decimal part,
                        // then convert it to String so it can be shown as a label on the bar
                    }
                });
                bar.setFitBars(true);// Ensures the bars fit nicely within the chart view
                bar.setData(barData);// Attach the BarData to the chart
                bar.animateY(3000);// Animate the chart with a vertical rise-in effect over 3 seconds
                xAxis.setYOffset(15f);// Move the X-axis labels slightly upward to reduce gap from bars
                xAxis.setCenterAxisLabels(false);// This ensures each label (Mon, Tue, etc.) appears directly under its corresponding bar,
// rather than being centered between two bars. Set to false for normal bar charts.
                bar.setExtraBottomOffset(10f);// Adds 10 pixels of extra space below the X-axis labels (at the bottom of the chart).
// This helps prevent the day labels from appearing too close to or getting cut off at the bottom edge of the screen.
                bar.invalidate();// Refresh and redraw the chart with the latest settings
            }
            });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId()== android.R.id.home)
        {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}