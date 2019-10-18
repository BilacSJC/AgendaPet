package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AlterarAgendamentoActivity extends AppCompatActivity {

    private MaterialButton btnAlterar, btnLimpar;
    private EditText edtData, edtHora;
    private TextView txtNomeEmpresa, txtEnderecoEmpresa, txtServico, txtNomeCliente, txtTelefoneCliente;
    private Spinner spnNomePet, spnPet;
    private List<String> lstPet = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterPet;
    private String idUsuario, idEmpresa, idPet, strServico, strEmpNome, strEmpEnd, strCliNome, strCliTelefone;

    private Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private MetodosPadraoController m = new MetodosPadraoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_alterar_agendamento);
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
                spnPet.setSelection(0);
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgendamentoModel agendamentoModel = validaCampos();

                if (agendamentoModel.getAge_id() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                cadastrarAgendamento(agendamentoModel);

                Intent intent = new Intent(AlterarAgendamentoActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cadastrarAgendamento(AgendamentoModel agendamentoModel) {
        fireBaseQuery.InsertObjectDb(agendamentoModel, "Agendamento", agendamentoModel.getAge_id(), databaseReference);
    }

    private AgendamentoModel validaCampos() {
        AgendamentoModel a = new AgendamentoModel();
        ValidaCampos v = new ValidaCampos();

        String strMensagemData = v.vStringData(edtData.getText().toString().toString());
        String strMensagemHora = v.vStringHora(edtHora.getText().toString().toString());
        int intPositionSelected = spnPet.getSelectedItemPosition();

        int contMsg = 0;

//        if(intPositionSelected <= 0){
//            m.alertDialog(context,"ATENÇÃO!", "Selecione um pet");
//            contMsg = 1;
//        }
        if (!strMensagemData.equals("ok")) {
            edtData.setError(strMensagemData);
            contMsg += 1;
        }
        if (!strMensagemHora.equals("ok")) {
            edtHora.setError(strMensagemHora);
            contMsg += 1;
        }

        if (contMsg > 0) {
            return new AgendamentoModel();
        }

        a.setAge_id(UUID.randomUUID().toString());
        a.setAge_cli_id(idUsuario);
        a.setAge_data_solicitada(edtData.getText().toString().trim());
        a.setAge_hora_solicitada(edtHora.getText().toString().trim());
        a.setAge_emp_id(idEmpresa);
        a.setAge_pet_id(idPet);
        a.setAge_status("Aguardando Atendimento");

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        a.setAge_data_cad(dataFormatada);
        a.setAge_hora_cad("20:42");


        return a;
    }

    private String getPetId() {
        String idPet = "";

        //Fazer Select e preencher os pets do usuario...para pegar o ID

        return idPet;
    }

    private void configuraNavBar() {
        setTitle("Agendamento");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    private void alertDialog(String strTitle, String strMsg) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AlterarAgendamentoActivity.this, LocalizaPetShopActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void preencheSpinnerNomePet() {
        Query query;
        query = databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstPet.clear();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    PetModel p = objSnapshot.getValue(PetModel.class);
                    lstPet.add(p.getPet_nome());
                }
                arrayAdapterPet = new ArrayAdapter<>(AlterarAgendamentoActivity.this, R.layout.support_simple_spinner_dropdown_item, lstPet);
                spnNomePet.setAdapter(arrayAdapterPet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializaCampos() {
        btnAlterar = findViewById(R.id.alt_age_btn_alterar);
        btnLimpar = findViewById(R.id.alt_age_btn_limpar);

        spnPet = findViewById(R.id.alt_age_cli_spn_pet);

        edtData = findViewById(R.id.alt_age_cli_edt_data);
        edtHora = findViewById(R.id.alt_age_cli_edt_hora);

        txtNomeEmpresa = findViewById(R.id.alt_age_cli_txt_nome_empresa);
        txtEnderecoEmpresa = findViewById(R.id.alt_age_cli_txt_endereco_empresa);
        txtServico = findViewById(R.id.alt_age_cli_txt_servico);
        txtNomeCliente = findViewById(R.id.alt_age_cli_txt_nome_cliente);
        txtTelefoneCliente = findViewById(R.id.alt_age_cli_txt_telefone_cliente);

        context = AlterarAgendamentoActivity.this;
        txtServico.setText("Serviço: " + strServico);
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
                            txtNomeCliente.setText("Nome: " + strCliNome);
                            txtTelefoneCliente.setText("Telefone: " + strCliTelefone);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
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
                            txtNomeEmpresa.setText("Empresa: " + strEmpNome);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void getEmpresaEndereco() {
        databaseReference.child("Endereco").orderByChild("id_usuario").equalTo(idEmpresa)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EnderecoModel e = objSnp.getValue(EnderecoModel.class);
                            strEmpEnd = e.getBairro() + " " + e.getNumero() + " " + e.getCidade() + "-" + e.getEstado();
                            txtEnderecoEmpresa.setText("Endereço da empresa: " + strEmpEnd);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}

