package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlterarServicoActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();
    private MetodosPadraoController m = new MetodosPadraoController();
    private ValidaCampos v = new ValidaCampos();

    private Context context = AlterarServicoActivity.this;

    private String idUsuario, idServico;

    private EditText alt_ser_edt_nome_servico, alt_ser_edt_preco;
    private Button alt_ser_btn_alterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_alterar_servico);

        inicializarFirebase();
        inicializaComponentes();
        configuraNavBar();
        getExtraIdUsuario();
        eventosClick();
    }

    private void eventosClick() {
        alt_ser_btn_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ServicoEmpresaModel servicoModel = validaCampos();

                if (servicoModel.getSer_id() == null) {
                    m.alertDialog(AlterarServicoActivity.this, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }

                cadastrarServico(servicoModel);

                Intent intent = new Intent(AlterarServicoActivity.this, ServicosActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();

            }
        });
    }

    private void cadastrarServico(ServicoEmpresaModel servicoModel) {
        fireBaseQuery.UpdateObjetcDb(servicoModel, "Servicos", servicoModel.getSer_id(), databaseReference);

        if (databaseReference.getDatabase() != null) {
            m.alertDialog(AlterarServicoActivity.this, "SUCESSO!", "Serviço atualizado com sucesso!");
        }
    }

    private ServicoEmpresaModel validaCampos() {
        boolean booPreco = v.validacaoBasicaStr(alt_ser_edt_preco.getText().toString());
        boolean booNome = v.validacaoBasicaStr(alt_ser_edt_nome_servico.getText().toString());

        if (booNome && booPreco) {
            ServicoEmpresaModel servicoModel = new ServicoEmpresaModel();

            servicoModel.setSer_id(idServico);
            servicoModel.setSer_emp_id(idUsuario);
            servicoModel.setSer_nome(alt_ser_edt_nome_servico.getText().toString().trim());
            servicoModel.setSer_preco(alt_ser_edt_preco.getText().toString().trim());

            return servicoModel;
        }
        return new ServicoEmpresaModel();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(AlterarServicoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializaComponentes(){
        alt_ser_edt_nome_servico = findViewById(R.id.alt_ser_emp_nome_servico);
        alt_ser_edt_preco = findViewById(R.id.alt_ser_emp_preco);

        alt_ser_btn_alterar = findViewById(R.id.alt_ser_bnt_alterar);
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        idServico = getIntent().getStringExtra("ID_SERVICO");
        getServicoDados();
    }

    private void getServicoDados(){
        databaseReference.child("Servicos").orderByChild("ser_id").equalTo(idServico)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ServicoEmpresaModel s = objSnp.getValue(ServicoEmpresaModel.class);
                            alt_ser_edt_nome_servico.setText(s.getSer_nome());
                            alt_ser_edt_preco.setText(s.getSer_preco());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void configuraNavBar() {
        setTitle("Alterar Serviço");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AlterarServicoActivity.this, ServicosActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
