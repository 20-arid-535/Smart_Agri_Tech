package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    TextView forget,signup;
    ProgressDialog d;
    public static String s="";
  //  DBHelper DB;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // DB = new DBHelper(this);
        d=new ProgressDialog(this);
        d.setMessage("please wait");
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        forget = (TextView) findViewById(R.id.forget);
        signup = (TextView) findViewById(R.id.signupText);
        username.setText("nomanarid@gmail.com");
        password.setText("1234");
        checkPermission(android.Manifest.permission.CAMERA, 11);
        SharedPreferences sh=getSharedPreferences("Userinfo",MODE_PRIVATE);

SharedPreferences.Editor ed=sh.edit();
if(sh.contains("Email")){

    if(sh.getString("Email","")!=""){

        Intent i = new Intent(Login.this, Home.class);
        startActivity(i);
    }
}
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainActivity.this, "forget!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this,ForgetPass.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainActivity.this, "signup!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this,Signup.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().length()!=0 && password.getText().toString().length()!=0) {
                    String s11=username.getText().toString();
                    String s12= password.getText().toString();
d.show();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.orderByChild("email").equalTo(s11).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if(dataSnapshot.exists()) {
                                String email = new String();
                                String key = new String();
                                String pass = new String();
                                String name = new String();
                                String url = new String();
                                String far = new String();
                                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                    key = userSnapshot.getKey();
                                     email = userSnapshot.child("email").getValue(String.class);
                                    pass= userSnapshot.child("pass").getValue(String.class);
                                    name= userSnapshot.child("name").getValue(String.class);
                                    url =userSnapshot.child("url").getValue(String.class);
                                    far=userSnapshot.child("farmer").getValue(String.class);
                                }

                                if (s11.equals(email) && s12.equals(pass)) {
                                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    s = username.getText().toString();
                                    ed.putString("Key",key);
                                    ed.putString("Name",name);
                                    ed.putString("Email",email);
                                    ed.putString("Pass",pass);
                                    ed.putString("Url",url);
                                    ed.putString("Farmer",far);
                                    ed.commit();

                                    getcontroldata();

                                    d.dismiss();

                                      Intent i = new Intent(Login.this, Home.class);
                                       startActivity(i);
                                     }
                                     else {
                                      Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                    d.dismiss();  }
                            }

                    else {
                                Toast.makeText(Login.this, "Login fail!", Toast.LENGTH_SHORT).show();
                                d.dismiss(); } }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("dberror",error.getMessage());
                        }
                    } );

                       //
                   // }
                }
                else {
                    Toast.makeText(Login.this, "Login in Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

}
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(Login.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Login.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(Login.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 11) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(Login.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                this.checkPermission(android.Manifest.permission.CAMERA, 11);
            }
            else {
                Toast.makeText(Login.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
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

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Login.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}