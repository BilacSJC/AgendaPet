package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CadastroEmpresaActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtCnpj, edtTelefone, edtSenha, edtEndereco;
    private Button btnCadastrar;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private FireBaseQuery fireBaseQuery;
    private DatabaseReference databaseReference;

    private ValidaCampos validaCampos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_empresa_teste);

        inicializarComponentes();
        inicializarFirebase();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacaoInputUsuario();
            }
        });

    }

    private EmpresaModel validacaoInputUsuario() {
        EmpresaModel e = new EmpresaModel();

        String strMsgNome = validaCampos.vString(edtNome.getText().toString().trim());
        String strMsgCnpj = validaCampos.vStringCnpj(edtCnpj.getText().toString().trim());
        String strMsgTelefone = validaCampos.vStringTelefone(edtTelefone.getText().toString().trim());
        String strMsgEmail = validaCampos.vStringEmail(edtEmail.getText().toString().trim());
        String strMsgSenha = validaCampos.vStringSenha(edtSenha.getText().toString().trim());
        String strMsgEndereco = validaCampos.vStringEndereco(edtEndereco.getText().toString().trim());

        int contMsg = 0;

        if (!strMsgNome.equals("ok")) {
            edtNome.setError(strMsgNome);
            contMsg=+1;
        }

        if (!strMsgCnpj.equals("ok")) {
            edtCnpj.setError(strMsgCnpj);
            contMsg=+1;
        }

        if (!strMsgTelefone.equals("ok")) {
            edtTelefone.setError(strMsgTelefone);
            contMsg=+1;
        }

        if (!strMsgEmail.equals("ok")) {
            edtEmail.setError(strMsgEmail);
            contMsg=+1;
        }

        if (!strMsgSenha.equals("ok")) {
            edtSenha.setError(strMsgSenha);
            contMsg=+1;
        }

        if (!strMsgEndereco.equals("ok")) {
            edtEndereco.setError(strMsgEndereco);
            contMsg=+1;
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
        e.setEmp_endereco(edtEndereco.getText().toString());

        return e;

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroEmpresaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.cad_emp_edt_nome);
        edtCnpj = findViewById(R.id.cad_emp_edt_cnpj);
        edtEmail = findViewById(R.id.cad_emp_edt_email);
        edtEndereco = findViewById(R.id.cad_emp_edt_endereco);
        edtSenha = findViewById(R.id.cad_emp_edt_senha);
        edtTelefone = findViewById(R.id.cad_emp_edt_telefone);
        btnCadastrar = findViewById(R.id.cad_emp_btn_cadastrar);
    }
}
