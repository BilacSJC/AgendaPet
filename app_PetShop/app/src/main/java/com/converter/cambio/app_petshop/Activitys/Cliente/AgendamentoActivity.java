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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AgendamentoActivity extends AppCompatActivity {
    private MaterialButton btnSolicitar, btnLimpar;
    private EditText edtNomePet, edtRacaPet, edtNomeUsuario, edtTelefone;
    private TextView txtCusto;
    private TextView txtNomeEmpresa;
    private Spinner spnPortePet;
    private String idUsuario;
    private String idEmpresa;
    private  String servico;

    private Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private MetodosPadraoController m = new MetodosPadraoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_agendamento);
        inicializaCampos();
        getExtraIdUsuario();
        inicializarFirebase();
        configuraNavBar();
        eventosClick();
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
                edtRacaPet.setText("");
                edtNomeUsuario.setText("");
                edtTelefone.setText("");
                edtNomePet.setText("");
            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgendamentoModel agendamentoModel = validaCampos();

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

    private void cadastrarAgendamento(AgendamentoModel agendamentoModel) {
        fireBaseQuery.InsertObjectDb(agendamentoModel, "Agendamento", agendamentoModel.getAge_id(), databaseReference);
    }

    private AgendamentoModel validaCampos() {
        AgendamentoModel a = new AgendamentoModel();
        ValidaCampos v = new ValidaCampos();

        String strMensagemNome = v.vString(edtNomePet.getText().toString());
        String strMensagemTelefone = v.vStringTelefone(edtTelefone.getText().toString());
        String strMensagemCpf = v.vString(edtNomeUsuario.getText().toString());
        String strMensagemSenha = v.vString(edtRacaPet.getText().toString());

        int contMsg = 0;

        if(!strMensagemNome.equals("ok")){
            edtNomePet.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemCpf.equals("ok")){
            edtTelefone.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemTelefone.equals("ok")){
            edtNomeUsuario.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemSenha.equals("ok")){
            edtRacaPet.setError(strMensagemNome);
            contMsg += 1;
        }

        if(contMsg > 0){
            return new AgendamentoModel();
        }

        a.setAge_id(UUID.randomUUID().toString());
        a.setAge_cli_id(idUsuario);
        a.setAge_data(edtTelefone.getText().toString().trim());
        a.setAge_empresa_id(idEmpresa);
        a.setAge_pet_id(getPetId());
        a.setAge_status("Aguardando Atendimento");

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        a.setAge_data(dataFormatada);
        a.setAge_hora("15:00");

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
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AgendamentoActivity.this, LocalizaPetShopActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:break;
        }
        return true;
    }

    private void inicializaCampos() {
        btnSolicitar = findViewById(R.id.age_btn_solicitar);
        btnLimpar = findViewById(R.id.age_btn_limpar);
        edtNomePet = findViewById(R.id.age_nome_pet);
        spnPortePet = findViewById(R.id.age_porte_pet);
        edtRacaPet = findViewById(R.id.age_raca_pet);
        edtNomeUsuario = findViewById(R.id.age_txt_nome_usuario);
        edtTelefone = findViewById(R.id.age_txt_telefone);
        txtCusto = findViewById(R.id.age_txt_custo);
        txtNomeEmpresa = findViewById(R.id.age_txt_nome_empresa);

        context = AgendamentoActivity.this;
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        idEmpresa = getIntent().getStringExtra("ID_EMPRESA");
        servico = getIntent().getStringExtra("SERVICO");
    }
}
