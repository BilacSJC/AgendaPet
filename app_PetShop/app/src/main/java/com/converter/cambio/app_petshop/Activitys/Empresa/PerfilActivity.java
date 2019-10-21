package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilActivity extends AppCompatActivity {
    private String idUsuario;
    private EditText edtNome, edtEmail, edtCnpj, edtTelefone, edtSenha;
    private EditText edtCidade, edtBairro, edtLogradouro, edtNumero, edtCep;
    private Button btnAlterar;
    private Spinner spnEstado;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private FireBaseQuery fireBaseQuery;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Context context;
    private ValidaCampos v;
    private MetodosPadraoController m = new MetodosPadraoController();
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_perfil);
        getExtraIdUsuario();
        inicializarComponentes();
        inicializarFirebase();
        configuraNavBar();
        eventosClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
        user = FireBaseConexao.getFirebaseUser();
        verificaUser();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void verificaUser() {
        if (user == null) {
            finish();
        } else {
            edtNome.setText("Digite o nome");
            edtEmail.setText(user.getEmail());
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventosClick() {
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.per_emp_edt_nome);
        edtCnpj = findViewById(R.id.per_emp_edt_cnpj);
        edtEmail = findViewById(R.id.per_emp_edt_email);
        edtSenha = findViewById(R.id.per_emp_edt_senha);
        edtTelefone = findViewById(R.id.per_emp_edt_telefone);

        edtCidade = findViewById(R.id.per_emp_edt_cidade);
        edtBairro = findViewById(R.id.per_emp_edt_bairro);
        edtLogradouro = findViewById(R.id.per_emp_edt_logradouro);
        edtNumero = findViewById(R.id.per_emp_edt_numero);
        edtCep = findViewById(R.id.per_emp_edt_cep);
        spnEstado = findViewById(R.id.per_emp_spn_estado);

        btnAlterar = findViewById(R.id.per_emp_btn_alterar);

        context = PerfilActivity.this;
        v = new ValidaCampos();
        fireBaseQuery = new FireBaseQuery();
    }

    private void configuraNavBar() {
        setTitle("Perfil");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PerfilActivity.this, HomeEmpActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
