package com.converter.cambio.app_petshop.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.converter.cambio.app_petshop.Activitys.Cliente.LoginClienteActivity;
import com.converter.cambio.app_petshop.Activitys.Empresa.LoginEmpresaActivity;
import com.converter.cambio.app_petshop.R;

public class TipoLoginActivity extends AppCompatActivity {

    private Button btnCliente;
    private Button btnEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_login);

        btnCliente = findViewById(R.id.btnCliente);
        btnEmpresa = findViewById(R.id.btnEmpresa);


        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginClienteActivity.class);
                startActivity(intent);
            }
        });

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginEmpresaActivity.class);
                startActivity(intent);
            }
        });
    }
}
