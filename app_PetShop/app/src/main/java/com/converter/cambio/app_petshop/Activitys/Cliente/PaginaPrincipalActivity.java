package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.Adapter.ListaAdapter;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.R;
import com.converter.cambio.app_petshop.ViewModel.AgendamentoViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String idUsuario, idEmp, idPet;
    private MetodosPadraoController m = new MetodosPadraoController();
    private List<AgendamentoViewModel> lstAgendamentoModel = new ArrayList<>();
    private List<String> lstIdEmp = new ArrayList<>();
    private List<String> lstIdPet = new ArrayList<>();
    private ListView lstAgendamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_home);
        lstAgendamentos = findViewById(R.id.cli_home_lst_age);
        getExtraIdUsuario();
        DrawerLayout drawer;
        configuraNavBar();

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(PaginaPrincipalActivity.this, LocalizaPetShopActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        inicializarFirebase();

        List<AgendamentoModel> lstAgendamento = new ArrayList<>();
        getLstAgendamentoModel();

    }

    private void configuraNavBar() {
        setTitle("Home");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getLstAgendamentoModel() {

        databaseReference.child("Agendamento").orderByChild("age_cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dSnp)
                    {
                        List<AgendamentoViewModel> lstVazia = new ArrayList<>();
                        lstAgendamentoModel = lstVazia;
                        for(DataSnapshot objSnp : dSnp.getChildren())
                        {
                            AgendamentoViewModel a = objSnp.getValue(AgendamentoViewModel.class);
                            if(a.getAlt_age_status().equals("Cancelado")) {
                                a.setAlt_age_status("Aguardando Confirmação");
                                lstAgendamentoModel.add(a);
                            }
//                            if(a.getAlt_age_status().equals("Aguardando Confirmação")) {
//                                lstAgendamentoModel.add(a);
//                            }
//                            lstIdEmp.add(a.getAge_emp_id());
//                            lstIdPet.add(a.getAge_pet_id());
                        }

                        if(lstAgendamentoModel.size() <= 0){
                            m.alertToast(PaginaPrincipalActivity.this,"Você não possui nenhum agendamento");
                        }else{

                                atualizaLista(lstAgendamentoModel);

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void atualizaLista(final List<AgendamentoViewModel> listAgendamentos) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try{
                    ListaAdapter filaAdapter = new ListaAdapter(idUsuario, listAgendamentos, PaginaPrincipalActivity.this);
                    lstAgendamentos.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(PaginaPrincipalActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void  alertDialog(String strTitle, String strMsg){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    } }).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_cli, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatemen
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cli_home) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, PaginaPrincipalActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_agendamento) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, LocalizaPetShopActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_pet) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, CadastroPetActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_historico) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, HistoricoAgendamentosActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_perfil) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, PerfilActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_sobre) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, SobreActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cli_sair) {
            Intent intent = new Intent(PaginaPrincipalActivity.this, LoginClienteActivity.class);
            startActivity(intent);
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
