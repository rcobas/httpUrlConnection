package com.example.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private EditText et1;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText) findViewById(R.id.et1);

        tv1 = (TextView) findViewById(R.id.tv1);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

    }

    public void buscar(View view) {

        try {
            String cadenaBusqueda = et1.getText().toString();
            String apariciones = resultadosGoogle(cadenaBusqueda);
            tv1.append(cadenaBusqueda + ": " + apariciones + " apariciones\n");

        } catch (Exception e) {

            tv1.append("Error al conectar\n");
            Log.e("HTTP", e.getMessage());
        }
    }

    ;

    public String resultadosGoogle(String cadenaABuscar) throws Exception {

        String pagina = "";
        String numeroApariciones = "";

        URL url = new URL("https://www.google.es/search?hl=es&q=\"" + URLEncoder.encode(cadenaABuscar, "UTF-8") + "\"");

        //Conexión HTTPURLCONNECTION

        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String linea = reader.readLine();


            while (linea != null) {

                pagina += linea;
                linea = reader.readLine();

            }

            reader.close();
            numeroApariciones = buscaTextoAprox(pagina);


        } else {
            numeroApariciones = "ERROR: " + conexion.getResponseMessage();
        }

        conexion.disconnect();

        return numeroApariciones;
    }

    public String buscaTextoAprox(String pagina) {

        String apariciones;

        int inicio = pagina.indexOf("Aproximadamente");
        Log.i("valorI", String.valueOf(inicio));

        int fin = pagina.indexOf(" ", inicio + 16);
        Log.i("valorFin", String.valueOf(fin));

        apariciones = pagina.substring(inicio + 16, fin);

        return apariciones;

    }

    ;

}
