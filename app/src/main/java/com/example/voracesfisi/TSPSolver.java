package com.example.voracesfisi;

public class TSPSolver {
    public static Resultado resolverTSP(int[][] distancias, int n) {
        Resultado mejorResultado = new Resultado(null, Integer.MAX_VALUE);

        // Probar todas las ciudades como punto inicial
        for (int inicio = 0; inicio < n; inicio++) {
            Resultado resultadoActual = vecinoMasCercano(distancias, n, inicio);
            if (resultadoActual.costoTotal < mejorResultado.costoTotal) {
                mejorResultado = resultadoActual;
            }
        }

        return mejorResultado;
    }

    public static Resultado vecinoMasCercano(int[][] distancias, int n, int inicio) {
        boolean[] visitado = new boolean[n];
        int[] recorrido = new int[n + 1]; // +1 para regresar a la ciudad inicial
        int costoTotal = 0;

        int actual = inicio;
        visitado[actual] = true;

        for (int i = 0; i < n; i++) {
            recorrido[i] = actual;
            int siguienteCiudad = -1;
            int distanciaMinima = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (!visitado[j] && distancias[actual][j] < distanciaMinima) {
                    distanciaMinima = distancias[actual][j];
                    siguienteCiudad = j;
                }
            }

            if (siguienteCiudad != -1) {
                costoTotal += distanciaMinima;
                actual = siguienteCiudad;
                visitado[actual] = true;
            }
        }

        // Regresar a la ciudad inicial
        costoTotal += distancias[actual][inicio];
        recorrido[n] = inicio;

        return new Resultado(recorrido, costoTotal);
    }
}