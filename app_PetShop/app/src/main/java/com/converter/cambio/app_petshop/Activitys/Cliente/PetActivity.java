package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.Adapter.ListaAdapterPet;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
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

public class PetActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private String idUsuario;
    private ListView lstPets;
    private List<PetModel> lstPetModel = new ArrayList<>();
    private DatabaseReference databaseReference;
    private MetodosPadraoController m = new MetodosPadraoController();
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_pet);

        configuraNavBar();
        fab = findViewById(R.id.fab_add_pet);

        getExtraIdUsuario();
        inicializarFirebase();
        lstPets = findViewById(R.id.lst_pets);
        getLstPetModel();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetActivity.this, CadastroPetActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            }
        });

        lstPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLstPetModel() {
        databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {

                        List<PetModel> lstVazia = new ArrayList<>();
                        lstPetModel = lstVazia;
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            PetModel p = objSnp.getValue(PetModel.class);
                            lstPetModel.add(p);
                        }

                        if (lstPetModel.size() <= 0) {
                            m.alertToast(PetActivity.this, "Você não possui nenhum Pet cadastrado!");
                        } else {

                            atualizaLista(lstPetModel);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void atualizaLista(final List<PetModel> listPets) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try{
                    ListaAdapterPet filaAdapter = new ListaAdapterPet(idUsuario, listPets, PetActivity.this);
                    lstPets.setAdapter(filaAdapter);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void configuraNavBar() {
        setTitle("Pets");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PetActivity.this, PaginaPrincipalActivity.class);
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
        Intent intent = new Intent(PetActivity.this, PaginaPrincipalActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(PetActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
}
