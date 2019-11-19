package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Empresa.Adapter.ListaAdapterServicos;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicosActivity extends AppCompatActivity {

    private FloatingActionButton fabAddServico;
    private String idUsuario;
    private ListView lstServicos;
    private List<ServicoEmpresaModel> lstServicoEmpresaModel = new ArrayList<>();
    private DatabaseReference databaseReference;
    private MetodosPadraoController m = new MetodosPadraoController();
    private FirebaseDatabase firebaseDatabase;

//    private String intentOrigem = "ServicosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_lst_servicos_empresa);
        getExtraIdUsuario();
        configuraNavBar();
        inicializarFirebase();

        lstServicos = findViewById(R.id.lst_servicos);
        getLstServicoEmpresaModel();

            fabAddServico = findViewById(R.id.fab_add_servico);

        fabAddServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicosActivity.this, ServicosAdd.class);
                intent.putExtra("ID_USUARIO", idUsuario);
//                intent.putExtra("INTENT_ORIGEM", intentOrigem);
                startActivity(intent);
                finish();
            }
        });

        lstServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLstServicoEmpresaModel() {
        databaseReference.child("Servicos").orderByChild("ser_emp_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        List<ServicoEmpresaModel> lstVazia = new ArrayList<>();
                        lstServicoEmpresaModel = lstVazia;
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ServicoEmpresaModel s = objSnp.getValue(ServicoEmpresaModel.class);
                            lstServicoEmpresaModel.add(s);
                        }

                        if (lstServicoEmpresaModel.size() <= 0) {
                            m.alertToast(ServicosActivity.this, "Você não possui nenhum serviço cadastrado!");
                        } else {

                            atualizaLista(lstServicoEmpresaModel);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void atualizaLista(final List<ServicoEmpresaModel> listServicos) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try{
                    ListaAdapterServicos filaAdapter = new ListaAdapterServicos(idUsuario, listServicos, ServicosActivity.this);
                    lstServicos.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(ServicosActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void configuraNavBar() {
        setTitle("Listar Serviços");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ServicosActivity.this, HomeEmpActivity.class);
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
        Intent intent = new Intent(ServicosActivity.this, HomeEmpActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }
}
