package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Activitys.Cliente.LoginClienteActivity;
import com.converter.cambio.app_petshop.Activitys.Empresa.Adapter.ListaAdapterSolicitacoes;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricoSolicitacoesActivity extends AppCompatActivity {

    private ListView lstAgendamentos;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FireBaseQuery firebaseQuery = new FireBaseQuery();
 
    private EditText txtEmail, txtSenha;
    private TextView txtEsqueceuSenha;
    private Date data = new Date();
    
    private MetodosPadraoController m = new MetodosPadraoController();
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_historico_solicitacoes);
        inicializaCampos();
        configuraNavBar();
        inicializarFirebase();

        registerForContextMenu(lstAgendamentos);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(HistoricoSolicitacoesActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializaCampos() {
        lstAgendamentos = (ListView) findViewById(R.id.lstSolicitacoes);
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void configuraNavBar() {
        setTitle("Lista de Usuários");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<AgendamentoModel> lstAgendamentos = new ArrayList<>();

        getLstAgendamentos();

        if(lstAgendamentos == null){

            lstAgendamentos = new ArrayList<>();
        }            

        atualizaLista(lstAgendamentos);
    }

    private void getLstAgendamentos() {
        databaseReference.child("Agendamentos").orderByChild("age_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dSnp)
                    {
                        for(DataSnapshot objSnp : dSnp.getChildren())
                        {
                            AgendamentoModel c = objSnp.getValue(AgendamentoModel.class);
                            
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void atualizaLista(final List<AgendamentoModel> lstAgendamentos) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try{
                    ListaAdapterSolicitacoes filaAdapter = new ListaAdapterSolicitacoes(lstAgendamentos, HistoricoSolicitacoesActivity.this);
                    HistoricoSolicitacoesActivity.this.lstAgendamentos.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}
