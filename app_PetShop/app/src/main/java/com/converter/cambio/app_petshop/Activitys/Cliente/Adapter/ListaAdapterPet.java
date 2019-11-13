package com.converter.cambio.app_petshop.Activitys.Cliente.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Activitys.Cliente.PerfilPetActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListaAdapterPet extends BaseAdapter {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private List<PetModel> lstPets;
    private String idUsuario;
    private Context context;

    private MetodosPadraoController m = new MetodosPadraoController();
    String idPet;

    TextView txt_pet_nome, txt_pet_raca, txt_pet_idade, txt_pet_sexo, txt_pet_porte;

    Button pet_btn_excluir, pet_btn_editar;

    public ListaAdapterPet(String idUsuario, List<PetModel> lista, Context context) {
        this.idUsuario = idUsuario;
        this.lstPets = lista;
        this.context = context;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public List<PetModel> getLista() {
        return lstPets;
    }

    @Override
    public int getCount() {
        return lstPets.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PetModel usuarioModelLista = (PetModel) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lst_pet, parent, false);

        txt_pet_nome = view.findViewById(R.id.lst_pet_txt_nome);
        txt_pet_raca = view.findViewById(R.id.lst_pet_txt_raca);
        txt_pet_idade = view.findViewById(R.id.lst_pet_txt_idade);
        txt_pet_sexo = view.findViewById(R.id.lst_pet_txt_sexo);
        txt_pet_porte = view.findViewById(R.id.lst_pet_txt_porte);

        pet_btn_excluir = view.findViewById(R.id.lst_pet_btn_excluir);
        pet_btn_editar = view.findViewById(R.id.lst_pet_btn_editar);

        pet_btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogButtonLst("ATENÇÃO!", "Tem certeza que deseja excluir este Pet?", position);
            }
        });

        pet_btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PerfilPetActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_PET", lstPets.get(position).getPet_id());
                context.startActivity(intent);
            }
        });

        setaCampos(usuarioModelLista);
        return view;
    }

    private void alertDialogButtonLst(String strTitle, String strMsg, final int position) {
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m.alertToast(context, "Nenhuma ação foi realizada.");
                    }
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String strIdPet = lstPets.get(position).getPet_id();

                        databaseReference.child("Pet").orderByChild("pet_id").equalTo(strIdPet)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dSnp) {
                                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                                            PetModel p = objSnp.getValue(PetModel.class);
                                            excluirPet(p);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                    }
                }).show();
    }

    private void excluirPet(PetModel p) {
        fireBaseQuery.DeleteObjectDb(p, "Pet", p.getPet_id(), databaseReference);
        m.alertToast(context, "Pet excluído com sucesso!");
    }

    private void setaCampos(PetModel petModel) {
        txt_pet_nome.setText(petModel.getPet_nome());
        txt_pet_raca.setText(petModel.getPet_raca());
        txt_pet_idade.setText(petModel.getPet_idade());
        txt_pet_sexo.setText(petModel.getPet_sexo());
        txt_pet_porte.setText(petModel.getPet_porte());

        idPet = petModel.getPet_id();
    }
}
