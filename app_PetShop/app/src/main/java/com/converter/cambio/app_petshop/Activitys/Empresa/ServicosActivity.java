package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.converter.cambio.app_petshop.R;

public class ServicosActivity extends AppCompatActivity {

    private FloatingActionButton fabAddServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_lst_servicos_empresa);

        fabAddServico = findViewById(R.id.fab_add_servico);

        fabAddServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServicosActivity.this, ServicosAdd.class);
            }
        });
    }
}
