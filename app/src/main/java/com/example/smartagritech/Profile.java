package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
TextView t1;
EditText e1,e2,e3;
Button  b1;
    ImageView i2;
ShapeableImageView i;
StorageReference sr;
    SharedPreferences sh;
    Map<String,Object> map;
    SharedPreferences.Editor ed;
    Uri selectedImageUri;

    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         map = new HashMap<>();
        setContentView(R.layout.activity_profile);
        t1 =(TextView) findViewById(R.id.textView3);
        e1=(EditText) findViewById(R.id.username);
        e2=(EditText) findViewById(R.id.username1);
        e3=(EditText) findViewById(R.id.password);
        b1=(Button) findViewById(R.id.Button);
        i=findViewById(R.id.shapeableImageView);
        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(Profile.this,MyAccount.class);
                startActivity(ii);
            }
        });
       sr = FirebaseStorage.getInstance().getReference("uploads/");

        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
       ed=sh.edit();
e1.setText(sh.getString("Name",""));
       // Toast.makeText(Profile.this, sh.getString("Url",""), Toast.LENGTH_SHORT).show();
        e2.setText(sh.getString("Email",""));
        e3.setText(sh.getString("Pass",""));
            Glide.with(this).load(sh.getString("Url", "")).circleCrop().into(i);

        b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (e1.getText().toString().length()!=0  && e2.getText().toString().length()!=0 && e2.getText().toString().length()!=0) {






            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(Profile.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                upload();
            }


        }
        else {
            Toast.makeText(Profile.this, " Failed!", Toast.LENGTH_SHORT).show();
        }

    }
});
    }
    public void click(View v){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Match the request 'pic id with requestCode
            if (requestCode == 1) {
                selectedImageUri = data.getData();
                Picasso.get().load(selectedImageUri).into(i);
                //Toast.makeText(Profile.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void upload(){
        if (selectedImageUri  != null) {
            StorageReference fileReference = sr.child(System.currentTimeMillis()
                    + "." + getFileExtension(selectedImageUri));

              fileReference.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Profile.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(Profile.this, task.getResult().toString(), Toast.LENGTH_LONG).show();
                                    map.put("email", e2.getText().toString());
                                    map.put("name", e1.getText().toString());
                                    map.put("pass", e3.getText().toString());
                                    map.put("url", task.getResult().toString());
                                    ed.putString("Url", task.getResult().toString());
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(sh.getString("Key", ""))
                                            .updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Profile.this, "reset Successful!", Toast.LENGTH_SHORT).show();

                                                    ed.putString("Name", e1.getText().toString());
                                                    ed.putString("Email", e2.getText().toString());
                                                    ed.putString("Pass", e3.getText().toString());

                                                    ed.commit();

                                                    //  Activity.recreate();

                                                    Intent i = new Intent(Profile.this, MyAccount.class);
                                                    startActivity(i);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Profile.this, "failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            map.put("email",e2.getText().toString());
            map.put("name",e1.getText().toString());
            map.put("pass",e3.getText().toString());

            FirebaseDatabase.getInstance().getReference().child("Users").child(sh.getString("Key",""))
                    .updateChildren(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile.this, "reset Successful!", Toast.LENGTH_SHORT).show();

                            ed.putString("Name",e1.getText().toString());
                            ed.putString("Email",e2.getText().toString());
                            ed.putString("Pass",e3.getText().toString());
                            ed.commit();

                            //  Activity.recreate();

                            Intent i = new Intent(Profile.this, MyAccount.class);
                            startActivity(i);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}