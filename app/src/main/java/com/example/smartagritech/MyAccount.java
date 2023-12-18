package com.example.smartagritech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyAccount extends AppCompatActivity {
CardView c1,c2,c3; SharedPreferences sh;
TextView t1,t2;
    ImageView i2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
       ImageView i=findViewById(R.id.shapeableImageView);
        Glide.with(this).load(sh.getString("Url","")).circleCrop().into(i);

        c1=(CardView) findViewById(R.id.c1);
        c2=(CardView) findViewById(R.id.c2);
        c3=(CardView) findViewById(R.id.c3);
        t1=findViewById(R.id.textView3);
        t2=findViewById(R.id.signupText);
        SharedPreferences   sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
        t1.setText(sh.getString("Name",""));
        t2.setText(sh.getString("Email",""));
        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(MyAccount.this,Home.class);
                startActivity(ii);
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAccount.this, Profile.class);
                startActivity(i);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyAccount.this, Profile.class);
                startActivity(i);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor ed=sh.edit();
                ed.putString("Key","");
                ed.putString("Email","");
                ed.commit();
                Intent i = new Intent(MyAccount.this, Login.class);
                startActivity(i);
            }
        });

    }
}