package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.converter.cambio.app_petshop.R;

public class LoginEmpresaActivity extends AppCompatActivity {

    private MaterialButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);

        btnCadastrar = findViewById(R.id.login_btn_cadastrar_empresa);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmpresaActivity.this, CadastroEmpresaActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}