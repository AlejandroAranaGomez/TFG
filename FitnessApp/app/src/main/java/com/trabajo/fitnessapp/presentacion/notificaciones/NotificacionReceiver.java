package com.trabajo.fitnessapp.presentacion.notificaciones;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.trabajo.fitnessapp.R;

import java.util.Calendar;

public class NotificacionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        android.util.Log.d("NOTIF", "Receiver ejecutado");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "recordatorio_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Recordatorios",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        Intent openIntent = new Intent(context, com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        int tipo = intent.getIntExtra("tipo", 0);

        String mensaje = "";

        if (tipo == 100) {
            mensaje = "Es la hora de desayunar 🥐, no olvides registrar tu comida.";
        } else if (tipo == 200) {
            mensaje = "Es la hora de comer 🍝, anota tu progreso.";
        } else if (tipo == 300) {
            mensaje = "Es la hora de cenar 🌙, vamos a llegar al objetivo.";
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Fitness App 💪")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        manager.notify(1, notification);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent2 = new Intent(context, NotificacionReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                context,
                0,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent2
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent2
                );
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
