package com.plantstechnology.produccion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Escaneado extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView escanerView;
    int tipoEscaneado = 0;
    String informacion = "";
    int opcion = 0;
    String escaneado = "";
    String textBoton = "Escanear Operador";
    String titulo = null;
    String idproceso = "", idplanea = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.
                Builder().permitNetwork().build());
        ///////////////////////////////////////////////////////

        setContentView(R.layout.activity_escaneado);
        opcion = getIntent().getExtras().getInt("opcion");
        if (opcion != 5){
            textBoton = "Escanear Operador";
        }else{
            textBoton = "Escanear OT";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        escanerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        escanerView.stopCamera();// Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        Intent inicio = new Intent(Escaneado.this, Inicio.class);
        startActivity(inicio);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        escanerView.stopCamera();
    }

    public void BotonEscaner(View view) {
        escanerView = new ZXingScannerView(this);
        List<BarcodeFormat> formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        escanerView.setFormats(formats);
        escanerView.setAutoFocus(true);
        escanerView.setAspectTolerance(0.5f);
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }

    @Override
    public void handleResult(Result resultEscaner) {
        escanerView.stopCamera();
        setContentView(R.layout.activity_escaneado);
        escaneado = resultEscaner.getText();
        idproceso = escaneado;
        escanerView.resumeCameraPreview(this);
        if(tipoEscaneado == 1){
            String proy = "";
            try{
                proy = resultAPI(escaneado);
                JSONArray jsonArray = new JSONArray(proy);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                escaneado = jsonObject1.getString("qr");
            }catch(Exception ex){
                Log.e("API lectura de QR : ",ex.getMessage());
            }
        }
        AlertDialog verificacion = new AlertDialog.Builder(this).create();
        verificacion.setCancelable(false);
        verificacion.setTitle("Verifique su Información: ");
        verificacion.setMessage(escaneado);

        verificacion.setButton(DialogInterface.BUTTON_POSITIVE,"Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (tipoEscaneado == 0){
                    informacion = escaneado;
                    tipoEscaneado = 1;
                    Button b1 = (Button) findViewById(R.id.botonTrabajoNormal);
                    textBoton = "Escanear Orden de Trabajo";
                    b1.setText(textBoton);
                }

                else if(tipoEscaneado == 1){
                    informacion = informacion + "!" + escaneado;
                    tipoEscaneado = 0;
                    dialogo();
                }

                else if(tipoEscaneado == 2){
                    informacion = informacion + "!" + escaneado + "!" + opcion;
                    tipoEscaneado = 0;
                    onPause();
                    Intent resultado = new Intent(Escaneado.this, Resultado.class);
                    resultado.putExtra("informacion", informacion);
                    resultado.putExtra("idproceso", idproceso); //Se agrega el id del proceso
                    startActivity(resultado);
                    finish();
                }

                else if(tipoEscaneado == 3){
                    informacion = informacion + "!" + escaneado + "!" + opcion;
                    tipoEscaneado = 0;
                    onPause();
                    Intent resultado = new Intent(Escaneado.this, Resultado.class);
                    resultado.putExtra("informacion", informacion);
                    resultado.putExtra("idproceso", idproceso); //Se agrega el id del proceso
                    startActivity(resultado);
                    finish();
                }

                onPause();
            }
        });

        verificacion.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                escaneado = "";
                Button b1 = (Button) findViewById(R.id.botonTrabajoNormal);
                b1.setText(textBoton);
                mensaje();
                onPause();
            }
        });
        verificacion.show();
    }

    public void dialogo (){

        switch (opcion){
            case 0:
                titulo = "¿Va a usar alguna maquina o herramienta?";
                break;
            case 1:
                titulo = "¿Uso alguna maquina o herramienta?";
                break;
            case 2:
                titulo = "¿Uso alguna maquina o herramienta?";
                break;
            case 3:
                titulo = "¿Uso alguna maquina o herramienta?";
                break;
            case 4:
                titulo = "¿Uso alguna maquina o herramienta?";
                break;
            default:
                titulo = "Error 44";
                break;
        }

        AlertDialog diferencia = new AlertDialog.Builder(this).create();
        diferencia.setTitle("Elija una Opción");
        diferencia.setMessage(titulo);
        diferencia.setButton(DialogInterface.BUTTON_POSITIVE,"Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which1) {
                tipoEscaneado = 2;
                Button b1 = (Button) findViewById(R.id.botonTrabajoNormal);
                textBoton = "Escanear Maquina";
                b1.setText(textBoton);
            }
        });

        diferencia.setButton(DialogInterface.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which1) {
                escaneado = "No";
                Button b1 = (Button) findViewById(R.id.botonTrabajoNormal);
                b1.setText(textBoton);
                informacion = informacion + "!" + escaneado + "!" + opcion;
                tipoEscaneado = 0;
                onPause();
                Intent resultado = new Intent(Escaneado.this, Resultado.class);
                resultado.putExtra("informacion", informacion);
                resultado.putExtra("idproceso", idproceso); //Se agrega el id del proceso
                startActivity(resultado);
                finish();
            }
        });
        diferencia.show();
    }

    public void mensaje (){
        Toast mensage = Toast.makeText(this,"Informacion Borrada",Toast.LENGTH_SHORT);
        mensage.show();
    }

    //////////////////////////////////////////////////////////////////
    String resultAPI(String palabras) throws Exception {
        String devuelve = "";
        HttpURLConnection conexion = null;

        ///////////////////// pagina //////////////////////////////
        //URL url = new URL("http://promkt.com.mx/testing/api/WS/on/" + palabras);
        //String eee = "http://asys.selfip.net/GJ/api/WS/on/" + palabras;
        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
        //String eee = "http://"+global.getIp_save()+"/GJ/api/WS/on/" + palabras;

        String eee = "http://"+global.getIp_save()+"/api/app/getproy/";
        URL url = new URL(eee);
        ///////////////////////////////////////////////////////////


            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", palabras);

            Log.i("JSON", jsonParam.toString());

            DataOutputStream os = null;
            try {
                 os = new DataOutputStream(conexion.getOutputStream());
            }catch (IOException e){
                Log.i("Stream", e.getMessage());
            }
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conexion.getResponseCode()));
            Log.i("MSG" , conexion.getResponseMessage());

            String sss = conexion.getResponseMessage();

            if (conexion.getResponseCode()==HttpURLConnection.HTTP_OK ) {
                devuelve = conexion.getContent().toString();


                StringBuilder result = new StringBuilder();
                InputStream in = new BufferedInputStream(conexion.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                devuelve = response.toString();

                Log.i("Response", devuelve);
                //JSONObject obj = new JSONObject(result.toString());

            }else {
                //intentos--;
                //resultadosGoogle(palabras);
                devuelve = conexion.getResponseMessage();

                StringBuilder result = new StringBuilder();
                InputStream in = new BufferedInputStream(conexion.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Toast t2 = Toast.makeText(this, "Error de comunicación no hay respuesta del servidor. ", Toast.LENGTH_SHORT);
                t2.show();
            }

        conexion.disconnect();
        return devuelve;
    }
}