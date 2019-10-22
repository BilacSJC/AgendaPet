package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.EnderecoModel;
import com.converter.cambio.app_petshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CadastroClienteActivity extends AppCompatActivity {
    private MaterialButton btnCadastrar;
    private EditText edtEmail, edtNome, edtSenha, edtCpf, edtTelefone, edtLogradouro, edtNumero, edtBairro, edtCep, edtCidade;
    private Spinner spnEstado;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery;
    private MetodosPadraoController m = new MetodosPadraoController();
    private Context context;
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_cadastro);
        inicializaComponentes();
        configuraNavBar();

        inicializarFirebase();
        eventoClicks();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroClienteActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
    }

    private void eventoClicks() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClienteModel clienteModel = validaCampos();

                if(clienteModel.getCli_id() == null){
                    m.alertDialog(context,"ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                EnderecoModel enderecoModel = setEnderecoModel(clienteModel);

                if(enderecoModel.getId_endereco() == null){
                    m.alertDialog(context,"ATENCÃO", "Preencha todos os campos.");
                    return;
                }

                cadastrarUsuario(clienteModel, enderecoModel);
            }
        });
    }

    private ClienteModel validaCampos() {

        ClienteModel c = new ClienteModel();
        ValidaCampos v = new ValidaCampos();

        String strMensagemNome = v.vString(edtNome.getText().toString());
        String strMensagemTelefone = v.vStringTelefone(edtTelefone.getText().toString());
        String strMensagemCpf = v.vStringCpf(edtCpf.getText().toString());
        String strMensagemSenha = v.vStringSenha(edtSenha.getText().toString());
        String strMensagemEmail = v.vStringEmail(edtEmail.getText().toString());

        int contMsg = 0;

        if(!strMensagemNome.equals("ok")){
            edtNome.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemCpf.equals("ok")){
            edtCpf.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemTelefone.equals("ok")){
            edtTelefone.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemSenha.equals("ok")){
            edtSenha.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemEmail.equals("ok")){
            edtEmail.setError(strMensagemNome);
            contMsg += 1;
        }

        if(contMsg > 0){
            return new ClienteModel();
        }

        c.setCli_id(UUID.randomUUID().toString());
        c.setCli_nome(edtNome.getText().toString().trim());
        c.setCli_telefone(edtTelefone.getText().toString().trim());
        c.setCli_cpf(edtCpf.getText().toString().trim());
        c.setCli_email(edtEmail.getText().toString().trim());
        c.setCli_senha(edtSenha.getText().toString().trim());
        c.setCli_senha_antiga(edtSenha.getText().toString().trim());

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        c.setCli_data_ultima_alteracao_senha(dataFormatada);
        c.setCli_id_endereco(UUID.randomUUID().toString().trim());

        return c;
    }

    private EnderecoModel setEnderecoModel(ClienteModel clienteModel) {
        EnderecoModel e = new EnderecoModel();
        ValidaCampos v = new ValidaCampos();

        boolean booMensagemCep = v.validacaoBasicaStr(edtCep.getText().toString());
        boolean booMensagemCidade = v.validacaoBasicaStr(edtCidade.getText().toString());
        boolean booMensagemNumero = v.validacaoBasicaStr(edtNumero.getText().toString());
        boolean booMensagemLogradouro = v.validacaoBasicaStr(edtLogradouro.getText().toString());
        int intPositionSelected = spnEstado.getSelectedItemPosition();
        boolean strMensagemBairro = v.validacaoBasicaStr(edtBairro.getText().toString());

        int contMsg = 0;
        String strMsg = "Preencha este campo";
        if (intPositionSelected <= 0) {
            m.alertDialog(context, "ATENÇÃO", "Selecione um estado");
            contMsg += 1;
        }
        if (!booMensagemCep) {
            edtCep.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemCidade) {
            edtCidade.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemNumero) {
            edtNumero.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemLogradouro) {
            edtLogradouro.setError(strMsg);
            contMsg += 1;
        }
        if (!strMensagemBairro) {
            edtBairro.setError(strMsg);
            contMsg += 1;
        }

        if (contMsg > 0) {
            return new EnderecoModel();
        }

        e.setId_usuario(clienteModel.getCli_id());
        e.setId_endereco(clienteModel.getCli_id_endereco());
        e.setEstado(spnEstado.getSelectedItem().toString().trim());
        e.setCidade(edtCidade.getText().toString().trim());
        e.setBairro(edtBairro.getText().toString().trim());
        e.setLogradouro(edtLogradouro.getText().toString().trim());
        e.setNumero(edtNumero.getText().toString().trim());
        e.setCep(edtCep.getText().toString().trim());

        return e;
    }

    private void cadastrarUsuario(ClienteModel clienteModel, EnderecoModel enderecoModel){
        fireBaseQuery.InsertObjectDb(clienteModel, "Cliente", clienteModel.getCli_id(), databaseReference);

        if(databaseReference.getDatabase() != null){
            fireBaseQuery.InsertObjectDb(enderecoModel, "Endereco", enderecoModel.getId_endereco(), databaseReference);
            cadastrarLoginUsuario(clienteModel);
        }
    }

    private void cadastrarLoginUsuario(final ClienteModel clienteModel){
        auth.createUserWithEmailAndPassword(clienteModel.getCli_email(), clienteModel.getCli_senha())
                .addOnCompleteListener(CadastroClienteActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // task retorna o status da autenticação
                                if(task.isSuccessful()){
                                    limparCampos();
                                    alertDialogBackToLogin("Sucesso!","Usuário cadastrado com sucesso!");
                                }else{
                                    m.alertToast(context,"Erro ao cadastrar. Tente novamente.");
                                }
                            }
                        }
                );
    }

    private void alertDialogBackToLogin(String strTitle, String strMsg) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CadastroClienteActivity.this, LoginClienteActivity.class);
                        startActivity(intent);
                        finish();
                    } }).show();
    }

    private void preencheSpinnerEstados() {
        List<String> lsEstados = geradorListSpinnerController.getLstEstados();
        ArrayAdapter<String> estado = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lsEstados);
        spnEstado.setAdapter(estado);
    }

    private void limparCampos() {
        edtEmail.setText("");
        edtNome.setText("");
        edtSenha.setText("");
        edtCpf.setText("");
        edtTelefone.setText("");
    }

    private void configuraNavBar() {
        setTitle("Cadastro de Usuário");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CadastroClienteActivity.this, LoginClienteActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void inicializaComponentes() {
        btnCadastrar = findViewById(R.id.cad_btn_cadastrar);
        edtEmail = findViewById(R.id.cad_usu_ed_email);
        edtNome = findViewById(R.id.cad_usu_ed_nome);
        edtSenha = findViewById(R.id.cad_usu_ed_senha);
        edtCpf = findViewById(R.id.cad_usu_ed_cpf);
        edtTelefone = findViewById(R.id.cad_usu_ed_telefone);
        edtLogradouro = findViewById(R.id.per_usu_edt_logradouro);
        edtNumero = findViewById(R.id.per_usu_edt_numero);
        edtBairro = findViewById(R.id.per_usu_edt_bairro);
        edtCep = findViewById(R.id.per_usu_edt_cep);
        edtCidade = findViewById(R.id.per_usu_edt_cidade);
        spnEstado = findViewById(R.id.cad_usu_spn_estado);
        fireBaseQuery  = new FireBaseQuery();
        context = CadastroClienteActivity.this;
    }
}
