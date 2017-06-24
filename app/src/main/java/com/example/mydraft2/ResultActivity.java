package com.example.mydraft2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dikki on 18/12/2016.
 */

public class ResultActivity extends AppCompatActivity {

    LineChart lineChart;
    String TAG = "ResultActivity :";
    final DbUtility dbUtility = new DbUtility();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_graph_result);
        lineChart = (LineChart) findViewById(R.id.linechart1);
        final List<String>  xValues = new ArrayList<String>();
        List<Integer> yValues = new ArrayList<Integer>();

        populateDataFromResult(xValues,yValues);

        Log.d(TAG, "onCreate: Method called ");
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < xValues.size() ; i++)
        {
         Log.d(TAG,"Inside Data set "+ xValues.get(i));
            // turn your data into Entry objects
            entries.add(new Entry(i+1, yValues.get(i)));
        }

            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    Log.d(TAG,"Value is "+ value);
                    if ((int)value == 0) //Temp Dirty fix to handle when we have only one value index -1 & 1 are invalid
                    {
                        xValues.add(1,String.valueOf(2));
                        return String.valueOf(0);
                    }
                    return xValues.get((int) value-1); //VS temporary corrected
                }
                // we don't draw numbers, so no decimal digits needed
                //@Override
                public int getDecimalDigits() {  return 0; }
            };


        Log.d(TAG,"After Ivalueformatter");
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(R.color.colorAccent); // styling, ...

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.animateY(5000);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        dataSet.setDrawFilled(true);
        lineChart.invalidate(); // refresh

    }

    private void populateDataFromResult(List<String> xValues, List<Integer> yValues)
    {

        DateFormat dbFormat = new SimpleDateFormat(DbUtility.dateFormat);
        DateFormat graphFormat = new SimpleDateFormat("d-MMM"); //i.e. 4-Jul
        Date date = new Date();

        String resultTableQuery = "SELECT * FROM RESULTS";
        Cursor resultCursor= dbUtility.QueryDb(resultTableQuery,null);
        resultCursor.moveToFirst();
        for(int i = 0 ; i < resultCursor.getCount();i++)
        {
            String ResultDate = resultCursor.getString(resultCursor.getColumnIndex(DbUtility.RESULTDATE));
            try {
                date = dbFormat.parse(ResultDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            xValues.add( graphFormat.format(date));
            yValues.add(resultCursor.getInt(resultCursor.getColumnIndex(DbUtility.RESULT)));
            resultCursor.moveToNext();
        }

    }
}
