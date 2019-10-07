package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.converter.cambio.app_petshop.Activitys.Cliente.CadastroPetActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.HistoricoAgendamentosActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.LocalizaPetSopActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.PaginaPrincipalActivity;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;

public class HomeEmpresaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String idUsuario;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_empresa, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_agendamento) {
            Intent intent = new Intent(HomeEmpresaActivity.this, ServicosAdd.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_historico) {
            Intent intent = new Intent(HomeEmpresaActivity.this, HistoricoSolicitacoesActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(HomeEmpresaActivity.this, PerfilActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(HomeEmpresaActivity.this, SobreActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_pet) {
            Intent intent = new Intent(HomeEmpresaActivity.this, ServicoEmpresaModel.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_sair) {
            finish();
        }

        Intent intent = getIntent();

        if(intent != null){
            String texto = intent.getStringExtra("ok");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
