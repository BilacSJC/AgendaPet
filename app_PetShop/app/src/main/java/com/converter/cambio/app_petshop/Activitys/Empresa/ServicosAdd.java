package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Activitys.Cliente.CadastroClienteActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicosAdd extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();
    private ValidaCampos v = new ValidaCampos();
    private MetodosPadraoController m = new MetodosPadraoController();
    private GeradorListSpinnerController geraSpinner = new GeradorListSpinnerController();

    private Spinner spnPorte;
    private EditText edtNome, edtPreco;
    private MaterialButton btnCadastrar;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_add_servico);

        inicializaCampos();
        inicializarFirebase();
        eventosClick();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(ServicosAdd.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventosClick() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServicoEmpresaModel servicoModel = validaCampos();

                if(servicoModel.getSer_id() == null){
                    m.alertDialog(ServicosAdd.this,"ATENCÃO", "Preencha todos os campos.");
                    return;
                }

                cadastrarServico(servicoModel);
            }
        });
    }

    private void cadastrarServico(ServicoEmpresaModel servicoModel){
        fireBaseQuery.InsertObjectDb(servicoModel, "Servicos", servicoModel.getSer_id(), databaseReference);

        if(databaseReference.getDatabase() != null){
            m.alertDialog(ServicosAdd.this, "SUCESSO!", "Serviço cadastrado com sucesso!");
            limparCampos();
        }
    }

    private void limparCampos() {
        edtNome.setText("");
        edtPreco.setText("");
    }

    private ServicoEmpresaModel validaCampos() {
        boolean booPreco = v.validacaoBasicaStr(edtPreco.getText().toString());
        boolean booNome = v.validacaoBasicaStr(edtNome.getText().toString());

        if(booNome && booPreco){
            ServicoEmpresaModel servicoModel = new ServicoEmpresaModel();

            servicoModel.setSer_id(UUID.randomUUID().toString().trim());
            servicoModel.setSer_emp_id(idUsuario);
            servicoModel.setSer_nome(edtNome.getText().toString().trim());
            servicoModel.setSer_preco("R$ " + edtPreco.getText().toString().trim() + ",00");

            return servicoModel;
        }
        return new ServicoEmpresaModel();
    }

    private void inicializaCampos() {
        edtNome = findViewById(R.id.ser_emp_nome);
        edtPreco = findViewById(R.id.ser_emp_preco);
        btnCadastrar = findViewById(R.id.btn_add_servico);
//       spnPorte = findViewById(R.id.ser_spn_porte);


//        List<String> lstEstados = new ArrayList<>();
//        lstEstados = geraSpinner.getLstPorte();
//        ArrayAdapter<String> estados = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lstEstados);
//        spnPorte.setAdapter(estados);

        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }
}
