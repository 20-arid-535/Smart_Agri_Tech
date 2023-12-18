package com.example.smartagritech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SoilStatus extends AppCompatActivity {
ImageView i2;
    LineChart lineChart;
    List<String> xAxis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_status);
         lineChart = findViewById(R.id.lc);
         xAxis = new ArrayList<>();
        List<Entry> entries = readCSVFile();

        LineDataSet dataSet = new LineDataSet(entries, "Moisture Level"); // Replace with your label
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        XAxis xa=lineChart.getXAxis();
        xa.setTextColor(Color.WHITE);
        xa.setValueFormatter(new IndexAxisValueFormatter(xAxis));
// xa.setLabelCount(10,true);
// float gr =(float)  Math.ceil(10.0);
// xa.setGranularity(gr);
        // Create and customize the chart
        xa.setLabelRotationAngle(45f);
        lineChart.setData(new LineData(dataSets));
        lineChart.getDescription().setText("Moisture Histroy"); // Replace with your description

        lineChart.invalidate(); // Refresh chart
        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(SoilStatus.this,Home.class);
                startActivity(ii);
            }
        });
    }
    private List<Entry> readCSVFile() {
        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0,2));
//        entries.add(new Entry(1,2));
//        entries.add(new Entry(2,5));
//        entries.add(new Entry(3,4));
//        entries.add(new Entry(4,2));
        try {
            InputStream is = getResources().openRawResource(R.raw.my_soildata); // Replace with your CSV file
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
int x2=0;
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if(values[1]==null||values[0]==null) {}
                else {
                   // String x1 = values[2];
                    float y = Float.parseFloat(values[1]); // Assuming the second column has your numeric values
                    entries.add(new Entry(xAxis.size(), y));
                    xAxis.add(values[0]);
                    x2++;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }
}