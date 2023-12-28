package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IrrigationControl extends AppCompatActivity {
ImageView i2;
Switch autod,autor,autoo,manuald,manualr,manualo;
SharedPreferences sh;
    ProgressDialog d;
    DatabaseReference databaseReference;
    SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irrigation_control);
        d=new ProgressDialog(this);
        d.setMessage("please wait");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Control");

        autod=findViewById(R.id.autodc);
        autor=findViewById(R.id.autor);
        autoo=findViewById(R.id.autoo);
        manuald=findViewById(R.id.manuald);
        manualr=findViewById(R.id.manualr);
        manualo=findViewById(R.id.manualo);
        sh=getSharedPreferences("Controlinfo",MODE_PRIVATE);
         ed=sh.edit();
         d.show();
        getcontroldata();
        autod.setChecked(sh.getBoolean("AutoD",false));
        autor.setChecked(sh.getBoolean("AutoR",false));
        autoo.setChecked(sh.getBoolean("AutoO",false));
        manuald.setChecked(sh.getBoolean("ManualD",false));
        manualr.setChecked(sh.getBoolean("ManualR",false));
        manualo.setChecked(sh.getBoolean("ManualO",false));
        if(autod.isChecked()){
            manuald.setClickable(false);
        }
        if(autor.isChecked()){
            manualr.setClickable(false);
        }
        if(autoo.isChecked()){
            manualo.setClickable(false);
        }

        i2=findViewById(R.id.back);

        autod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("DripCrop", "auto", b);

                ed.putBoolean("AutoD",b);
                ed.commit();
                manuald.setClickable(!b);
            }
        });
        autor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("RainGun", "auto", b);
                ed.putBoolean("AutoR",b);
                ed.commit();
                    manualr.setClickable(!b);;

            }
        });
        autoo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("Orchard", "auto", b);
                ed.putBoolean("AutoO",b);
                ed.commit();
                manualo.setClickable(!b);
            }
        });
        manuald.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("DripCrop", "DS1", b);
                ed.putBoolean("ManualD",b);
                ed.commit();
            }
        });
        manualr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("RainGun", "RS1", b);
                ed.putBoolean("ManualR",b);
                ed.commit();
            }
        });
        manualo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateFirebaseValue("Orchard", "OS1", b);
                ed.putBoolean("ManualO",b);
                ed.commit();
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(IrrigationControl.this,Home.class);
                startActivity(ii);
            }
        });
    }

    private void updateFirebaseValue(String category, String key, boolean value) {
        if (databaseReference != null) {
            databaseReference.child(category).child(key).setValue(value);
        }
    }
    private void getcontroldata() {
        SharedPreferences sh1=getSharedPreferences("Controlinfo",MODE_PRIVATE);

        SharedPreferences.Editor ed1=sh1.edit();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Control");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // Get values from dataSnapshot and update TextViews

                    ed1.putBoolean("AutoD",dataSnapshot.child("DripCrop").child("auto").getValue(Boolean.class));
                    ed1.putBoolean("AutoR",dataSnapshot.child("RainGun").child("auto").getValue(Boolean.class));
                    ed1.putBoolean("AutoO",dataSnapshot.child("Orchard").child("auto").getValue(Boolean.class));
                    ed1.putBoolean("ManualD",dataSnapshot.child("DripCrop").child("DS1").getValue(Boolean.class));
                    ed1.putBoolean("ManualR",dataSnapshot.child("RainGun").child("RS1").getValue(Boolean.class));
                    ed1.putBoolean("ManualO",dataSnapshot.child("Orchard").child("OS1").getValue(Boolean.class));
                    ed1.commit();
d.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                //Toast.makeText(Login.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

