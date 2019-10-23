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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.EnderecoModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {
    private EditText edtNome,edtCpf, edtTelefone, edtCep, edtLogradouro, edtNumero, edtBairro,edtCidade;
    private Button btnAlterar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUsuario, cli_email, cli_senha, idEndereco, idCliente;
    private Context context = PerfilActivity.this;
    private Spinner spnEstado;
    private MetodosPadraoController m = new MetodosPadraoController();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_perfil);
        inicializarFirebase();
        inicializaComponentes();
        configuraNavBar();
        getExtraIdUsuario();
        eventosClick();
    }

    private void eventosClick() {
        btnAlterar.setOnClickListener(new View.OnClickListener() {
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

        if(contMsg > 0){
            return new ClienteModel();
        }

        c.setCli_id(idCliente);
        c.setCli_nome(edtNome.getText().toString().trim());
        c.setCli_telefone(edtTelefone.getText().toString().trim());
        c.setCli_cpf(edtCpf.getText().toString().trim());
        c.setCli_email(cli_email.trim());
        c.setCli_senha(cli_senha.trim());
        c.setCli_senha_antiga(cli_senha.trim());

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        c.setCli_data_ultima_alteracao_senha(dataFormatada);
        c.setCli_id_endereco(idEndereco);

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
        fireBaseQuery.UpdateObjetcDb(clienteModel, "Cliente", clienteModel.getCli_id(), databaseReference);

        if(databaseReference.getDatabase() != null){
            fireBaseQuery.UpdateObjetcDb(enderecoModel, "Endereco", enderecoModel.getId_endereco(), databaseReference);
        }
        m.alertToast(PerfilActivity.this,"Perfil alterado com sucesso!");
    }


    private void inicializaComponentes() {
        edtNome = findViewById(R.id.per_usu_edt_nome);
        edtCpf = findViewById(R.id.per_usu_edt_cpf);
        edtTelefone = findViewById(R.id.per_usu_edt_telefone);
        spnEstado = findViewById(R.id.per_usu_spn_estado);
        edtCep = findViewById(R.id.per_usu_edt_cep);
        edtLogradouro = findViewById(R.id.per_usu_edt_logradouro);
        edtNumero = findViewById(R.id.per_usu_edt_numero);
        edtBairro = findViewById(R.id.per_usu_edt_bairro);
        edtCidade = findViewById(R.id.per_usu_edt_cidade);
        btnAlterar = findViewById(R.id.per_usu_btn_alterar);

        preencheSpinnerEstados();

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
        user = FireBaseConexao.getFirebaseUser();
        verificaUser();
    }

    private void verificaUser() {
        if(user == null){
            finish();
        }else{
            edtNome.setText("Digite um nome");
        }
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

    private void configuraNavBar() {
        setTitle("Perfil");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PerfilActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void getCliDados() {
        databaseReference.child("Cliente").orderByChild("cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ClienteModel c = objSnp.getValue(ClienteModel.class);
                            edtTelefone.setText(c.getCli_telefone());
                            edtNome.setText(c.getCli_nome());
                            edtCpf.setText(c.getCli_cpf());
                            cli_email = c.getCli_email();
                            cli_senha = c.getCli_senha();
                            idCliente = c.getCli_id();
                            getCliEnd(c.getCli_id_endereco());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        getCliDados();
    }

    private void getCliEnd(String idEndereco) {
        this.idEndereco = idEndereco;
        databaseReference.child("Endereco").orderByChild("id_endereco").equalTo(idEndereco)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EnderecoModel e = objSnp.getValue(EnderecoModel.class);
                            edtLogradouro.setText(e.getLogradouro());
                            setSpnEstado(e.getEstado());
                            edtNumero.setText(e.getNumero());
                            edtBairro.setText(e.getBairro());
                            edtCidade.setText(e.getCidade());
                            edtCep.setText(e.getCep());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
    private void preencheSpinnerEstados() {
        List<String> lsEstados = geradorListSpinnerController.getLstEstados();
        ArrayAdapter<String> estado = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lsEstados);
        spnEstado.setAdapter(estado);
    }

    private void setSpnEstado(String estado) {
        List<String> lsEstados = geradorListSpinnerController.getLstEstados();
        for(int i = 0; i < lsEstados.size(); i++){

            if(lsEstados.get(i).trim().equals(estado.trim())){
                spnEstado.setSelection(i);
                break;
            }
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(PerfilActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
