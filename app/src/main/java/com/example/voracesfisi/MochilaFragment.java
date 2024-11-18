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
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MochilaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MochilaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText capacidadEntrada, pesoObjetoEntrada , valorObjetoEntrada;
    private TextView cantidadObjetosVista, listaObjetosVista,resultadoVista;
    private Button agregarObjetoBoton, calcularBoton;

    ArrayList<Objeto> objetosLista = new ArrayList<>();
    private StringBuilder objetosListaTexto = new StringBuilder();

    public MochilaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MochilaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MochilaFragment newInstance(String param1, String param2) {
        MochilaFragment fragment = new MochilaFragment();
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
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mochila, container, false);
        // Inicializa los componentes
        inicializarUI(view);
        // Configura los listeners
        configurarListeners();
        return view;
    }

    private void configurarListeners() {
        agregarObjetoBoton.setOnClickListener(v -> clickAgregarObjetoBoton());
        calcularBoton.setOnClickListener(v -> clickCalcularBoton());
    }

    private void clickCalcularBoton() {
        int capacidad = Integer.parseInt(capacidadEntrada.getText().toString());
        capacidadEntrada.setText("");
        List<ResultadoObjeto> resultados = resolverMochilaFraccionaria(objetosLista, capacidad);

        // Construir el resultado como una cadena
        StringBuilder resultadoVistaString = new StringBuilder("Valor máximo obtenido (S/.): " + resultados.stream()
                .mapToDouble(res -> res.objeto.valorPorPeso * res.objeto.peso * res.fraccion) // Calcula el valor fraccionado
                .sum());

        resultadoVistaString.append("\nFracciones de objetos tomados:\n");
        for (ResultadoObjeto res : resultados) {
            resultadoVistaString.append(res.toString()).append("\n");
        }

        // Mostrar en la vista
        resultadoVista.setText(resultadoVistaString.toString());
    }
    public static List<ResultadoObjeto> resolverMochilaFraccionaria(ArrayList<Objeto> objetosLista, int capacidad) {
        // Ordenar los objetos por valor/peso en orden descendente
        Collections.sort(objetosLista);
        double valorTotal = 0;
        List<ResultadoObjeto> resultados = new ArrayList<>();

        for (Objeto obj : objetosLista) {
            if (capacidad == 0) break; // Detener si la mochila está llena

            double fraccion = 0;  // Variable para almacenar la fracción tomada

            if (capacidad >= obj.peso) { // Tomar el objeto completo si cabe
                capacidad -= obj.peso;
                valorTotal += obj.valor;
                fraccion = 1.0; // Fracción completa
            } else { // Tomar una fracción del objeto si no cabe completo
                fraccion = (double) capacidad / obj.peso;
                valorTotal += obj.valorPorPeso * capacidad;
                capacidad = 0; // Mochila llena
            }

            // Añadir al resultado
            resultados.add(new ResultadoObjeto(obj, fraccion));
        }

        // Mostrar el valor total
        System.out.printf("Valor total: %.2f%n", valorTotal);

        return resultados;
    }

    private void clickAgregarObjetoBoton() {
        String pesoObjetoEntradaString = pesoObjetoEntrada.getText().toString();
        String valorObjetoEntradaString =valorObjetoEntrada.getText().toString();
        // Comprobar si la denominación está vacía
        if (pesoObjetoEntradaString.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingresa un peso", Toast.LENGTH_SHORT).show();
            return;
        } else if (valorObjetoEntradaString.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, ingresa un valor", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int pesoObjeto = Integer.parseInt(pesoObjetoEntradaString);
            int valorObjeto = Integer.parseInt(valorObjetoEntradaString);
            agregarObjeto(pesoObjeto,valorObjeto);
            Toast.makeText(getContext(), "Objeto agregado: \n" + "Peso: "+pesoObjetoEntradaString+"\nValor:"+valorObjetoEntradaString, Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            // En caso de que el texto no se pueda convertir a un número
            Toast.makeText(getContext(), "Los datos no son válidos", Toast.LENGTH_SHORT).show();
        }
        // Limpiar el campo de entrada
        pesoObjetoEntrada.setText("");
        // Limpiar el campo de entrada
        valorObjetoEntrada.setText("");
    }

    private void agregarObjeto(int pesoObjeto, int valorObjeto) {
        Objeto objeto =new Objeto(pesoObjeto,valorObjeto);
        objetosLista.add(objeto);
        cantidadObjetosVista.setText(String.format(Locale.getDefault(), "%,d", objetosLista.size()));
        actualizarObjetosLista();
    }

    private void actualizarObjetosLista() {
        objetosListaTexto.setLength(0); // Limpiar el contenido anterior
        objetosListaTexto.append(TextUtils.join(", ", objetosLista)); // Unir los objetos
        listaObjetosVista.setText(objetosListaTexto.toString()); // Mostrar el texto
    }

    private void inicializarUI(View view) {
        capacidadEntrada=view.findViewById(R.id.capacidadEntrada);
        pesoObjetoEntrada=view.findViewById(R.id.pesoObjetoEntrada);
        valorObjetoEntrada=view.findViewById(R.id.valorObjetoEntrada);
        cantidadObjetosVista=view.findViewById(R.id.cantidadObjetosVista);
        listaObjetosVista=view.findViewById(R.id.listaObjetosVista);
        resultadoVista=view.findViewById(R.id.resultadoVista);
        agregarObjetoBoton=view.findViewById(R.id.agregarObjetoBoton);
        calcularBoton=view.findViewById(R.id.calcularBoton);
    }
    public static class Objeto implements Comparable<Objeto> {
        int peso;
        int valor;
        double valorPorPeso;

        public Objeto(int peso, int valor) {
            this.peso = peso;
            this.valor = valor;
            this.valorPorPeso = (double) valor / peso;
        }

        @Override
        public int compareTo(Objeto otro) {
            return Double.compare(otro.valorPorPeso, this.valorPorPeso); // Orden descendente
        }
        @Override
        public String toString() {
            // Formato (peso, valor)
            return "(" + peso + ", " + valor + ")";
        }
    }
    public static class ResultadoObjeto {
        public Objeto objeto;
        public double fraccion;

        public ResultadoObjeto(Objeto objeto, double fraccion) {
            this.objeto = objeto;
            this.fraccion = fraccion;
        }

        @Override
        public String toString() {
            return String.format("Objeto: %s, Fracción tomada: %.2f%%", objeto, fraccion * 100);
        }
    }
}