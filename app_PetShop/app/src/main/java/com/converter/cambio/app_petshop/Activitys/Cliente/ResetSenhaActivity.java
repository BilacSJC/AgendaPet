package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenhaActivity extends AppCompatActivity {

    private EditText edtEmail;
    private MaterialButton btnResetSenha;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_reset_senha);

        inicalizaComponentes();
        configuraNavBar();
        eventosClick();
    }

    private void configuraNavBar() {
        setTitle("Reset Senha");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ResetSenhaActivity.this, LoginClienteActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void eventosClick() {
        btnResetSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edtEmail.getText().toString().trim();
                resetSenha(strEmail);
            }
        });
    }

    private void resetSenha(String strEmail) {
        auth.sendPasswordResetEmail(strEmail)
                .addOnCompleteListener(ResetSenhaActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String strEmailDigitado = edtEmail.getText().toString().trim();
                        if(task.isSuccessful()){
                            alertDialogBackToLogin("ENVIADO!","Foi enviado um link de redefinição de senha para "+
                                    strEmailDigitado);
                        }else{
                            alertDialog("ATENÇÃO!","O e-mail "+strEmailDigitado+" não está cadastrado no aplicativo.");
                        }
                    }
                });
    }

    private void alertDialogBackToLogin(String strTitle, String strMsg) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(ResetSenhaActivity.this, LoginClienteActivity.class);
//                        startActivity(intent);
//                        finish();
                        onBackPressed();
                    } }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void alert(String s) {
        Toast toast = Toast.makeText(ResetSenhaActivity.this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
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

    private void inicalizaComponentes() {
        edtEmail = findViewById(R.id.reset_txt_email);
        btnResetSenha = findViewById(R.id.reset_btn_enviar_email);
    }
}
