package com.converter.cambio.app_petshop.Activitys.Empresa;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.ResetSenhaActivity;
import com.converter.cambio.app_petshop.Activitys.TipoLoginActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginEmpresaActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FireBaseQuery firebaseQuery = new FireBaseQuery();
    private FirebaseAuth auth;
    private EditText txtEmail, txtSenha;
    private TextView txtEsqueceuSenha;
    private MaterialButton btnCadastrar, btnLogin;
    private EmpresaModel empresa = new EmpresaModel();
    private ValidaCampos validaCampos = new ValidaCampos();
    private Date data = new Date();
    private String idUsuario;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_login);
        inicializaComponentes();
        inicializarFirebase();
        configuraNavBar();
        eventosClick();
    }

    private void configuraNavBar() {
        setTitle("Login");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LoginEmpresaActivity.this, TipoLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginEmpresaActivity.this, TipoLoginActivity.class);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(LoginEmpresaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventosClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = txtEmail.getText().toString().trim();
                String strSenha = txtSenha.getText().toString().trim();

                if (validaInputUsuario(strEmail, strSenha)) { return; }

                loginFirebase(strEmail, strSenha);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmpresaActivity.this, CadastroEmpresaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginEmpresaActivity.this, ResetSenhaActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean validaInputUsuario(String strEmail, String strSenha) {
        if(strEmail.trim().equals("") || strSenha.equals("")){
            if (strEmail.equals("") && !strSenha.equals("")){
                txtEmail.setError("O E-mail deve ser preenchido!");
                return true;
            }
            else if(strSenha.equals("") && !strEmail.equals("")){
                txtSenha.setError("A Senha deve ser preenchida!");
                return true;
            }
            txtSenha.setError("A Senha deve ser preenchida!");
            txtEmail.setError("O E-mail deve ser preenchido!");
            alertDialog("ATENÇÃO!","Todos os campos devem ser preenchidos!");
            return true;
        }
        return false;
    }

    private void loginFirebase(final String strEmail, final String strSenha) {

        auth.signInWithEmailAndPassword(strEmail, strSenha)
                .addOnFailureListener(LoginEmpresaActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        altertToast("E-mail ou senha inválidos!");
                    }
                })
                .addOnCanceledListener(LoginEmpresaActivity.this, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        altertToast("E-mail ou senha inválidos!");
                    }
                })
                .addOnCompleteListener(LoginEmpresaActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //Get Empresa no banco e valida se senha foi alterada a menos de 60 dias
                            databaseReference.child("Empresa").orderByChild("emp_email").equalTo(strEmail)
                                    .addValueEventListener(new ValueEventListener(){
                                        @Override
                                        public void onDataChange(DataSnapshot dSnp)
                                        {
                                            if(!dSnp.hasChildren()){
                                                altertToast("E-mail ou senha inválidos!");
                                            }
                                            for(DataSnapshot objSnp : dSnp.getChildren())
                                            {
                                                EmpresaModel c = objSnp.getValue(EmpresaModel.class);
                                                idUsuario = c.getEmp_id();

                                                if(!strEmail.trim().equals(c.getEmp_email()) || !strSenha.trim().equals(strSenha)){

                                                    altertToast("E-mail ou senha inválidos!");
                                                    return;
                                                }
                                                //VERIFICA SE O USUÁRIO ALTEROU A SENHA
                                                verificaSeSenhaFoiAlterada(c, strSenha);

                                                break;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    });
                        }else{ altertToast("E-mail ou senha inválidos!"); }
                    }
                });

    }

    private void validaPrazoAlteracaoSenha(EmpresaModel e) {
        boolean booSenhaIsValida =  validaCampos.senhaIsValida(e.getEmp_data_ultima_alteracao_senha());

        if(!booSenhaIsValida){//Senha inválida
            alertDialogRecSenha("ATENÇÃO!", "Renove sua senha para acessar o aplicativo!");
            return;
        }

        if(e.getEmp_usu_tipo().equals("Empresa")){
            if(idUsuario != null && booSenhaIsValida){//Loga e inicia Sessão
                usuarioLogadoStartSession();
            }else {
                alertDialog("ATENÇÃO", "E-mail ou senha inválidos!");
            }
        }else {
            alertDialog("ATENÇÃO", "E-mail ou senha inválidos!");
        }
    }



    private void verificaSeSenhaFoiAlterada(EmpresaModel e, String strSenha) {
        if(!e.getEmp_senha().trim().equals(strSenha)){
            //Get data Atual
            SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy");
            String strDataAtual = formatData.format(data);

            e.setEmp_senha(strSenha);
            e.setEmp_senha_antiga(strSenha);
            e.setEmp_data_ultima_alteracao_senha(strDataAtual.trim());

            firebaseQuery.UpdateObjetcDb(e, "Empresa", e.getEmp_id(), databaseReference);

            usuarioLogadoStartSession();
        }else{
            //VERIFICA SE O PRAZO DE ALTERAÇÃO DE SENHA ESTÁ EM DIA
            validaPrazoAlteracaoSenha(e);
        }
    }

    private void usuarioLogadoStartSession() {
        Intent i = new Intent(LoginEmpresaActivity.this, HomeEmpActivity.class);
        i.putExtra("ID_USUARIO", idUsuario);
        startActivity(i);
        finish();
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

    private void  alertDialogRecSenha(String strTitle, String strMsg){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LoginEmpresaActivity.this, ResetSenhaActivity.class);
                        startActivity(intent);
                        finish();
                    } }).show();
    }

    private void altertToast(String strMsgm){
        Toast.makeText(LoginEmpresaActivity.this, strMsgm, Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {
        txtEmail = findViewById(R.id.login_txt_email_emp);
        txtSenha = findViewById(R.id.login_txt_senha_emp);
        btnCadastrar = findViewById(R.id.login_btn_cadastrar_empresa);
        btnLogin = findViewById(R.id.login_btn_login_emp);
        txtEsqueceuSenha = findViewById(R.id.txt_esqueceu_senha_emp);

    }
}