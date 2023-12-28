package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Home extends AppCompatActivity {
BottomNavigationView b;
    SharedPreferences sh;
    ImageView i;
    CardView c0,c1,c2,c3,c4,c5;
    MenuItem menuItem;
    FloatingActionButton fb;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1;
    private String cityName;
    TextView cond,temp,city;
    ImageView image;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4BA26AgetWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        b=findViewById(R.id.bottomNavigationView);
        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
        i=findViewById(R.id.i11);
        cond=findViewById(R.id.cond);
        temp=findViewById(R.id.tvTemp);
        city=findViewById(R.id.city);
        image=findViewById(R.id.img);
        c0=findViewById(R.id.c0);
        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c3=findViewById(R.id.c3);
        c4=findViewById(R.id.c4);
        c5=findViewById(R.id.c5);
        fb=findViewById(R.id.fb1);
        SharedPreferences sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
        Menu menu = b.getMenu();
        menuItem = menu.findItem(R.id.home);

        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Home.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }
        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null) {
            cityName = getCityName(location.getLongitude(), location.getLatitude());

            getWeatherInfo(cityName);
        }
        else {
            getWeatherInfo("Rawalpindi");
        }


        Glide.with(this).load(sh.getString("Url","")).circleCrop().into(i);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, MyAccount.class);
                startActivity(i);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, WeedDetection.class);
                startActivity(i);
            }
        });
        c0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Weather.class);
                startActivity(i);
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sh.getString("Farmer", "").equals("false"))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                    builder.setTitle("Notify").setMessage("you are not a farmer").setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
                else{
                    Intent i = new Intent(Home.this, IrrigationControl.class);
                    startActivity(i);
                    Toast.makeText(Home.this, "1!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, WeedDetection.class);
                startActivity(i);
                Toast.makeText(Home.this, "2!", Toast.LENGTH_SHORT).show();
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sh.getString("Farmer", "").equals("false"))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                    builder.setTitle("Notify").setMessage("you are not a farmer").setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
                else{
                Intent i2 = new Intent(Home.this, SoilFarm.class);
                startActivity(i2);
                Toast.makeText(Home.this, "3", Toast.LENGTH_SHORT).show();}
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Home.this, Notification.class);
               startActivity(i);
                Toast.makeText(Home.this, "4!", Toast.LENGTH_SHORT).show();
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, WheatRust.class);
                startActivity(i);
                Toast.makeText(Home.this, "4!", Toast.LENGTH_SHORT).show();
            }
        });
        b.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    Toast.makeText(Home.this, "1!", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.shorts:
                    if(sh.getString("Farmer", "").equals("false"))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                        builder.setTitle("Notify").setMessage("you are not a farmer").setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (menuItem != null) {
                                            menuItem.setChecked(true);
                                        }
                                    }
                                }).show();
                    }
                    else{
                    Intent i = new Intent(Home.this, IrrigationControl.class);
                    startActivity(i);
                    Toast.makeText(Home.this, "2!", Toast.LENGTH_SHORT).show();}
                    break;

                case R.id.subscriptions:
                    if(sh.getString("Farmer", "").equals("false"))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
                        builder.setTitle("Notify").setMessage("you are not a farmer").setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (menuItem != null) {
                                            menuItem.setChecked(true);
                                        }
                                    }
                                }).show();
                    }
                    else{
                    Intent i2 = new Intent(Home.this, SoilFarm.class);
                    startActivity(i2);
                    Toast.makeText(Home.this, "3!", Toast.LENGTH_SHORT).show();}
                    break;

                case R.id.library:
                    Toast.makeText(Home.this, "4!", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(Home.this, MyAccount.class);
                    startActivity(i1);
                    break;
            }

            return true;

        });



    }
    @Override
//    //Granting permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please provide the permission",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address adr : addresses) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User city not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;

    }

    private void getWeatherInfo(String cityName){

        String url="http://api.weatherapi.com/v1/forecast.json?key=52a3aea6d02b461cb3e55152232909&q="+ cityName +"&days=7&aqi=no&alerts=no";
        city.setText(cityName);

        RequestQueue requestQueue= Volley.newRequestQueue(Home.this);

        //json object request
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String temperature= response.getJSONObject("current").getString("temp_c");

                    temp.setText(temperature+ "°");
                    int isDay= response.getJSONObject("current").getInt("is_day");
                    String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(image);
                    cond.setText(condition);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this,"Please enter valid city name",Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

        protected void onResume() {

        super.onResume();
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }
}