package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import papaya.in.sendmail.SendMail;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

public class ForgetPass extends AppCompatActivity {
    EditText email;
    String s;
    boolean y=false;
    public static String s1="";
    Button sendButton;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        Random r = new Random();
        s=String.valueOf(r.nextInt((9999-100)+1)+10);
        email = findViewById(R.id.username1);
        email.setText("nomanarid@gmail.com");
        sendButton = findViewById(R.id.loginButton);

        login = (TextView) findViewById(R.id.signupText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Signup.this, "login!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ForgetPass.this,Login.class);
                startActivity(i);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length()!=0) {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.orderByChild("email").equalTo(email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if(dataSnapshot.exists()) {

                                s1=email.getText().toString();
                                SendMail mail = new SendMail("zeeshanmasood04181613021@gmail.com", "nuevzfwlxfoqjeby",
                                        email.getText().toString().trim(),
                                        "Testing Email Sending",
                                        "The verification code is : "+s);
                                mail.execute();
                                Toast.makeText(ForgetPass.this, "send Successful!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ForgetPass.this,Verify.class);
                                //ref.removeEventListener();
                                i.putExtra("key",s);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(ForgetPass.this, " Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("dberror",error.getMessage());
                        }
                    } );

                } else {
                    Toast.makeText(ForgetPass.this, " Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}