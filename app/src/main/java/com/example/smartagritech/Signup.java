package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    EditText username,email;
    FirebaseDatabase fd;
    DatabaseReference dr;
    EditText password;
    EditText confirmpassword;
    Button signupButton;
    TextView login;

    String s11,s12,s13;
    CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        email = findViewById(R.id.username1);
        password = findViewById(R.id.password);
        cb = findViewById(R.id.forget);
        confirmpassword = findViewById(R.id.confirmpass);
        signupButton = findViewById(R.id.loginButton);
        username.setText("Noman");
        email.setText("nomanarid@gmail.com");
        password.setText("1234");
        confirmpassword.setText("1234");
        login = (TextView) findViewById(R.id.signupText);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setTransformationMethod(null);
                    confirmpassword.setTransformationMethod(null);
                }
                else{
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    confirmpassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signup.this,Login.class);
                startActivity(i);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length()!=0 && password.getText().toString().equals(confirmpassword.getText().toString()) && password.getText().toString().length()!=0 && email.getText().toString().length()!=0) {
                    s11=username.getText().toString();
                    s12=email.getText().toString();
                    s13=password.getText().toString();
                   insert(s11,s12,s13);
               }

                else
                {
                    Toast.makeText(Signup.this, " Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void insert(String user,String email,String pass){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(!dataSnapshot.exists()) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("name",user);
                    map.put("pass",pass);
                    map.put("url","https://firebasestorage.googleapis.com/v0/b/smart-agri-tech.appspot.com/o/uploads%2F1701365676305.png?alt=media&token=3c45c606-488f-4f27-9a95-c9c57cafa70a");
FirebaseDatabase.getInstance().getReference().child("Users").push().setValue(map)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Signup.this, " successfull!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "failed!", Toast.LENGTH_SHORT).show();
            }
        });
                }
                else{
                    Toast.makeText(Signup.this, " Exists!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("dberror",error.getMessage());
            }
        } );
    }

}