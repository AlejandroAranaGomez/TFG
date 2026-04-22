package com.trabajo.fitnessapp.presentacion.menu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoDietas;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoInicio;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoPerfil;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoProgreso;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoRutinas;
import com.trabajo.fitnessapp.presentacion.notificaciones.NotificacionReceiver;

import java.util.Calendar;

public class MenuPrincipalActivity extends AppCompatActivity {

    private BottomNavigationView barraMenu;
    private Long usuarioRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarioRegistrado = getIntent().getLongExtra("ID_USUARIO", -1L);

        enlazarVistas();

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        programarNotificacion(this);

//        boolean yaProgramado = prefs.getBoolean("notificacion_programada", false);
//
//        if (!yaProgramado) {
//            programarNotificacion(this);
//            prefs.edit().putBoolean("notificacion_programada", true).apply();
//        }

        // Para cuando se carga por primera vez el menu
        if (savedInstanceState == null) {
            FragmentoInicio inicio = new FragmentoInicio();

            Bundle argumentos = new Bundle();
            argumentos.putLong("ID_USUARIO", usuarioRegistrado);
            inicio.setArguments(argumentos);

            cargarFragmento(inicio);
        }

        barraMenu.setOnItemSelectedListener(item -> {
            Fragment fragmentoSeleccionado = null;
            int id = item.getItemId();

            if (id == R.id.btnMenuInicio) {
                fragmentoSeleccionado = new FragmentoInicio();
            } else if (id == R.id.btnMenuDietas) {
                fragmentoSeleccionado = new FragmentoDietas();
            } else if (id == R.id.btnMenuRutinas) {
                fragmentoSeleccionado = new FragmentoRutinas();
            } else if (id == R.id.btnMenuPerfil) {
                fragmentoSeleccionado = new FragmentoPerfil();
            } else if (id == R.id.btnMenuProgreso) {
                fragmentoSeleccionado = new FragmentoProgreso();
            }

            if (fragmentoSeleccionado != null) {
                Bundle argumentos = new Bundle();
                argumentos.putLong("ID_USUARIO", usuarioRegistrado);
                fragmentoSeleccionado.setArguments(argumentos);

                cargarFragmento(fragmentoSeleccionado);
                return true;
            }
            return false;
        });
    }

    private void enlazarVistas() {
        barraMenu = findViewById(R.id.barraMenu);
    }

    private void cargarFragmento(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragmentoVacio, fragment);
        transaction.commit();
    }

    private void programarNotificacion(Context context) {

        programarAlarma(context, 9, 0, 100);   // desayuno
        programarAlarma(context, 14, 0, 200);  // comida
        programarAlarma(context, 22, 0, 300);  // cena
    }

    private void programarAlarma(Context context, int hora, int minuto, int requestCode) {

        Intent intent = new Intent(context, NotificacionReceiver.class);

        // 👇 IMPORTANTE: enviar info para diferenciar notificación
        intent.putExtra("tipo", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}