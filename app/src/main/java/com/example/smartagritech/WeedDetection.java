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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartagritech.ml.BestFloat322;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.DataType;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class WeedDetection extends AppCompatActivity {
Button b1,b2;
ImageView i1,i2;
Uri selectedImageUri;
Bitmap B;
String[] labels;
TextView rs;
    SharedPreferences sh;

NotificationHelper nh;
    int imageSize = 416;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weed_detection);
        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
         key= sh.getString("Email","");
        labels = new String[] {"Piyazi Booti","Jungli Gajjar","Canada Thistle"," Billi Booti","Bathu"};
        b1=findViewById(R.id.loginButton);
        b2=findViewById(R.id.loginButton1);
        i1=findViewById(R.id.i12);
        i2=findViewById(R.id.back);
        rs=findViewById(R.id.result);
        nh=new NotificationHelper(this);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(WeedDetection.this,Home.class);
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
    }
    private void detect(Bitmap photo){
        boolean detect=false;
String r="";
        int x=-1,y=-1,z=-1,u=-1,la=-1,ch=0;
        float sc=0;
        try {
            photo=Bitmap.createScaledBitmap(photo,416,416,false);
            BestFloat322 model = BestFloat322.newInstance(this);

            //test
Paint paint=new Paint();
paint.setColor(Color.RED);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 416, 416, 3}, DataType.FLOAT32);
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
            BestFloat322.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputAsTensorBuffer();

            float[] outputArray = outputFeature0.getFloatArray();
            int[] outputShape = outputs.getOutputAsTensorBuffer().getShape();
            Log.d("out2", Arrays.toString(outputs.getOutputAsTensorBuffer().getFloatArray()));
            float w=photo.getWidth();
            float h= photo.getHeight();
            Canvas c=new Canvas(photo);
            paint.setTextSize(h/15f);
            paint.setStrokeWidth(h/89f);
            int numDetections = outputShape[2];
            for (int i = 0; i < numDetections; i++) {
                // Extract relevant information from the output array
                int baseIndex =  outputShape[2]; // Index for the start of the i-th box

                // Extract relevant information from the output array
                float x1 = outputArray[i+(baseIndex*0)];
                float y1 = outputArray[i+(baseIndex *1)];
                float x2 = outputArray[i+(baseIndex *2)];
                float y2 = outputArray[i+(baseIndex *3)];
                float confidence=outputArray[i+(baseIndex *4)];
                int classLabel = 0;
                for (int j=0; j<5;j++){
                    if(confidence<outputArray[i+(baseIndex *(4+j))]){
                        confidence = outputArray[i+(baseIndex *(4+j))];
                        classLabel = j;
                    }
                }
                if(confidence>0.7) {
                    detect=true;
                    // Convert box coordinates to the actual image dimensions
                    int imageWidth = photo.getWidth();
                    int imageHeight = photo.getHeight();
                    int boxLeftPixel = (int) ((x1-x2/2)*imageWidth);
                    int boxTopPixel = (int) ((y1-y2/2) *imageHeight);
                    int boxRightPixel = (int) ((x1+x2/2) *imageWidth);
                    int boxBottomPixel = (int) ((y1+y2/2) *imageHeight);
                    if(ch==0){
                        x=boxLeftPixel;
                        y=boxTopPixel;
                        z=boxRightPixel;
                        u=boxBottomPixel;
                        la=classLabel;
                        sc=confidence;
                        ch=1;
                    }
                    else if(la==classLabel){
                        if((boxLeftPixel<=(x+2)||boxLeftPixel>=(x-2))&&(boxTopPixel<=(x+2)||boxTopPixel>=(x-2))){
                            if(sc<confidence){
                                x=boxLeftPixel;
                                y=boxTopPixel;
                                z=boxRightPixel;
                                u=boxBottomPixel;
                                la=classLabel;
                                sc=confidence;
                            }
                        }
                        else{
                            r=r+labels[la]+", ";
                            paint.setStyle(Paint.Style.STROKE);
                            c.drawRect(x, y, z, u, paint);
                            paint.setStyle(Paint.Style.FILL);
                            c.drawText(labels[la] + " " + String.valueOf(sc), x, y, paint);
                            x=boxLeftPixel;
                            y=boxTopPixel;
                            z=boxRightPixel;
                            u=boxBottomPixel;
                            la=classLabel;
                            sc=confidence;
                        }
                    }
                    else {
                        r=r+labels[la]+", ";
                        paint.setStyle(Paint.Style.STROKE);
                        c.drawRect(x, y, z, u, paint);
                        paint.setStyle(Paint.Style.FILL);
                        c.drawText(labels[la] + " " + String.valueOf(sc), x, y, paint);
                        x=boxLeftPixel;
                        y=boxTopPixel;
                        z=boxRightPixel;
                        u=boxBottomPixel;
                        la=classLabel;
                        sc=confidence;
                    }
                    // Process or display the detection information as needed
                    // ...


                }
            }
            if(detect){
                r=r+labels[la]+", ";
            paint.setStyle(Paint.Style.STROKE);
            c.drawRect(x, y, z, u, paint);
            paint.setStyle(Paint.Style.FILL);
            c.drawText(labels[la] + " " + String.valueOf(sc), x, y, paint);
                i1.setImageBitmap(photo);
                rs.setText(r+" weeds are detected in your field");
                nh.send(key,"Weed Detected",r+" weeds are detected in your field");
            }
            else{
                i1.setImageBitmap(photo);
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Congratulation!").setMessage("Your plant is weed free").setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                i1.setImageResource(R.drawable.d1);
                            }
                        }).show();

                rs.setText("No Result");
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Match the request 'pic id with requestCode
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

            } else if (requestCode == 2) {
                Bitmap p = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                detect(p);
                //  i1.setImageBitmap(p);
            }
        }
    }
}