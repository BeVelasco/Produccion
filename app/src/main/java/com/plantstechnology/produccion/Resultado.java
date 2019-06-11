	package com.plantstechnology.produccion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Resultado extends AppCompatActivity {


    String hora = null;
    String fecha = null;
    String estado = "";
    String estado0 = "";
    String operador = "";
    String nomencl = "";
    String dibu = "";
    String canti = "";
    String opera = "";
    String descri = "";
    String priori = "";
    String maquina = "";
    String operacion = "";
    String informacion = "";
    String[] opera2;
    String opcion = "";
    String modo = "";
    String descripcion = "";
    String enpausa= "";
    String idproceso = "";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        informacion = getIntent().getExtras().getString("informacion");
        idproceso = getIntent().getExtras().getString("idproceso");

        EditText descripcion0 = findViewById(R.id.textDescripcion);
        descripcion = descripcion0.getText().toString();

        Spinner sp = findViewById(R.id.selectPausa);
        sp.setVisibility(View.INVISIBLE);

        ///////////////////////////////////////////////////////
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.
                Builder().permitNetwork().build());
        ///////////////////////////////////////////////////////

        try {
            String[] dividir = informacion.split("!");
            operador = dividir[0];
            maquina = dividir[2];

            String[] separar = dividir[1].split(";");

            nomencl = separar[0];
            dibu = separar[1];
            dibu = dibu.replace('/',' '); //Cambio "/" por "-" para su envio se elimina "."

            //////////////////////////
            String a = separar[2].substring(separar[2].indexOf("(") + 1, separar[2].indexOf(")"));
            String [] c = a.split("/");

            canti = c[0] + "-" + c[1];
            ///////////////////////

            descri = separar[3];
//            priori = separar[4];
            idproceso = separar[4];

            // Se separan las diferentes tareas dependiendo del la OT
            String opciones;
            opciones = "Seleccione la operación a realizar";

            Integer tope = separar.length-1;


            for(Integer x=0; x < tope-4; x++){
                opciones = opciones + "," + separar[5+x];
            }
            opera2 = opciones.split(",");
            // Ya se agrego las operaciones en las opciones
            opcion = dividir[3];
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this,"",Toast.LENGTH_LONG);
            toast.show();
        }

        int opcion1 = Integer.parseInt(opcion);

        switch (opcion1){
            case 0:
                modo = "Trabajo Normal";
                break;
            case 1:
                modo = "RT, RP o CD";

                RadioButton v1r = findViewById(R.id.empezar);
                //v1r.setVisibility(View.INVISIBLE);
                v1r.setText("RePro.");

                RadioButton v12r = findViewById(R.id.aux);
                v12r.setText("ReTra.");
                v12r.setVisibility(View.VISIBLE);

                RadioButton v2r = findViewById(R.id.terminar);
                //v2r.setVisibility(View.INVISIBLE);
                v2r.setText("CaDi.");

                EditText edr = findViewById(R.id.textDescripcion);
                edr.setVisibility(View.INVISIBLE);
                estado0 = "R0";
                break;
            case 3:
                modo = "Pausa";
                estado0 = "Pausa";

                RadioButton v1 = findViewById(R.id.empezar);
                v1.setVisibility(View.INVISIBLE);

                RadioButton v2 = findViewById(R.id.terminar);
                v2.setVisibility(View.INVISIBLE);

                EditText ed = findViewById(R.id.textDescripcion);
                ed.setVisibility(View.INVISIBLE);

                break;
            case 4:
                modo = "Reanudar";
                estado0 = "Reanudar";

                RadioButton v3 = findViewById(R.id.empezar);
                v3.setVisibility(View.INVISIBLE);

                RadioButton v4 = findViewById(R.id.terminar);
                v4.setVisibility(View.INVISIBLE);

                EditText ed1 = findViewById(R.id.textDescripcion);
                ed1.setVisibility(View.INVISIBLE);
                break;

            default:
                modo = "Error";
                break;
        }
        TextView textView = findViewById(R.id.textModo);
        textView.setText(modo);

        TextView textView0 = findViewById(R.id.operador);
        textView0.setText("Operador: " + operador);

        TextView textView1 = findViewById(R.id.maquina);
        textView1.setText("Maquina: " + maquina);

        TextView textView2 = findViewById(R.id.nomenclatura);
        textView2.setText("Nomenclatura: " + nomencl);

        TextView textView3 = findViewById(R.id.dibujo);
        textView3.setText("Dibujo: " + dibu);

        TextView textView4 = findViewById(R.id.cantidad);
        textView4.setText("Cantidad: " + canti);

        TextView textView5 = findViewById(R.id.prioridad);
        textView5.setText("Prioridad: " + priori);

        TextView textView6 = findViewById(R.id.descripcion);
        textView6.setText("Descripción: " + descri);

        if (modo.equals("Pausa") || modo.equals("Reanudar")){
            RadioButton rb1 = findViewById(R.id.terminar);
            rb1.setEnabled(false);

            Spinner sp1 = findViewById(R.id.selectPausa);
            sp1.setVisibility(View.VISIBLE);
        }

        Spinner spinner = findViewById(R.id.selectOperacion);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opera2));

        final String[] datos = {spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString()};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = findViewById(R.id.selectOperacion);
                datos[0] = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                if (datos[0].equals("Seleccione su operación a realizar")) {
                    operacion = " ";
                }else {
                    operacion = datos[0];
                    try {
                        operacion = URLEncoder.encode(operacion, "UTF-8");
                    }catch (Exception ex){
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ningun motivo seleccioando, intente nuevamente", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        Spinner spx = findViewById(R.id.selectPausa);

        String[] pausas = {"Seleccione motivo de pausa", "Fin dia laboral", "Comida", "Indicaciones", "Sanitario"};
        spx.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pausas));

        final String[] datos0 = {spx.getItemAtPosition(spinner.getSelectedItemPosition()).toString()};
        spx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner0 = findViewById(R.id.selectPausa);
                datos[0] = spinner0.getItemAtPosition(spinner0.getSelectedItemPosition()).toString();

                if (datos[0].equals("Seleccione motivo de pausa")) {
                    enpausa = " ";
                }else {
                    enpausa = datos[0];
                    try {
                        enpausa = URLEncoder.encode(enpausa, "UTF-8");
                    }catch (Exception ex){
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ninguna pausa seleccionada, intente nuevamente", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        Intent regresar = new Intent(Resultado.this, Escaneado.class);
        startActivity(regresar);
        //super.onBackPressed();
        finish();
    }

    public void Empezar(View view) {
        RadioButton rb1 = findViewById(R.id.empezar);
        rb1.setChecked(true);
        RadioButton rb2 = findViewById(R.id.terminar);
        rb2.setChecked(false);
        switch (modo) {
            case "Trabajo Normal":
                estado0 = "Inicio";
                break;
            case "RT, RP o CD":
                estado0 = "RP";
                break;
            case "Reproceso":
                estado0 = "Reproceso";
                break;
            case "Pausa":
                estado0 = "Pausa";
                break;
            case "Reanudar":
                estado0 = "Reanudar";
                break;
            default:
                estado0 = "Error";
                break;
        }
    }

    public void auxretra(View view){
        RadioButton rb1 = findViewById(R.id.empezar);
        rb1.setChecked(false);

        RadioButton rb12 = findViewById(R.id.aux);
        rb12.setChecked(true);

        RadioButton rb2 = findViewById(R.id.terminar);
        rb2.setChecked(false);

        switch (modo) {
            case "Trabajo Normal":
                estado0 = "Inicio";
                break;
            case "RT, RP o CD":
                estado0 = "RT";
                break;
            case "Reproceso":
                estado0 = "Reproceso";
                break;
            case "Pausa":
                estado0 = "Pausa";
                break;
            case "Reanudar":
                estado0 = "Reanudar";
                break;
            default:
                estado0 = "Error";
                break;
        }
    }

    public void Terminar(View view) {
        RadioButton rb1 = findViewById(R.id.empezar);
        rb1.setChecked(false);
        RadioButton rb2 = findViewById(R.id.terminar);
        rb2.setChecked(true);

        if(modo.equals("RT, RP o CD")){
            estado0 = "CaDi";
        } else {
            estado0 = "Fin";
        }
    }

    public void Enviar(View view) throws Exception {
        if (operacion.equals(" ")){
            Toast toast1 = Toast.makeText(this, "Elija una Operación", Toast.LENGTH_LONG);
            toast1.show();
        } else if (estado0.equals("")) {
            Toast toast = Toast.makeText(this, "Elija Empezar o Terminar", Toast.LENGTH_LONG);
            toast.show();
        } else if ((modo.equals("RT, RP o CaDi")) && (estado0.equals("R0"))) {
            Toast toast = Toast.makeText(this, "Elija ReProceso, Retrabajo o CaDi", Toast.LENGTH_LONG);
            toast.show();
        } else if(estado0.equals("Pausa") && enpausa.equals(" ")){
            Toast toast = Toast.makeText(this, "Elija un motivo de pausa", Toast.LENGTH_LONG);
            toast.show();
        } else if(estado0.equals("Reanudar") && enpausa.equals(" ")){
            Toast toast = Toast.makeText(this, "Elija un motivo de pausa", Toast.LENGTH_LONG);
            toast.show();
        } else {

//            EditText descripcion0 = (EditText) findViewById(R.id.textDescripcion);
//            descripcion = descripcion0.getText().toString();

            long ahora = System.currentTimeMillis();
            Date fechaActual = new Date(ahora);

            SimpleDateFormat formdate = new SimpleDateFormat("dd:MM:yyyy");
            fecha = formdate.format(fechaActual);

            SimpleDateFormat formtime = new SimpleDateFormat("HH:mm");
            hora = formtime.format(fechaActual);

            //////////////enviar informacion en http//////////////////
            if((estado0.equals("Pausa")) || (estado0.equals("Reanudar"))){
                Spinner spxx = findViewById(R.id.selectPausa);
                spxx.setVisibility(View.INVISIBLE);
            } else {
                TextView desc = findViewById(R.id.textDescripcion);
                descripcion = desc.getText().toString();
            }

            JSONObject jo = new JSONObject();
            jo.put("user", operador);
            jo.put("time", fecha + "--" + hora);
            jo.put("act", estado0);
            jo.put("maq", maquina);
            jo.put("nomen", nomencl);
            jo.put("dibu", dibu);
            jo.put("canti", canti);
            jo.put("descPz", descri);
            jo.put("priori", "1");
            jo.put("oper",operacion);
            if((estado0.equals("Pausa")) || (estado0.equals("Reanudar"))){
                jo.put("descEscri", enpausa);
            }else {
                jo.put("descEscri", descripcion);
            }
            jo.put("idproceso", idproceso);

//            String palabras = "{\"user\":\""+ operador +"\",\"time\":\""+ fecha + "--" + hora + "\",\"act':\"" + estado0 + "\",\"maq\":\""
//                    + maquina + "\",\"nomen\":\"" + nomencl + "\",\"dibu\":\"" + dibu + "\",\"canti\":\"" + canti + "\",\"descPz\":\""
//                    + descri + "\",\"priori\":\"1\",\"oper\":\"" + operacion + "\",\"descEscri\":\"" + descripcion + "\",\"idproceso\":\""
//                    +idproceso+"\"}";


            JSONObject jor = new JSONObject();
            jor.put("user", operador);
            jor.put("time", fecha + "--" + hora);
            jor.put("act", estado0);
            jor.put("maq", maquina);
            jor.put("nomen", nomencl);
            jor.put("dibu", dibu);
            jor.put("canti", canti);
            jor.put("descPz", descri);
            jor.put("priori", "1");
            jor.put("oper",operacion);
            if((estado0.equals("Pausa")) || (estado0.equals("Reanudar"))){
                jo.put("descEscri", enpausa);
            }else {
                jo.put("descEscri", descripcion);
            }
            jor.put("r",estado0);
            jor.put("idproceso", idproceso);

//            String palabrasr = "{\"user\":\"" + operador + "\",\"time\":\"" + fecha + "--" + hora + "\",\"act\":\"" + estado0 + "\",\"maq\":\""
//                    + maquina + "\",\"nomen\":\"" + nomencl + "\",\"dibu\":\"" + dibu + "\",\"canti\":\"" + canti + "\",\"descPz\":\""
//                    + descri + "\",\"priori\":\"1\",\"oper\":\"" + operacion + "\",\"descEscri\":\"" + descripcion + "\"r\":\""
//                    + estado0 +"\", \"idproceso\"   :"+idproceso+"\"}";

//            String palabras = "user/" + operador + "/time/" + fecha + "--" + hora + "/act/" + estado0 + "/maq/"
//                    + maquina + "/nomen/" + nomencl + "/dibu/" + dibu + "/canti/" + canti + "/descPz/"
//                    + descri + "/priori/" + priori + "/oper/" + operacion + "/descEscri/" + descripcion + " /";
//
//            String palabrasr = "user/" + operador + "/time/" + fecha + "--" + hora + "/act/" + estado0 + "/maq/"
//                    + maquina + "/nomen/" + nomencl + "/dibu/" + dibu + "/canti/" + canti + "/descPz/"
//                    + descri + "/priori/" + priori + "/oper/" + operacion + "/descEscri/" + descripcion + " /r/"
//                    + estado0 +"/";

            //palabras = palabras.replace(" ", "%20");
            //palabrasr = palabrasr.replace(" ", "%20");
            String resultado = "";

            if((estado0.equals("RT")) || (estado0.equals("RP")) || (estado0.equals("CaDi"))) {
                resultado = resultadosGoogleRTRPCD(jor);
            }else {
                resultado = resultadosGoogle(jo);
                //salida.append(palabras + "--" + resultado + "\n");
            }


            Toast t = Toast.makeText(this, resultado, Toast.LENGTH_SHORT);
            t.show();
            ///////////////////////////////////////////////////////////

                Intent enviado = new Intent(Resultado.this, Inicio.class);
                startActivity(enviado);
                finish();
        }
    }

    public void Cancelar(View view) {
        estado = "";
        operador = "";
        nomencl = "";
        dibu = "";
        canti = "";
        opera = "";
        descri = "";
        priori = "";
        maquina = "";
        operacion = "";
        Toast toast = Toast.makeText(this, "Información Borrada", Toast.LENGTH_LONG);
        toast.show();
        Intent cancelar = new Intent(Resultado.this, Inicio.class);
        startActivity(cancelar);
        finish();
    }

    //////////////////////////////////////////////////////////////////
    String resultadosGoogle(JSONObject jo) throws Exception {
        String devuelve = "";
        HttpURLConnection conexion = null;

        ///////////////////// pagina //////////////////////////////
        //URL url = new URL("http://promkt.com.mx/testing/api/WS/on/" + palabras);
        //String eee = "http://asys.selfip.net/GJ/api/WS/on/" + palabras;
        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
        //String eee = "http://"+global.getIp_save()+"/GJ/api/WS/on/" + palabras;
        String eee = "http://"+global.getIp_save()+"/api/app/registro";
        URL url = new URL(eee);
        ///////////////////////////////////////////////////////////

        try {
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("data", jo.toString());

            Log.i("JSON", jo.toString());

            DataOutputStream os = null;
            try {
                os = new DataOutputStream(conexion.getOutputStream());
            }catch (IOException e){
                Log.i("Stream", e.getMessage());
            }
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jo.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conexion.getResponseCode()));
            Log.i("MSG" , conexion.getResponseMessage());

            String sss = conexion.getResponseMessage();

        if (conexion.getResponseCode()==HttpURLConnection.HTTP_OK ) {
                devuelve = conexion.getResponseMessage();

                StringBuilder result = new StringBuilder();
                InputStream in = new BufferedInputStream(conexion.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.i("Result ", result.toString());
                JSONObject obj = new JSONObject(result.toString());


                Toast t2 = Toast.makeText(this, getmessjson(obj), Toast.LENGTH_LONG);
                t2.show();

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

                JSONObject obj = new JSONObject(line);

                Toast t2 = Toast.makeText(this, "Error de comunicación no hay respuesta del servidor. ", Toast.LENGTH_SHORT);
                t2.show();
            }
        }catch (Exception e){
                Log.i("Error : ", e.getMessage());
                Toast t1 = Toast.makeText(this, "Servidor no encontrado. Verificar la conexión y/o datos de configuración ("+global.getIp_save()+")", Toast.LENGTH_SHORT);
                t1.show();
            }

        conexion.disconnect();
        return devuelve;
    }

    //////////////////////////////////////////////////////////////////
    String resultadosGoogleRTRPCD(JSONObject jor) throws Exception {
        String devuelve = "";
        HttpURLConnection conexion = null;

        ///////////////////// pagina //////////////////////////////
        //URL url = new URL("http://promkt.com.mx/testing/api/WS/on/" + palabras);
        //String eee = "http://asys.selfip.net/GJ/api/WS/on/" + palabras;
        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
        //String eee = "http://"+global.getIp_save()+"/GJ/api/WS/on/" + palabras;
        String eee = "http://"+global.getIp_save()+"/api/app/rtrpcd";
        URL url = new URL(eee);
        ///////////////////////////////////////////////////////////

        try {
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("data", jor.toString());
            Log.i("JSON", jor.toString());

            DataOutputStream os = null;
            try {
                os = new DataOutputStream(conexion.getOutputStream());
            }catch (IOException e){
                Log.i("Stream", e.getMessage());
            }
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jor.toString());

            os.flush();
            os.close();

            Log.i("STATUSR", String.valueOf(conexion.getResponseCode()));
            Log.i("MSGR" , conexion.getResponseMessage());

            String sss = conexion.getResponseMessage();

            if (conexion.getResponseCode()==HttpURLConnection.HTTP_OK ) {
                devuelve = conexion.getResponseMessage();

                StringBuilder result = new StringBuilder();
                InputStream in = new BufferedInputStream(conexion.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject obj = new JSONObject(result.toString());

                Toast t2 = Toast.makeText(this, getmessjson(obj), Toast.LENGTH_LONG);
                t2.show();

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

                JSONObject obj = new JSONObject(line);

                Toast t2 = Toast.makeText(this, "Error de comunicación no hay respuesta del servidor. ", Toast.LENGTH_SHORT);
                t2.show();
            }
        }catch (Exception e){
            Toast t1 = Toast.makeText(this, "Sin comunicación con el servidor. Verificar la conexión", Toast.LENGTH_SHORT);
            t1.show();
        }

        conexion.disconnect();
        return devuelve;
    }

    private String getmessjson(JSONObject obj){
        String messresp = "";
        try {
            Integer urlresponse = obj.getInt("status");

            if (urlresponse == 10) {
                messresp = "Actividad ya registrada : "+obj.get("ultact");
            } else if (urlresponse == 1) {
                messresp = "Registro Exitoso";
            } else if (urlresponse == 0) {
                messresp = obj.getString("ultact");
            } else if (urlresponse == 11) {
                messresp = "Proyecto Finalizado : "+obj.get("ultact");
            } else{
                messresp = "Excepcion : "+obj.get("ultact");
            }
        } catch (Exception ex){}

        return messresp;
    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    public Boolean isOnlineNet() {

        try {
            Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 "+global.getIp_save());

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}