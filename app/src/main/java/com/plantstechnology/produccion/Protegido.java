package com.plantstechnology.produccion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Protegido extends AppCompatActivity {
    String usuario = "";
    String contraseña = "";
    //View mContra = R.layout.activity_ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Inicio.classGlobal global = (Inicio.classGlobal) getApplication();
        usuario = global.getUser_save();
        contraseña = global.getClave_save();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protegido);
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setContraseña(String contraseña) {
        EditText newContraseña = (EditText) findViewById(R.id.password);
        this.contraseña = newContraseña.getText().toString();
    }

    public void setUsuario(String usuario) {
        EditText newUsuario = (EditText) findViewById(R.id.username);
        this.usuario = newUsuario.getText().toString();
    }


}
