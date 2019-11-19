package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PerfilPetActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();
    private MetodosPadraoController m = new MetodosPadraoController();
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    private Context context = PerfilPetActivity.this;

    private String idUsuario, idPet;

    private EditText edtPetNome, edtPetRaca, edtPetIdade;
    private Spinner spnPetSexo, spnPetPorte;
    private Button btnAlterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_perfil_pet);

        inicializarFirebase();
        inicializaComponentes();
        configuraNavBar();
        getExtraIdUsuario();
        eventosClick();
    }

    private void eventosClick() {
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetModel pet = validaCampos();

                if(pet.getPet_id() == null){
                    alertDialog("ATENÇÃO", "Preencha todos os campos");
                    return;
                }

                atualizarPet(pet);

                alertDialog("Pet Alterado!", "Pet atualizado com sucesso!");

                Intent intent = new Intent(PerfilPetActivity.this, PetActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            }
        });
    }

    private void atualizarPet(PetModel pet) {
        new FireBaseQuery().UpdateObjetcDb( pet, "Pet", pet.getPet_id(), databaseReference);
    }

    private PetModel validaCampos() {
        PetModel pet = new PetModel();

        ValidaCampos v = new ValidaCampos();

        String strMensagemNome = v.vString(edtPetNome.getText().toString().trim());
        String strMensagemRaca = v.vString(edtPetRaca.getText().toString().trim());
        String strMensagemIdade = v.vInt(edtPetIdade.getText().toString().trim());
        String strMensagemSexo = v.vStringSpn(spnPetSexo.getSelectedItem().toString().trim());
        String strMensagemPorte = v.vStringSpn(spnPetPorte.getSelectedItem().toString().trim());
        int contMsg = 0;


        if(!strMensagemNome.equals("ok")){
            edtPetNome.setError(strMensagemNome);
            contMsg += 1;
        }
        if(!strMensagemIdade.equals("ok")){
            edtPetIdade.setError(strMensagemIdade);
            contMsg += 1;
        }
        if(!strMensagemRaca.equals("ok")){
            edtPetRaca.setError(strMensagemRaca);
            contMsg += 1;
        }
        if(!strMensagemSexo.equals("ok")){
            alertDialog("ATENÇÃO!", strMensagemSexo);
            contMsg += 1;
        }
        if(!strMensagemPorte.equals("ok")){
            alertDialog("ATENÇÃO", strMensagemPorte);
            contMsg += 1;
        }

        if(contMsg > 0){
            return new PetModel();
        }

        pet.setPet_id(idPet.trim());
        pet.setPet_cli_id(idUsuario.trim());
        pet.setPet_nome(edtPetNome.getText().toString().trim());
        pet.setPet_idade(edtPetIdade.getText().toString().trim());
        pet.setPet_raca(edtPetRaca.getText().toString().trim());
        pet.setPet_porte(spnPetPorte.getSelectedItem().toString().trim());
        pet.setPet_sexo(spnPetSexo.getSelectedItem().toString().trim());

        return pet;
    }

    private void  alertDialog(String strTitle, String strMsg){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(PerfilPetActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializaComponentes(){
        edtPetNome = findViewById(R.id.per_pet_edt_nome);
        edtPetRaca = findViewById(R.id.per_pet_edt_raca);
        edtPetIdade = findViewById(R.id.per_pet_edt_idade);
        spnPetSexo = findViewById(R.id.per_pet_spn_sexo);
        spnPetPorte = findViewById(R.id.per_pet_spn_porte);

        btnAlterar = findViewById(R.id.per_pet_btn_alterar);

        List<String> lstSexo = new ArrayList<>();

        lstSexo.add("Selecione o sexo do pet");
        lstSexo.add("Macho");
        lstSexo.add("Fêmea");

        ArrayAdapter sexo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lstSexo);
        spnPetSexo.setAdapter(sexo);

        List<String> lstPorte = new ArrayList<>();

        lstPorte.add("Selecione o porte do pet");
        lstPorte.add("Filhote");
        lstPorte.add("Pequeno");
        lstPorte.add("Médio");
        lstPorte.add("Grande");
        lstPorte.add("Muito Grande");

        ArrayAdapter porte = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lstPorte);
        spnPetPorte.setAdapter(porte);
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        idPet = getIntent().getStringExtra("ID_PET");
        getPetDados();
    }

    private void getPetDados(){
        databaseReference.child("Pet").orderByChild("pet_id").equalTo(idPet)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dSnp) {
                for (DataSnapshot objSnp : dSnp.getChildren()) {
                    PetModel p = objSnp.getValue(PetModel.class);
                    edtPetNome.setText(p.getPet_nome());
                    edtPetRaca.setText(p.getPet_raca());
                    edtPetIdade.setText(p.getPet_idade());
                    setSpnSexo(p.getPet_sexo());
                    setSpnPorte(p.getPet_porte());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSpnSexo(String sexo) {
        List<String> lsSexo = geradorListSpinnerController.getLstSexo();
        for(int i = 0; i < lsSexo.size(); i++){

            if(lsSexo.get(i).trim().equals(sexo.trim())){
                spnPetSexo.setSelection(i);
                break;
            }
        }
    }

    private void setSpnPorte(String porte) {
        List<String> lsPorte = geradorListSpinnerController.getLstPorte();
        for(int i = 0; i < lsPorte.size(); i++){

            if(lsPorte.get(i).trim().equals(porte.trim())){
                spnPetPorte.setSelection(i);
                break;
            }
        }
    }

    private void configuraNavBar() {
        setTitle("Perfil do Pet");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PerfilPetActivity.this, PetActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
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
        Intent intent = new Intent(PerfilPetActivity.this, PetActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }
}
