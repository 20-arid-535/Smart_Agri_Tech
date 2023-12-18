package com.example.smartagritech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Notification extends AppCompatActivity {
    private DBHelper d;
    SharedPreferences sh;
    private static final String ID = "C1";
    private static final String C_NAME = "smart agri tech";
    private static final String C_DESC = "notification";
    ImageView i2;
    private RecyclerView rvN;
    private ArrayList<NotifyModel> NList;
    private NotifyAdapter NRVAdapter;
    ItemTouchHelper.SimpleCallback swip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sh=getSharedPreferences("Userinfo",MODE_PRIVATE);
        d = new DBHelper(this);
        DividerItemDecoration di=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rvN=findViewById(R.id.rvNotify);
        rvN.addItemDecoration(di);
        NList=new ArrayList<>();
        NRVAdapter=new NotifyAdapter(this,NList);

        rvN.setAdapter(NRVAdapter);
        Button b = findViewById(R.id.b1);
        show();
        NRVAdapter.notifyDataSetChanged();
        swip = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p = viewHolder.getAdapterPosition();
                NotifyModel model= NList.get(p);
                int id=model.getId();
                switch (direction){

                    case ItemTouchHelper.LEFT:
                        boolean check =d.deletedata(id);
                        if(check==true){
                            Toast.makeText(Notification.this, "successfully", Toast.LENGTH_SHORT).show();
                            show();

                        }
                        else{
                            Toast.makeText(Notification.this, "failed", Toast.LENGTH_SHORT).show();
                        }

                        NRVAdapter.notifyDataSetChanged();
                        break;
                    case ItemTouchHelper.RIGHT:
                        boolean check1 =d.deletedata(id);
                        if(check1==true){
                            Toast.makeText(Notification.this, "successfully", Toast.LENGTH_SHORT).show();
                            show();
                        }
                        else{
                            Toast.makeText(Notification.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                        NRVAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        ItemTouchHelper it = new ItemTouchHelper(swip);
        it.attachToRecyclerView(rvN);

        i2=findViewById(R.id.back);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(Notification.this,Home.class);
                startActivity(ii);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID, C_NAME, importance);
            channel.setDescription(C_DESC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title="Low Moisture Level";
                String desc="moisture level is below 20 so turn on the pump as soon as possible";
                String key =sh.getString("Email","");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification.this, ID)
                        .setSmallIcon(R.drawable.sp)
                        .setContentTitle(title)
                        .setContentText("check moisture")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(desc))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Notification.this);

// notificationId is a unique int for each notification that you must define.
                if (ActivityCompat.checkSelfPermission(Notification.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Toast.makeText(Notification.this, "notification Permission Denied", Toast.LENGTH_SHORT).show();

                    ActivityCompat.requestPermissions(Notification.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1122);
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManager.notify(1, builder.build());

               boolean check =d.insertdata(key,title,desc);

               if(check==true){
                   Toast.makeText(Notification.this, "successfully", Toast.LENGTH_SHORT).show();
                   show();

               }
               else{
                   Toast.makeText(Notification.this, "failed", Toast.LENGTH_SHORT).show();
               }
                NRVAdapter.notifyDataSetChanged();
    }
});
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1122) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
               Toast.makeText(Notification.this, "notification Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Notification.this, " Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void show(){
        NList.clear();
        Cursor c = d.getdatas(sh.getString("Email",""));
        if(c.moveToFirst()){
            do{
                NList.add(new NotifyModel(c.getString(2),c.getString(3),c.getInt(0)));
            }while (c.moveToNext());
        }
        Collections.reverse(NList);
    }
}