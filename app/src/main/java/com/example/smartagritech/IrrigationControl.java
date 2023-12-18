package com.example.smartagritech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class IrrigationControl extends AppCompatActivity {
ImageView i2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irrigation_control);
        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(IrrigationControl.this,Home.class);
                startActivity(ii);
            }
        });
    }
}

//<com.github.lzyzsd.circleprogress.CircleProgress
//        android:layout_gravity="center"
//        android:layout_width="210dp"
//        android:layout_height="210dp"
//        app:circle_progress="60"
//        android:layout_marginTop="20dp"
//        android:layout_marginBottom="25dp"
//        app:circle_text_size="30sp"
//
//        >
//
//</com.github.lzyzsd.circleprogress.CircleProgress>