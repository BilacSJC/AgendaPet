package com.converter.cambio.app_petshop.Activitys.Empresa;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Activitys.Cliente.PaginaPrincipalActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.PerfilActivity;
import com.converter.cambio.app_petshop.Activitys.TipoLoginActivity;
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

public class CadastroEmpresaActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtCnpj, edtTelefone, edtSenha;
    private EditText edtCidade, edtBairro, edtLogradouro, edtNumero, edtCep;
    private MaterialButton btnCadastrar;
    private Spinner spnEstado;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private FireBaseQuery fireBaseQuery;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Context context;
    private ValidaCampos v;
    private MetodosPadraoController m = new MetodosPadraoController();
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_cadastro);
        inicializarComponentes();
        inicializarFirebase();
        configuraNavBar();
        preencheSpinnerEstados();
        eventosClick();
    }

    private void configuraNavBar() {
        setTitle("Cadastro de Usuário");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CadastroEmpresaActivity.this, LoginEmpresaActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void preencheSpinnerEstados() {
        List<String> lsEstados = geradorListSpinnerController.getLstEstados();
        ArrayAdapter<String> estado = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lsEstados);
        spnEstado.setAdapter(estado);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
    }

    private void eventosClick() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpresaModel empresaModel = validacaoInputUsuario();
                if (empresaModel.getEmp_id() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                EnderecoModel enderecoModel = setEnderecoModel(empresaModel);

                if (enderecoModel.getId_endereco() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }

                cadastrarUsuario(empresaModel, enderecoModel);
            }
        });
    }

    private void cadastrarUsuario(EmpresaModel empresaModel, EnderecoModel enderecoModel) {
        fireBaseQuery.InsertObjectDb(empresaModel, "Empresa", empresaModel.getEmp_id(), databaseReference);
        if (databaseReference.getDatabase() != null) {
            fireBaseQuery.InsertObjectDb(enderecoModel, "Endereco", enderecoModel.getId_endereco(), databaseReference);
            cadastrarLoginUsuario(empresaModel);
        }
    }

    private void cadastrarLoginUsuario(final EmpresaModel empresaModel) {
        auth.createUserWithEmailAndPassword(empresaModel.getEmp_email(), empresaModel.getEmp_senha())
                .addOnCompleteListener(CadastroEmpresaActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // task retorna o status da autenticação
                                if (task.isSuccessful()) {
                                    limparCampos();
                                    alertDialogBackToLogin("Sucesso!", "Usuário cadastrado com sucesso!");
                                } else {
                                    m.alertToast(context, "Erro ao cadastrar. Tente novamente.");
                                }
                            }
                        }
                );
    }

    private EmpresaModel validacaoInputUsuario() {
        EmpresaModel e = new EmpresaModel();
        String strMsgNome = v.vString(edtNome.getText().toString().trim());
        String strMsgCnpj = v.vStringCnpj(edtCnpj.getText().toString().trim());
        String strMsgTelefone = v.vStringTelefone(edtTelefone.getText().toString().trim());
        String strMsgEmail = v.vStringEmail(edtEmail.getText().toString().trim());
        String strMsgSenha = v.vStringSenha(edtSenha.getText().toString().trim());
        String strMsgEstado = v.vStringSpn(spnEstado.getSelectedItem().toString().trim());
        boolean booCidade = v.validacaoBasicaStr(edtCidade.getText().toString().trim());
        boolean booBairro = v.validacaoBasicaStr(edtBairro.getText().toString().trim());
        boolean booLogradouro = v.validacaoBasicaStr(edtLogradouro.getText().toString().trim());
        boolean booNumero = v.validacaoBasicaStr(edtNumero.getText().toString().trim());
        boolean booCep = v.validacaoBasicaStr(edtCep.getText().toString().trim());
        int contMsg = 0;

        if (!strMsgEstado.equals("ok")) {
            m.alertDialog(CadastroEmpresaActivity.this, "ATENÇÃO!", "Selecione um Estado.");
        }
        if (!booCidade) {
            edtCidade.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booBairro) {
            edtBairro.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booLogradouro) {
            edtLogradouro.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booNumero) {
            edtNumero.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booCep) {
            edtCep.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!strMsgNome.equals("ok")) {
            edtNome.setError(strMsgNome);
            contMsg = +1;
        }
        if (!strMsgCnpj.equals("ok")) {
            edtCnpj.setError(strMsgCnpj);
            contMsg = +1;
        }
        if (!strMsgTelefone.equals("ok")) {
            edtTelefone.setError(strMsgTelefone);
            contMsg = +1;
        }
        if (!strMsgEmail.equals("ok")) {
            edtEmail.setError(strMsgEmail);
            contMsg = +1;
        }
        if (!strMsgSenha.equals("ok")) {
            edtSenha.setError(strMsgSenha);
            contMsg = +1;
        }

        if (contMsg > 0) {
            return new EmpresaModel();
        }

        e.setEmp_id(String.valueOf(UUID.randomUUID()));
        e.setEmp_nome(edtNome.getText().toString());
        e.setEmp_cnpj(edtCnpj.getText().toString());
        e.setEmp_email(edtEmail.getText().toString());
        e.setEmp_senha(edtSenha.getText().toString());
        e.setEmp_telefone(edtTelefone.getText().toString());
        e.setEmp_senha_antiga(edtSenha.getText().toString().trim());
        e.setEmp_id_endereco(UUID.randomUUID().toString().trim());

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        e.setEmp_data_ultima_alteracao_senha(dataFormatada);

        return e;
    }

    private EnderecoModel setEnderecoModel(EmpresaModel empresaModel) {
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

        e.setId_usuario(empresaModel.getEmp_id());
        e.setId_endereco(empresaModel.getEmp_id_endereco());
        e.setEstado(spnEstado.getSelectedItem().toString().trim());
        e.setCidade(edtCidade.getText().toString().trim());
        e.setBairro(edtBairro.getText().toString().trim());
        e.setLogradouro(edtLogradouro.getText().toString().trim());
        e.setNumero(edtNumero.getText().toString().trim());
        e.setCep(edtCep.getText().toString().trim());

        return e;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroEmpresaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtCnpj.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
        edtTelefone.setText("");
        btnCadastrar.setText("");
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.cad_emp_edt_nome);
        edtCnpj = findViewById(R.id.cad_emp_edt_cnpj);
        edtEmail = findViewById(R.id.cad_emp_edt_email);
        edtSenha = findViewById(R.id.cad_emp_edt_senha);
        edtTelefone = findViewById(R.id.cad_emp_edt_telefone);
        btnCadastrar = findViewById(R.id.cad_emp_btn_cadastrar);

        edtCidade = findViewById(R.id.cad_emp_edt_cidade);
        edtBairro = findViewById(R.id.cad_emp_edt_bairro);
        edtLogradouro = findViewById(R.id.cad_emp_edt_logradouro);
        edtNumero = findViewById(R.id.cad_emp_edt_numero);
        edtCep = findViewById(R.id.cad_emp_edt_cep);
        spnEstado = findViewById(R.id.cad_emp_spn_estado);

        context = CadastroEmpresaActivity.this;
        v = new ValidaCampos();
        fireBaseQuery = new FireBaseQuery();
    }

    private void alertDialogBackToLogin(String strTitle, String strMsg) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CadastroEmpresaActivity.this, LoginEmpresaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }
}