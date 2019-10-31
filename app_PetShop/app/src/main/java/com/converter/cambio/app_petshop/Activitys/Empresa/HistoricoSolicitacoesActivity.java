package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.Adapter.ListaAdapter;
import com.converter.cambio.app_petshop.Activitys.Cliente.HistoricoAgendamentosActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.LoginClienteActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.PaginaPrincipalActivity;
import com.converter.cambio.app_petshop.Activitys.Empresa.Adapter.ListaAdapterSolicitacoes;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
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
import java.util.Date;
import java.util.List;

public class HistoricoSolicitacoesActivity extends AppCompatActivity {

    private ListView lstAgendamentos;
    private List<AgendamentoViewModel> lstAgendamentoModel = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FireBaseQuery firebaseQuery = new FireBaseQuery();

    private EditText txtEmail, txtSenha;
    private TextView txtEsqueceuSenha;
    private Date data = new Date();

    private MetodosPadraoController m = new MetodosPadraoController();
    private String idUsuario, idPet,idCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_historico_solicitacoes);
        getExtraIdUsuario();
        configuraNavBar();
        inicializarFirebase();
        lstAgendamentos = findViewById(R.id.hst_lst_sol);
        getLstAgendamentoModel();

        lstAgendamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"OKOK",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getLstAgendamentoModel() {

        databaseReference.child("Agendamento").orderByChild("age_emp_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dSnp)
                    {
                        List<AgendamentoViewModel> lstVazia = new ArrayList<>();
                        lstAgendamentoModel = lstVazia;
                        for(DataSnapshot objSnp : dSnp.getChildren())
                        {
                            AgendamentoViewModel a = objSnp.getValue(AgendamentoViewModel.class);
                            lstAgendamentoModel.add(a);
                        }

                        if(lstAgendamentoModel.size() <= 0){
                            m.alertToast(HistoricoSolicitacoesActivity.this,"Não há nenhuma solicitação no momento");
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
                    ListaAdapterSolicitacoes filaAdapter = new ListaAdapterSolicitacoes(idUsuario, listAgendamentos, HistoricoSolicitacoesActivity.this);
                    lstAgendamentos.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(HistoricoSolicitacoesActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void configuraNavBar() {
        setTitle("Histórico");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(HistoricoSolicitacoesActivity.this, HomeEmpActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
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

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }
}