package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Restpass extends AppCompatActivity {
    EditText username,email;
    EditText password;
    EditText confirmpassword;
    Button signupButton;
    TextView login;
    public String name="";
    String  user = new String();
    String pass = new String();
  //  DBHelper DB1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restpass);
      //  DB1 = new DBHelper(this);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpass);
        password.setText("1234");
        confirmpassword.setText("1234");
        signupButton = findViewById(R.id.loginButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(confirmpassword.getText().toString()) && password.getText().toString().length()!=0 ) {
                    String ss1=password.getText().toString();
                    String ss2=ForgetPass.s1;

                    Map<String,Object> map = new HashMap<>();


                   // map.put("email",ss2);
                  // map.put("name",user);
                    map.put("pass",ss1);

                    FirebaseDatabase.getInstance().getReference().child("Users").child(Verify.s3)
                            .updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Restpass.this, "reset Successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Restpass.this, Login.class);
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Restpass.this, "failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(Restpass.this, " Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}