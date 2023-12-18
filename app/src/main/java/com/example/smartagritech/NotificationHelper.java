package com.example.smartagritech;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private DBHelper d;
    private static final String ID = "C1";
    private static final String C_NAME = "smart agri tech";
    private static final String C_DESC = "notification";
    Context c;

    NotificationHelper(Context c) {
        this.c = c;
    }

    public void send(String key, String title, String desc) {
        d = new DBHelper(c);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, ID)
                .setSmallIcon(R.drawable.sp)
                .setContentTitle(title)
                .setContentText("check more")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(desc))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());

        boolean check =d.insertdata(key,title,desc);

        if(check==true){
            Toast.makeText(c, "successfully", Toast.LENGTH_SHORT).show();


        }
        else{
            Toast.makeText(c, "failed", Toast.LENGTH_SHORT).show();
        }

    }

}



