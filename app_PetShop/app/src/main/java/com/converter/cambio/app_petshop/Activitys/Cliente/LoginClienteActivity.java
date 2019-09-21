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
    private FirebaseAuth auth;
    private EditText txtEmail, txtSenha;
    private TextView txtEsqueceuSenha;
    private MaterialButton btnCadastrar, btnLogin;
    private ClienteModel cliente = new ClienteModel();
    private List<ClienteModel> lstCliente = new ArrayList<ClienteModel>();
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

                if (validaInputUsuario(strEmail, strSenha)) return;

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
                    iniciaSessaoCliente(strEmail);
                }else{
                    altertToast("E-mail ou senha inválidos!");
                }
            }
        });
    }

    private void iniciaSessaoCliente(String strEmail) {
        databaseReference.child("Cliente").orderByChild("cli_email").equalTo(strEmail)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    ClienteModel cliente = objSnapshot.getValue(ClienteModel.class);

                    idUsuario = cliente.getCli_id();


                    boolean booSenhaVencida = verificaSeSenhaEstaVencida(cliente.getCli_data_ultima_alteracao_senha());

                    if(idUsuario != null && booSenhaVencida == false){
                        Intent i = new Intent(LoginClienteActivity.this, PaginaPrincipalActivity.class);
                        i.putExtra("ID_USUARIO", idUsuario);
                        startActivity(i);
                        finish();
                    }
                    else {
                        alertDialog("ATENÇÃO", "E-mail ou senha inválidos!");
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private boolean verificaSeSenhaEstaVencida(String strDataUltimaAlteracao) {

        SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String strDataAtual = formatData.format(data);

        if(!strDataUltimaAlteracao.trim().equals("")){
            long lngDataAlteracao = Long.valueOf(strDataUltimaAlteracao.trim().replace("-", ""));
            long lngDataAtual = Long.valueOf(strDataAtual.trim().replace("-",""));

            long lngDias = lngDataAtual - lngDataAlteracao;
            if(lngDias > 60){
                alertDialog("ATENÇÃO!", "Renove sua senha para acessar o sistema!");
            }
            return false;
        }

        return false;
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
