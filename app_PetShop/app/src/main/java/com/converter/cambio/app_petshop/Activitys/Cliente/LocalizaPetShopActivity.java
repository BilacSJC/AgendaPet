package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocalizaPetShopActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private Spinner spnServicos;
    private Spinner spnEmpresas;
    private MaterialButton btnAgendar;
    private String idUsuario;
    private List<EmpresaModel> lstEmpresas = new ArrayList<>();
    private List<ServicoEmpresaModel> lstServicos = new ArrayList<>();

    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_map_servicos);
        getExtraIdUsuario();
        inicializarFirebase();
        inicializaComponentes();

        preencheSpinnerEmpresas();

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalizaPetShopActivity.this, AgendamentoActivity.class);
                startActivity(intent);
                intent.putExtra("ID_USUARIO", idUsuario);
                finish();
            }
        });
    }

    private void inicializaComponentes() {
        btnAgendar = findViewById(R.id.map_btn_agendar);
        spnEmpresas = findViewById(R.id.map_spnEmpresas);
        spnServicos = findViewById(R.id.map_spnServicos);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(LocalizaPetShopActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void preencheSpinnerEmpresas() {
        getLstEmpresas();
        List<String> lstEmpresaNome = new ArrayList<>();

        if(lstEmpresas.size() <= 0){}

        for(int i = 0; i < lstEmpresas.size(); i++) {
            lstEmpresaNome.add(lstEmpresas.get(i).getEmp_nome());
        }
        ArrayAdapter<String> empresas = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lstEmpresaNome);
        spnEmpresas.setAdapter(empresas);
    }

    private void getLstEmpresas() {

        databaseReference.child("Empresa")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dSnp)
                    {
                        for(DataSnapshot objSnp : dSnp.getChildren())
                        {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);
                            lstEmpresas.add(e);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
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

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

}
