package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.Adapter.ListaAdapter;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
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

public class HistoricoAgendamentosActivity extends AppCompatActivity {
    private String idUsuario;
    private ListView lstAgendamentos;
    private List<AgendamentoViewModel> lstAgendamentoModel = new ArrayList<>();
    private DatabaseReference databaseReference;
    private MetodosPadraoController m = new MetodosPadraoController();
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_historico_agendamentos);
        configuraNavBar();
        getExtraIdUsuario();
        inicializarFirebase();
        lstAgendamentos = findViewById(R.id.hst_lst_age);
        getLstAgendamentoModel();

        lstAgendamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"OKOK",Toast.LENGTH_SHORT).show();
            }
        });
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
                            lstAgendamentoModel.add(a);
                        }

                        if(lstAgendamentoModel.size() <= 0){
                            m.alertToast(HistoricoAgendamentosActivity.this,"Você não possui nenhum agendamento");
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
                    ListaAdapter filaAdapter = new ListaAdapter(idUsuario, listAgendamentos, HistoricoAgendamentosActivity.this);
                    lstAgendamentos.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(HistoricoAgendamentosActivity.this);
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
                Intent intent = new Intent(HistoricoAgendamentosActivity.this, PaginaPrincipalActivity.class);
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
