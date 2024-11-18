package com.example.voracesfisi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Usamos un Handler para esperar unos segundos antes de cargar la actividad principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inicia la actividad principal
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la SplashActivity para que no se quede en el stack
            }
        }, 3000); // 3000 milisegundos = 3 segundos
    }
}
