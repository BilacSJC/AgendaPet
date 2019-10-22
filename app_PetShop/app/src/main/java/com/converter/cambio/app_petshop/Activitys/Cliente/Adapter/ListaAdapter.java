package com.converter.cambio.app_petshop.Activitys.Cliente.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Activitys.Cliente.LocalizaPetShopActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListaAdapter extends BaseAdapter {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private List<AgendamentoModel> lstAgendamentos;
    private String idUsuario, idEmp, idPet, empresaNome, servicoPreco, servicoNome, petNome;
    private Context context;

    ImageView imgFotoLista;
    TextView txt_age_emp_nome, txt_age_ser_preco, txt_age_ser_nome_pet, txt_age_data, txt_age_hora, txt_age_status;

    public ListaAdapter(String idUsuario, String idEmp, String idPet, List<AgendamentoModel> lista, Context context)
    {
        this.idUsuario = idUsuario;
        this.lstAgendamentos = lista;
        this.context = context;
        this.idEmp = idEmp;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public List<AgendamentoModel> getLista(){
        return lstAgendamentos;
    }

    @Override
    public int getCount() {
        return lstAgendamentos.size();
    }

    @Override
    public Object getItem(int position) {
        return lstAgendamentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AgendamentoModel usuarioModelLista = (AgendamentoModel) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lst_agendamento, null);

        txt_age_emp_nome = (TextView) view.findViewById(R.id.txt_age_emp_nome);
        txt_age_ser_preco = (TextView) view.findViewById(R.id.txt_age_ser_preco);
        txt_age_ser_nome_pet = (TextView) view.findViewById(R.id.txt_age_ser_nome_pet);
        txt_age_data = (TextView) view.findViewById(R.id.txt_age_data);
        txt_age_hora = (TextView) view.findViewById(R.id.txt_age_hora);
        txt_age_status = (TextView) view.findViewById(R.id.txt_age_status);

        getServicoPreco();
        getEmpresaNome();
        getPetNome();

        setaCampos(usuarioModelLista);

        return view;
    }

    private void setaCampos(AgendamentoModel agendamentoModel){
        txt_age_data.setText("Data: " + agendamentoModel.getAge_data_solicitada().trim());
        txt_age_hora.setText("Hora: " + agendamentoModel.getAge_hora_solicitada().trim());
        txt_age_status.setText("Status: " + agendamentoModel.getAge_status().trim());
    }

    private void getPetNome() {
        databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            PetModel p = objSnp.getValue(PetModel.class);
                            petNome = p.getPet_nome().trim();
                            txt_age_ser_nome_pet.setText("Pet: " + petNome);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getServicoPreco() {
        databaseReference.child("Servicos").orderByChild("ser_emp_id").equalTo(idEmp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ServicoEmpresaModel p = objSnp.getValue(ServicoEmpresaModel.class);
                            servicoPreco = p.getSer_preco().trim();
                            servicoNome = p.getSer_nome().trim();
                            txt_age_ser_preco.setText("Serviço: " + servicoNome + " " + servicoPreco);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getEmpresaNome() {
        databaseReference.child("Empresa").orderByChild("emp_id").equalTo(idEmp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EmpresaModel p = objSnp.getValue(EmpresaModel.class);
                            empresaNome = p.getEmp_nome().trim();
                            txt_age_emp_nome.setText("Empresa: " + empresaNome);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}
