package com.plantstechnology.produccion;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ajustes extends AppCompatActivity {

    //View obtencion = getLayoutInflater().inflate(R.layout.activity_protegido,null);
    //Protegido protegido = new Protegido();
    String valip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        TextView text_ip = (TextView) findViewById(R.id.dip);
        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
        valip = global.getIp_save();

        text_ip.setText(valip);
    }

    @Override
    public void onBackPressed() {
        Intent regresar = new Intent(Ajustes.this, Inicio.class);
        startActivity(regresar);
        super.onBackPressed();
    }

    public void guardar(View view){
//        final EditText txtContraseña = (EditText) obtencion.findViewById(R.id.password);
//        protegido.setContraseña(txtContraseña.getText().toString());
//        final EditText txtUsuario = (EditText) obtencion.findViewById(R.id.username);
//        protegido.setUsuario(txtUsuario.getText().toString());
        TextView text_ip = (TextView) findViewById(R.id.dip);
        TextView text_user = (TextView) findViewById(R.id.duser);
        TextView text_clave = (TextView) findViewById(R.id.contra);

        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();

        if (!text_user.getText().toString().equals(global.getUser_save().toString())){
            global.setUser_save(text_user.getText().toString());
        }

        if (!text_clave.getText().toString().equals(global.getClave_save().toString())){
            global.setClave_save(text_clave.getText().toString());
        }

        if (!text_ip.getText().toString().equals(global.getIp_save().toString())) {
            global.setIp_save(text_ip.getText().toString());
        }

        Toast okresult = Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT);
        okresult.show();
    }

    public void cancel(View view){
        setResult(RESULT_OK);
        finish();
    }
}
