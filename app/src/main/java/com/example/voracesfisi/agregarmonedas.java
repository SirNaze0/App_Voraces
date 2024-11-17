package com.example.voracesfisi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.InputType;
import java.util.ArrayList;
import java.util.List;

public class agregarmonedas extends AppCompatActivity {

    private LinearLayout coinsContainer; // Contenedor para las monedas
    private List<EditText> coinDenominationEditTexts = new ArrayList<>(); // Lista de EditTexts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AgregarMonedas", "onCreate ejecutado");
        setContentView(R.layout.cambiomonedas);

        // Inicializar vistas
        coinsContainer = findViewById(R.id.coinsContainer);
        Button addCoinButton = findViewById(R.id.addCoinButton);

        // Configurar listener del botón
        addCoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AgregarMonedas", "Botón presionado");

                // Cambia el texto del botón al presionarlo
                addCoinButton.setText("Presionado");

                addCoinDenominationField();
            }
        });

    }

    private void addCoinDenominationField() {
        if (coinsContainer == null) {
            Log.e("AgregarMonedas", "El contenedor coinsContainer es nulo");
            return;
        }

        // Crear un nuevo LinearLayout horizontal para la moneda
        LinearLayout coinLayout = new LinearLayout(this);
        coinLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        coinLayout.setOrientation(LinearLayout.HORIZONTAL);
        coinLayout.setGravity(Gravity.CENTER_VERTICAL);
        coinLayout.setPadding(0, 8, 0, 8);

        // Agregar imagen de moneda
        ImageView coinImageView = new ImageView(this);
        coinImageView.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        coinImageView.setImageResource(R.drawable.tu_imagen_moneda);
        coinImageView.setContentDescription(getString(R.string.billete_desc));

        // Agregar campo de entrada para la denominación
        EditText denominationEditText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(8, 0, 0, 0);
        denominationEditText.setLayoutParams(params);
        denominationEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        denominationEditText.setHint("Denominación");

        // Agregar elementos al layout
        coinLayout.addView(coinImageView);
        coinLayout.addView(denominationEditText);

        // Agregar el layout al contenedor principal
        coinsContainer.addView(coinLayout);

        // Agregar EditText a la lista
        coinDenominationEditTexts.add(denominationEditText);
    }
}
