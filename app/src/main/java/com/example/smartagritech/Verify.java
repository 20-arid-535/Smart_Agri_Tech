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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import papaya.in.sendmail.SendMail;

public class Verify extends AppCompatActivity {
    EditText password;
    public static String s3="";
    String s;
    Button signupButton;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        password = findViewById(R.id.password);
        password.setText("1234");
        login = (TextView) findViewById(R.id.signupText);
        Intent i = getIntent();
        s = i.getStringExtra("key");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Signup.this, "login!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Verify.this,ForgetPass.class);
                startActivity(i);
            }
        });
        signupButton = findViewById(R.id.loginButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("1234") && password.getText().toString().length()!=0 ) {


                    Toast.makeText(Verify.this, "verify  Successful!", Toast.LENGTH_SHORT).show();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.orderByChild("email").equalTo(ForgetPass.s1).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                    s3 = userSnapshot.getKey();
                                    // pass= userSnapshot.child("pass").getValue(String.class);
                                    // pass= userSnapshot.child("name").getValue(String.class);
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("dberror",error.getMessage());
                        }
                    } );


                    Intent i = new Intent(Verify.this,Restpass.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Verify.this, " Failed!", Toast.LENGTH_SHORT).show();
                }
            }});
    }
}