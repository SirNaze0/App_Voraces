package com.example.voracesfisi;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViajeroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViajeroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TableLayout tablaCiudades;
    private TextView resultadoVista;
    private EditText cantidadCiudadesEntrada;
    private Button cantidadCiudadesBoton,calcularBoton;

    private int cantidadCiudades;
    public ViajeroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViajeroFragment newInstance(String param1, String param2) {
        ViajeroFragment fragment = new ViajeroFragment();
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
        View view= inflater.inflate(R.layout.fragment_viajero, container, false);
        // Inicializa los componentes
        inicializarUI(view);
        // Configura los listeners
        configurarListeners();
        cantidadCiudades=0;
        return view;
    }

    private void configurarListeners() {
        cantidadCiudadesBoton.setOnClickListener(v -> clickCantidadCiudadesBoton());
        calcularBoton.setOnClickListener(v -> clickCalcularBoton());
    }

    private void clickCalcularBoton() {
        if (cantidadCiudades == 0) {
            Toast.makeText(getContext(), "Por favor, establece la cantidad de ciudades", Toast.LENGTH_SHORT).show();
            return; // Salir si no se ha establecido la cantidad de ciudades
        }

        // Cargar los datos de la tabla
        int[][] distancias = cargarDatosDesdeTabla();

        // Resolver el problema usando el enfoque voraz mejorado
        Resultado mejorResultado = TSPSolver.resolverTSP(distancias, cantidadCiudades);

        // Mostrar el resultado en pantalla
        mostrarResultado(mejorResultado);
    }
    private int[][] cargarDatosDesdeTabla() {
        int[][] distancias = new int[cantidadCiudades][cantidadCiudades];

        for (int i = 1; i <= cantidadCiudades; i++) { // Ignorar la primera fila (etiquetas)
            TableRow fila = (TableRow) tablaCiudades.getChildAt(i); // Obtener la fila

            for (int j = 1; j <= cantidadCiudades; j++) { // Ignorar la primera columna (etiquetas)
                EditText celda = (EditText) fila.getChildAt(j); // Obtener la celda específica
                String texto = celda.getText().toString(); // Obtener el texto de la celda

                // Convertir el texto en entero (o establecer 0 si está vacío)
                int valor = texto.isEmpty() ? 0 : Integer.parseInt(texto);

                // Almacenar el valor en la matriz de distancias
                distancias[i - 1][j - 1] = valor;
            }
        }
        return distancias;
    }
    private void mostrarResultado(Resultado mejorResultado) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Recorrido aproximado más corto:\n");

        // Convertir índices del recorrido a etiquetas (A, B, C, etc.)
        for (int i = 0; i < mejorResultado.recorrido.length - 1; i++) {
            resultado.append((char) ('A' + mejorResultado.recorrido[i])).append(" -> ");
        }
        resultado.append((char) ('A' + mejorResultado.recorrido[0])); // Regresar a la inicial

        resultado.append("\n\nCosto total: ").append(mejorResultado.costoTotal);

        // Mostrar resultado en el TextView
        resultadoVista.setText(resultado.toString());
    }


    private void clickCantidadCiudadesBoton() {
        cantidadCiudades= Integer.parseInt(cantidadCiudadesEntrada.getText().toString());
        generarTabla(cantidadCiudades);
    }

    private void inicializarUI(View view) {
        cantidadCiudadesBoton=view.findViewById(R.id.cantidadCiudadesBoton);
        cantidadCiudadesEntrada=view.findViewById(R.id.cantidadCiudadesEntrada);
        tablaCiudades=view.findViewById(R.id.tablaCiudades);
        resultadoVista=view.findViewById(R.id.resultadoVista);
        calcularBoton=view.findViewById(R.id.calcularBoton);
    }
    private void generarTabla(int tamaño) {
        tablaCiudades.removeAllViews(); // Eliminar cualquier vista anterior

        // Obtener el contexto de la actividad asociada al fragmento
        Context context = getContext();

        for (int i = 0; i < tamaño + 1; i++) {  // Generar una tabla (tamaño + 1) por filas y columnas
            TableRow fila = new TableRow(context); // Usar el contexto adecuado

            for (int j = 0; j < tamaño + 1; j++) {  // Similar a las filas, sumamos 1 para el encabezado
                EditText celda = new EditText(context); // Usar el contexto adecuado

                // Si está en la primera fila o primera columna (no editable)
                if (i == 0 || j == 0) {
                    celda.setEnabled(false); // Desactivar las intersecciones (fila 0, columna 0)

                    // Etiquetas de fila y columna (A, B, C, etc.)
                    if (i == 0 && j == 0) {
                        celda.setText("");  // La intersección (0,0) queda vacía
                    } else if (i == 0) {
                        celda.setText(String.valueOf((char) ('A' + j - 1)));  // Etiquetas columna (A, B, C)
                    } else if (j == 0) {
                        celda.setText(String.valueOf((char) ('A' + i - 1)));  // Etiquetas fila (A, B, C)
                    }
                }
                // Si es una celda en la diagonal (i == j), también se desactiva
                else if (i == j) {
                    celda.setEnabled(false); // Desactivar celdas en la diagonal
                    celda.setText(String.valueOf(0)); // Etiquetas de la diagonal (1, 2, 3, etc.)
                }
                else {
                    celda.setEnabled(true); // Habilitar celdas editables
                    celda.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Solo números decimales
                }

                // Establecer el tamaño de la celda y distribuir proporcionalmente
                celda.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f
                ));

                fila.addView(celda);
            }
            tablaCiudades.addView(fila);
        }
    }

}