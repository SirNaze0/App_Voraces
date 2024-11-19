package com.example.voracesfisi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Declare UI elements
    private EditText denominacionEntrada, montoEntrada;
    private Button agregarDenominacionesBoton, calcularBoton;
    private TextView cantidadDenominacionesVista, resultadoVista, denominacionesListaVista;
    private ArrayList<Integer> denominacionesLista = new ArrayList<>();
    private StringBuilder denominacionesListaTexto = new StringBuilder();


    public MoneyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoneyFragment newInstance(String param1, String param2) {
        MoneyFragment fragment = new MoneyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        // Inicializa los componentes
        inicializarUI(view);
        // Configura los listeners
        configurarListeners();
        return view;
    }
    //Metodo para inicializar los componentes de la UI
    private void inicializarUI(View view){
        denominacionEntrada = view.findViewById(R.id.denominationInput);
        agregarDenominacionesBoton = view.findViewById(R.id.addDenominationButton);
        cantidadDenominacionesVista = view.findViewById(R.id.denominationCount);
        denominacionesListaVista = view.findViewById(R.id.denominationValues);
        calcularBoton = view.findViewById(R.id.calculateButton);
        montoEntrada = view.findViewById(R.id.totalAmountEditText);
        resultadoVista = view.findViewById(R.id.resultValue);
    }
    // Metodo para configurar escuchadores de eventos
    private void configurarListeners() {
        agregarDenominacionesBoton.setOnClickListener(v -> clickBotonAgregarDenominacion());
        calcularBoton.setOnClickListener(v -> clickBotonCalcular());
    }
    //Logica del manejo de boton calcular
    private void clickBotonCalcular(){
        int monto = Integer.parseInt(montoEntrada.getText().toString());
        montoEntrada.setText("");
        // Ordenar la lista una vez que todos los elementos han sido añadidos de forma descendente
        Collections.sort(denominacionesLista, Collections.reverseOrder());
        Map<Integer, Integer> resultado = algoritmoCambioMonedasVoraz(denominacionesLista, monto);
        mostrarListaOrdenada();
        mostrarResultado(resultado);
    }
    //Muestra Lista ordenada
    private void mostrarListaOrdenada(){
        StringBuilder listaOrdenada=new StringBuilder();
        // Limpiar el StringBuilder antes de agregar nuevos datos
        listaOrdenada.setLength(0);
        // Recorrer el ArrayList y agregar los valores al StringBuilder
        for (int i = 0; i < denominacionesLista.size(); i++) {
            listaOrdenada.append(denominacionesLista.get(i)); // Agregar el número
            if (i < denominacionesLista.size() - 1) {
                listaOrdenada.append(", "); // Agregar una coma después de cada número, excepto el último
            }
        }
        denominacionesListaVista.setText(listaOrdenada.toString());
    }
    //Muestra el resultado
    private void mostrarResultado(Map<Integer, Integer> resultado) {
        StringBuilder resultadoTexto = new StringBuilder("\nResultado:\n");
        int totalMonedas = 0;
        for (Map.Entry<Integer, Integer> entry : resultado.entrySet()) {
            if (entry.getValue() > 0) {
                resultadoTexto.append("Monedas de ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                totalMonedas += entry.getValue();
            }
        }
        resultadoTexto.append("\nTotal de monedas usadas: ").append(totalMonedas);
        resultadoVista.setText(resultadoTexto.toString());
    }
    //AlgoritmoCambioMonedas
    private Map<Integer, Integer> algoritmoCambioMonedasVoraz( ArrayList<Integer> denominacionesLista, int monto){
        Map<Integer, Integer> resultado = new HashMap<>();
        for (int i = 0; i < denominacionesLista.size(); i++) {
            int moneda = denominacionesLista.get(i); // Obtener la denominación de la lista
            int cantidad = monto / moneda; // Cuántas monedas de esta denominación usamos
            monto %= moneda; // Resto que queda después de usar estas monedas
            if (cantidad > 0) { // Solo agregamos la denominación si se usa al menos una moneda
                resultado.put(moneda, cantidad); // Guardar la cantidad de monedas de esta denominación en el mapa
            }
        }
        return resultado; // Devolver el mapa con las denominaciones y las cantidades
    }
    //Logica del manejo de boton agregar denominaciones
    private void clickBotonAgregarDenominacion() {
        // Obtener el texto de la entrada
        String denominacionTexto = denominacionEntrada.getText().toString();

        // Comprobar si la denominación está vacía
        if (denominacionTexto.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingresa una denominación", Toast.LENGTH_SHORT).show();
            return; // Salir del metodo si está vacío
        }
        try {
            // Intentar convertir la denominación a un entero
            int denominacion = Integer.parseInt(denominacionTexto);

            // Comprobar si la denominación ya está en la lista
            if (!denominacionesLista.contains(denominacion)) {
                agregarDenominacion(denominacion);
                Toast.makeText(getContext(), "Denominación agregada: " + denominacionTexto, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "La denominación: " + denominacionTexto + " ya existe en la lista", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            // En caso de que el texto no se pueda convertir a un número
            Toast.makeText(getContext(), "La denominación ingresada no es válida", Toast.LENGTH_SHORT).show();
        }
        // Limpiar el campo de entrada
        denominacionEntrada.setText("");
    }
    //Logica de agregar denominacion
    private void agregarDenominacion(int denominacion){
        denominacionesLista.add(denominacion);
        cantidadDenominacionesVista.setText(String.format(Locale.getDefault(), "%,d", denominacionesLista.size()));
        actualizarListaDenominaciones();
    }
    //Logica de actualizar la lista con la denominacion agregada
    private void actualizarListaDenominaciones() {
        denominacionesListaTexto.setLength(0);
        denominacionesListaTexto.append(TextUtils.join(", ", denominacionesLista));
        denominacionesListaVista.setText(denominacionesListaTexto.toString());
    }

}