package com.example.smartagritech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartagritech.ml.BestFloat32;
import com.example.smartagritech.ml.BestFloat322;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WheatRust extends AppCompatActivity {

    SharedPreferences sh;
    String url = "http://noman78621.pythonanywhere.com/predict";
    NotificationHelper nh;
    String key;
    Button b1,b2,b3;
    ImageView i1,i2;
    Uri selectedImageUri;
    Bitmap B;
    String[] labels;
String result="";
    EditText tem,hum;
    int imageSize = 640;//224
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheat_rust);
        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
        key= sh.getString("Email","");
        nh=new NotificationHelper(this);
        b1=findViewById(R.id.loginButton);
        b2=findViewById(R.id.loginButton1);
        b3=findViewById(R.id.predict);
        i1=findViewById(R.id.i12);
        i2=findViewById(R.id.back);
        hum=findViewById(R.id.humidity);
        tem =findViewById(R.id.temp);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(WheatRust.this,Home.class);
                startActivity(ii);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 2);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 1);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(hum.getText().toString()!=""||tem.getText().toString()!=""){
                   StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                           new Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {

                                   try {
                                       JSONObject jsonObject = new JSONObject(response);
                                       String data = jsonObject.getString("predict");
                                       if(data.equals("1")){
                                           result="UnHealthy";
                                       }else{
                                           result="Healthy";
                                       }
                                       AlertDialog.Builder builder=new AlertDialog.Builder(WheatRust.this);
                                       builder.setTitle("Results").setMessage("Your plant is " +result).setCancelable(false)
                                               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {

                                                   }
                                               }).show();

                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }

                               }
                           },
                           new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   Toast.makeText(WheatRust.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }){

                       @Override
                       protected Map<String,String> getParams(){
                           Map<String,String> params = new HashMap<String,String>();
                           params.put("humidity",hum.getText().toString());
                           params.put("temperature",tem.getText().toString());

                           return params;
                       }

                   };
                   RequestQueue queue = Volley.newRequestQueue(WheatRust.this);
                   queue.add(stringRequest);
               }
               else {
                   Toast.makeText(WheatRust.this, " Fill each column", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
    private void detect(Bitmap photo){
        try {
            photo=Bitmap.createScaledBitmap(photo,640,640,false);
            BestFloat32 model = BestFloat32.newInstance(this);

            //test
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 640, 640, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            photo.getPixels(intValues, 0, photo.getWidth(), 0, 0, photo.getWidth(), photo.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }
//
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            BestFloat32.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputAsTensorBuffer();

            float[] outputArray = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < outputArray.length; i++) {
                if (outputArray[i] > maxConfidence) {
                    maxConfidence = outputArray[i];
                    maxPos = i;
                }
            }
            String[] classes = {"healthy", "unhealthy"};
            Toast.makeText(WheatRust.this, classes[maxPos], Toast.LENGTH_SHORT).show();
            i1.setImageBitmap(photo);
            //rs.setText(classes[maxPos]+"   "+String.valueOf((int)(maxConfidence*100))+"%");
            if(classes[maxPos]=="unhealthy"){
                nh.send(key,"Rust detected","Your plant is attack by rust disease");
            }
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Results").setMessage("Your plant is " +classes[maxPos]+" having confidence score "+String.valueOf((int)(maxConfidence*100))+"%").setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //i1.setImageResource(R.drawable.d1);
                        }
                    }).show();

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (resultCode == RESULT_OK) {
        if (requestCode == 1) {
            selectedImageUri = data.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            detect(image);
            // Picasso.get().load(selectedImageUri).into(i1);

        }
        else if (requestCode == 2) {
            Bitmap  p= (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            detect(p);
            //  i1.setImageBitmap(p);
        }

        }
    }
}