package com.converter.cambio.app_petshop.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.converter.cambio.app_petshop.Activitys.Cliente.LoginClienteActivity;
import com.converter.cambio.app_petshop.Activitys.Empresa.LoginEmpresaActivity;
import com.converter.cambio.app_petshop.R;

public class TipoLoginActivity extends AppCompatActivity {

    private Button btnCliente;
    private Button btnEmpresa;
    private Context context = TipoLoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_login);

        btnCliente = findViewById(R.id.btnCliente);
        btnEmpresa = findViewById(R.id.btnEmpresa);


        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TipoLoginActivity.this, LoginClienteActivity.class);
                startActivity(intent);
            }
        });

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TipoLoginActivity.this, LoginEmpresaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
            alertDialogOnBackPressed("Sair?", "Deseja sair do app?");
    }

    private void alertDialogOnBackPressed(String strTitle, String strMsg) {
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // m.alertToast(context, "Nenhuma ação foi realizada.");
                    }
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                }).show();
    }
}
