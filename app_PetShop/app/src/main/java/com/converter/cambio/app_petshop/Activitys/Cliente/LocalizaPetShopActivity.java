package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
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

public class LocalizaPetShopActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private Spinner spnServicos;
    private Spinner spnEmpresas;
    private MaterialButton btnAgendar;
    private String empId;
    private String idUsuario;
    private List<EmpresaModel> lstEmpresas = new ArrayList<>();
    private List<ServicoEmpresaModel> lstServicos = new ArrayList<>();
    private List<String> lstEmpresaNome = new ArrayList<>();
    private List<String> lstServicoNome = new ArrayList<>();
    private List<String> lstEmpresaId = new ArrayList<>();

    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_map_servicos);
        getExtraIdUsuario();
        inicializarFirebase();
        inicializaComponentes();
        configuraNavBar();
        preencheSpinnerEmpresas();

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spnEmpresas.getSelectedItemPosition() <= 0 && spnServicos.getSelectedItemPosition() <= 0){
                    if(spnServicos.getSelectedItemPosition() == 0){
                        alertDialog("ATENÇÃO", "Selecione Uma empresa e um serviço.");
                    }
                }else{
                    Intent intent = new Intent(LocalizaPetShopActivity.this, AgendamentoActivity.class);
                    intent.putExtra("ID_USUARIO", idUsuario);
                    intent.putExtra("ID_EMPRESA", empId);
                    intent.putExtra("SERVICO", spnServicos.getSelectedItem().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

        spnEmpresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                empId = getEmpId(position);
                databaseReference.child("Servicos").orderByChild("ser_emp_id").equalTo(empId)
                        .addValueEventListener(new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot dSnp)
                            {
                                List<String> lstVazia = new ArrayList<>();
                                lstServicoNome = lstVazia;
                                List<ServicoEmpresaModel> lstVaziaServicos = new ArrayList<>();
                                lstServicos = lstVaziaServicos;
                                for(DataSnapshot objSnp : dSnp.getChildren())
                                {
                                    ServicoEmpresaModel s = objSnp.getValue(ServicoEmpresaModel.class);
                                    lstServicos.add(s);
                                }

                                if(lstServicos.size() <= 0){
                                    lstServicoNome.add("Nenhum serviço cadastrado");
                                }else{
                                    lstServicoNome.add("Selecione um serviço");
                                }

                                for(int i = 0; i < lstServicos.size(); i++) {
                                    lstServicoNome.add(lstServicos.get(i).getSer_nome() + " - " + lstServicos.get(i).getSer_preco());
                                }
                                ArrayAdapter<String> servicos = new ArrayAdapter<>(LocalizaPetShopActivity.this, R.layout.support_simple_spinner_dropdown_item, lstServicoNome);
                                spnServicos.setAdapter(servicos);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configuraNavBar() {
        setTitle("Agendamento");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LocalizaPetShopActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_EMPRESA", empId);
                intent.putExtra("SERVICO", spnServicos.getSelectedItem().toString());
                startActivity(intent);
                // finish();
                break;
            default:
                break;
        }
        return true;
    }

    private String getEmpId(int position) {
        for(int i = 0; i < lstEmpresaId.size(); i++){
            if(position == i && position > 0){
                return lstEmpresaId.get(i-1);
            }
        }
        return "";
    }

    private void inicializaComponentes() {
        btnAgendar = findViewById(R.id.map_btn_agendar);
        spnEmpresas = findViewById(R.id.map_spnEmpresas);
        spnServicos = findViewById(R.id.map_spnServicos);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(LocalizaPetShopActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void preencheSpinnerEmpresas() {

        databaseReference.child("Empresa").orderByChild("emp_id")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dSnp)
                    {
                        for(DataSnapshot objSnp : dSnp.getChildren())
                        {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);
                            lstEmpresas.add(e);
                            lstEmpresaId.add(e.getEmp_id());
                        }

                        if(lstEmpresas.size() <= 0){
                            lstEmpresaNome.add("Nenhuma empresa cadastrada");
                        }else{
                            lstEmpresaNome.add("Selecione uma Empresa");
                        }

                        for(int i = 0; i < lstEmpresas.size(); i++) {
                            lstEmpresaNome.add(lstEmpresas.get(i).getEmp_nome());
                        }
                        ArrayAdapter<String> empresas = new ArrayAdapter<>(LocalizaPetShopActivity.this, R.layout.support_simple_spinner_dropdown_item, lstEmpresaNome);
                        spnEmpresas.setAdapter(empresas);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
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
