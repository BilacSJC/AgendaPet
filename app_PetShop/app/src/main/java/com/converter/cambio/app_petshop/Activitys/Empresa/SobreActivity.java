package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.converter.cambio.app_petshop.R;

public class SobreActivity extends AppCompatActivity {
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_sobre);
        inicializaCampos();
        getExtraIdUsuario();
        configuraNavBar();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void configuraNavBar() {
        setTitle("Sobre");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SobreActivity.this, HomeEmpActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SobreActivity.this, HomeEmpActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    private void inicializaCampos() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }
}
