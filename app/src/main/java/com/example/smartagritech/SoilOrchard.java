package com.example.smartagritech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SoilOrchard extends AppCompatActivity {
    ImageView i2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_orchard);
        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(SoilOrchard.this,SoilFarm.class);
                startActivity(ii);
            }
        });
    }
}