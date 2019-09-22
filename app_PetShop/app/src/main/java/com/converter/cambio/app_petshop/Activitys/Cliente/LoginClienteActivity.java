package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.ResetSenhaActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginClienteActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FireBaseQuery firebaseQuery = new FireBaseQuery();
    private FirebaseAuth auth;
    private EditText txtEmail, txtSenha;
    private TextView txtEsqueceuSenha;
    private MaterialButton btnCadastrar, btnLogin;
    private ClienteModel cliente = new ClienteModel();
    private ValidaCampos validaCampos = new ValidaCampos();
    private List<ClienteModel> lstCliente = new ArrayList<ClienteModel>();
    private Date data = new Date();
    private String idUsuario;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        inicializarFirebase();
        eventosClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(LoginClienteActivity.this);
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
                Intent intent = new Intent(LoginClienteActivity.this, CadastroClienteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginClienteActivity.this, ResetSenhaActivity.class);
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
            .addOnCompleteListener(LoginClienteActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //Get Cliente no banco e valida se senha foi alterada a menos de 60 dias
                    databaseReference.child("Cliente").orderByChild("cli_email").equalTo(strEmail)
                            .addValueEventListener(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dSnp)
                                {
                                    for(DataSnapshot objSnp : dSnp.getChildren())
                                    {
                                        ClienteModel c = objSnp.getValue(ClienteModel.class);
                                        idUsuario = c.getCli_id();

                                        if(!strEmail.trim().equals(c.getCli_email()) || !strSenha.trim().equals(strSenha)){

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

    private void validaPrazoAlteracaoSenha(ClienteModel c) {
        boolean booSenhaIsValida =  validaCampos.senhaIsValida(c.getCli_data_ultima_alteracao_senha());

        if(!booSenhaIsValida){//Senha inválida
            alertDialogRecSenha("ATENÇÃO!", "Renove sua senha para acessar o aplicativo!");
            return;
        }

        if(idUsuario != null && booSenhaIsValida){//Loga e inicia Sessão
            usuarioLogadoStartSession();
        }
        else {
            alertDialog("ATENÇÃO", "E-mail ou senha inválidos!");
        }
    }



    private void verificaSeSenhaFoiAlterada(ClienteModel c, String strSenha) {
        if(!c.getCli_senha().trim().equals(strSenha)){
            //Get data Atual
            SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy");
            String strDataAtual = formatData.format(data);

            c.setCli_senha(strSenha);
            c.setCli_senha_antiga(strSenha);
            c.setCli_data_ultima_alteracao_senha(strDataAtual.trim());

            firebaseQuery.UpdateObjetcDb(c, "Cliente", c.getCli_id(), databaseReference);

            usuarioLogadoStartSession();
        }else{
            //VERIFICA SE O PRAZO DE ALTERAÇÃO DE SENHA ESTÁ EM DIA
            validaPrazoAlteracaoSenha(c);
        }
    }

    private void usuarioLogadoStartSession() {
        Intent i = new Intent(LoginClienteActivity.this, PaginaPrincipalActivity.class);
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
                        Intent intent = new Intent(LoginClienteActivity.this, ResetSenhaActivity.class);
                        startActivity(intent);
                        finish();
                    } }).show();
    }

    private void altertToast(String strMsgm){
        Toast.makeText(LoginClienteActivity.this, strMsgm, Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {
        txtEmail = findViewById(R.id.login_txt_email);
        txtSenha = findViewById(R.id.login_txt_senha);
        btnCadastrar = findViewById(R.id.login_btn_cadastrar);
        btnLogin = findViewById(R.id.login_btn_login);
        txtEsqueceuSenha = findViewById(R.id.txt_esqueceu_senha);

    }
}
