package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.converter.cambio.app_petshop.R;

public class SobreActivity extends AppCompatActivity {
    private String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_sobre);
        inicializaCampos();
    }

    private void inicializaCampos() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }
}
