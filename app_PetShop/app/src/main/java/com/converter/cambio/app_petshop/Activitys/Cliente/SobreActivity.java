package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.converter.cambio.app_petshop.R;

public class SobreActivity extends AppCompatActivity {
    private Button btnAgendamento;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_cliente);
        getExtraIdUsuario();

        btnAgendamento = findViewById(R.id.sob_btn_agendamento);

        btnAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SobreActivity.this, PaginaPrincipalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }
}