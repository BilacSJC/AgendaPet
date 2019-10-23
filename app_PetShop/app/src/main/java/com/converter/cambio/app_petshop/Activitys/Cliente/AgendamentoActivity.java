package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.EnderecoModel;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.R;
import com.converter.cambio.app_petshop.ViewModel.AgendamentoViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AgendamentoActivity extends AppCompatActivity {
    private MaterialButton btnSolicitar, btnLimpar;
    private EditText edtData, edtHora;
    private TextView txtNomeEmpresa, txtEnderecoEmpresa, txtServico, txtNomeCliente, txtTelefoneCliente;
    private Spinner spnNomePet;
    private List<String> lstPet = new ArrayList<>();
    private List<String> lstIdPet = new ArrayList<>();
    private String idUsuario, idEmpresa, petNome, petPorte, petRaca, strServico, strEmpNome, strEmpEnd, strCliNome, strCliTelefone;

    private Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private MetodosPadraoController m = new MetodosPadraoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_agendamento);
        inicializarFirebase();
        getExtraIdUsuario();
        inicializaCampos();
        configuraNavBar();
        eventosClick();
        preencheSpinnerNomePet();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventosClick() {
        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtData.setText("");
                edtHora.setText("");
                spnNomePet.setSelection(0);
            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgendamentoViewModel agendamentoModel = validaCampos();

                if(agendamentoModel.getAge_id() == null){
                    m.alertDialog(context,"ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                cadastrarAgendamento(agendamentoModel);
                
                Intent intent = new Intent(AgendamentoActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cadastrarAgendamento(AgendamentoViewModel agendamentoModel) {
        fireBaseQuery.InsertObjectDb(agendamentoModel, "Agendamento", agendamentoModel.getAge_id(), databaseReference);
    }

    private AgendamentoViewModel validaCampos() {
        AgendamentoViewModel a = new AgendamentoViewModel();
        ValidaCampos v = new ValidaCampos();

        String strMensagemData = v.vStringData(edtData.getText().toString().toString());
        String strMensagemHora = v.vStringHora(edtHora.getText().toString().toString());
        int intPositionSelected = spnNomePet.getSelectedItemPosition();

        int contMsg = 0;

//        if(intPositionSelected <= 0){
//            m.alertDialog(context,"ATENÇÃO!", "Selecione um pet");
//            contMsg = 1;
//        }
        if(!strMensagemData.equals("ok")){
            edtData.setError(strMensagemData);
            contMsg += 1;
        }
        if(!strMensagemHora.equals("ok")){
            edtHora.setError(strMensagemHora);
            contMsg += 1;
        }

        if(contMsg > 0){
            return new AgendamentoViewModel();
        }
        a.setAge_cli_id(idUsuario);
        a.setAge_emp_id(idEmpresa);
        a.setAge_id(UUID.randomUUID().toString());
        getDadosPet(spnNomePet.getSelectedItem().toString());
        a.setAlt_age_pet_nome(spnNomePet.getSelectedItem().toString());
        a.setAlt_age_data(edtData.getText().toString().trim());
        a.setAlt_age_hora(edtHora.getText().toString().trim());
        a.setAlt_age_emp_nome(strEmpNome);
        a.setAlt_age_emp_endereco(strEmpEnd);
        a.setAlt_age_servico(strServico);
        a.setAlt_age_cli_nome(strCliNome);
        a.setAlt_age_cli_telefone(strCliTelefone);
        a.setAlt_age_status("Aguardando Atendimento");

        return a;
    }

    private void getDadosPet(String toString) {

    }

//    private void getIdPet(int intPetSelectedPosition) {
//        for (int i = 0; i < lstIdPet.size(); i++){
//            if(i == intPetSelectedPosition){
//                idPet = lstIdPet.get(i);
//                break;
//            }
//        }
//    }

    private void configuraNavBar() {
        setTitle("Agendamento");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AgendamentoActivity.this, LocalizaPetShopActivity.class);
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

    private void preencheSpinnerNomePet() {
        Query query;
        query = databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstPet.clear();

                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    PetModel p = objSnapshot.getValue(PetModel.class);
                    lstPet.add(p.getPet_nome());
                    lstIdPet.add(p.getPet_id());
                }
                ArrayAdapter<String> arrayAdapterPet = new ArrayAdapter<>(AgendamentoActivity.this, R.layout.support_simple_spinner_dropdown_item, lstPet);
                spnNomePet.setAdapter(arrayAdapterPet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializaCampos() {
        btnSolicitar = findViewById(R.id.alt_age_btn_alterar);
        btnLimpar = findViewById(R.id.alt_age_btn_limpar);

        spnNomePet = findViewById(R.id.alt_age_cli_spn_pet);

        edtData = findViewById(R.id.alt_age_cli_edt_data);
        edtHora = findViewById(R.id.alt_age_cli_edt_hora);

        txtNomeEmpresa = findViewById(R.id.alt_age_cli_txt_nome_empresa);
        txtEnderecoEmpresa = findViewById(R.id.alt_age_cli_txt_endereco_empresa);
        txtServico = findViewById(R.id.alt_age_cli_txt_servico);
        txtNomeCliente = findViewById(R.id.alt_age_cli_txt_nome_cliente);
        txtTelefoneCliente = findViewById(R.id.alt_age_cli_txt_telefone_cliente);

        context = AgendamentoActivity.this;
        txtServico.setText("Serviço: "+ strServico);
        getEmpresaNome();
        getCliDados();
        getEmpresaEndereco();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        idEmpresa = getIntent().getStringExtra("ID_EMPRESA");
        strServico = getIntent().getStringExtra("SERVICO");

    }

    private void getCliDados() {
        databaseReference.child("Cliente").orderByChild("cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ClienteModel c = objSnp.getValue(ClienteModel.class);
                            strCliNome = c.getCli_nome().trim();
                            strCliTelefone = c.getCli_telefone();
                            txtNomeCliente.setText("Nome: "+ strCliNome);
                            txtTelefoneCliente.setText("Telefone: "+ strCliTelefone);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getEmpresaNome() {
        databaseReference.child("Empresa").orderByChild("emp_id").equalTo(idEmpresa)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);
                            strEmpNome = e.getEmp_nome().trim();
                            txtNomeEmpresa.setText("Empresa: "+ strEmpNome);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getEmpresaEndereco() {
        databaseReference.child("Endereco").orderByChild("id_usuario").equalTo(idEmpresa)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EnderecoModel e = objSnp.getValue(EnderecoModel.class);
                            strEmpEnd = e.getBairro() + " " +e.getNumero() + " " + e.getCidade() + "-" + e.getEstado();
                            txtEnderecoEmpresa.setText("Endereço da empresa: "+ strEmpEnd);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}
