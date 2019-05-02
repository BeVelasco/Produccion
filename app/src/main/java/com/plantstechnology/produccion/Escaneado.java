package com.plantstechnology.produccion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Escaneado extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView escanerView;
    int tipoEscaneado = 0;
    String informacion = "";
    int opcion = 0;
    String escaneado = "";
    String textBoton = "Escanear Operador";
    String titulo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneado);
        opcion = getIntent().getExtras().getInt("opcion");
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
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }

    @Override
    public void handleResult(Result resultEscaner) {
        escanerView.stopCamera();
        setContentView(R.layout.activity_escaneado);
        escaneado = resultEscaner.getText();
        escanerView.resumeCameraPreview(this);
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
                    startActivity(resultado);
                    finish();
                }

                else if(tipoEscaneado == 3){
                    informacion = informacion + "!" + escaneado + "!" + opcion;
                    tipoEscaneado = 0;
                    onPause();
                    Intent resultado = new Intent(Escaneado.this, Resultado.class);
                    resultado.putExtra("informacion", informacion);
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
}