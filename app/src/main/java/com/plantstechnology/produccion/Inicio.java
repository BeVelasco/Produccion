package com.plantstechnology.produccion;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Inicio extends AppCompatActivity  {
    int opcion = 0;
    Protegido protegido = new Protegido();
    String contraseña = protegido.getContraseña();
    String usuario = protegido.getUsuario();

    public static class classGlobal extends Application{
        private String ip_save;
        private String user_save;
        private String clave_save;

        public classGlobal(){
            super();
        }

        public void setIp_save(String ip){
            ip_save = ip;
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = conf.edit();
            editor.putString("ddir",ip_save);
            editor.commit();
        }

        public String getIp_save(){
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            String ip_recuperado = conf.getString("ddir","localhost");
            return ip_recuperado;
        }

        public void setUser_save(String user){
            user_save = user;
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = conf.edit();
            editor.putString("duser", user_save);
            editor.commit();
        }

        public String getUser_save(){
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            String user_recuperado = conf.getString("duser","mantenimiento");
            return user_recuperado;
        }

        public void setClave_save(String clave){
            clave_save = clave;
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = conf.edit();
            editor.putString("dclave", clave_save);
            editor.commit();
        }

        public String getClave_save(){
            SharedPreferences conf = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
            String clave_recuperada = conf.getString("dclave","default");
            return  clave_recuperada;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        String versionName = "Version "+BuildConfig.VERSION_NAME;
        TextView tversion = (TextView) findViewById(R.id.tversion);
        tversion.setText(versionName);
    }

    public void TrabajoNormal(View view){
        opcion = 0;
        Intent trabNormal = new Intent(Inicio.this, Escaneado.class);
        trabNormal.putExtra("opcion", opcion);
        startActivity(trabNormal);
        finish();
    }

    public void RPRTCD(View view) {
        opcion = 1;
        Intent retrabajo = new Intent(Inicio.this, Escaneado.class);
        retrabajo.putExtra("opcion", opcion);
        startActivity(retrabajo);
        finish();
    }

    public void Pausar(View view){
        opcion = 3;
        Intent pausar = new Intent(Inicio.this, Escaneado.class);
        pausar.putExtra("opcion", opcion);
        startActivity(pausar);
        finish();
    }

    public void Reanudar(View view){
        opcion = 4;
        Intent pausar = new Intent(Inicio.this, Escaneado.class);
        pausar.putExtra("opcion", opcion);
        startActivity(pausar);
        finish();
    }


    public void Exit(View view){
        setResult(RESULT_OK);
        finish();
    }

    public void Ajustes(View view){
        dialogo();
    }

    public void dialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Inicio.this);
        builder.setCancelable(false);
        @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.activity_protegido,null);
        final EditText txtUsuario = mView.findViewById(R.id.username);
        final EditText txtContraseña = mView.findViewById(R.id.password);

        builder.setView(mView)
                // Add action buttons
                .setPositiveButton("Acceder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
                        usuario = global.getUser_save();
                        contraseña = global.getClave_save();

                        if (txtUsuario.getText().toString().equals(usuario) && txtContraseña.getText().toString().equals(contraseña)){
                            //finish();
                            Intent acceso = new Intent(Inicio.this, Ajustes.class);
                            startActivity(acceso);
                        } else {
                            Toast loginError = Toast.makeText(Inicio.this, "Ingrese información correcta",Toast.LENGTH_SHORT);
                            loginError.show();
                            dialogo();
                        }
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
//                        Intent cancelar = new Intent(Inicio.this, Inicio.class);
//                        startActivity(cancelar);
                        //LoginDialogFragment.this.getDialog().cancel();

                    }
                });
        builder.show();
    }
}
