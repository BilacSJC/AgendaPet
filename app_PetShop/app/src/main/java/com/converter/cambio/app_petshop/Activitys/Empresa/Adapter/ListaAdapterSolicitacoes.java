package com.converter.cambio.app_petshop.Activitys.Empresa.Adapter;

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

import com.converter.cambio.app_petshop.Activitys.Cliente.AlterarAgendamentoActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.R;
import com.converter.cambio.app_petshop.ViewModel.AgendamentoViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

    public class ListaAdapterSolicitacoes extends  BaseAdapter{

        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;
        private FireBaseQuery fireBaseQuery = new FireBaseQuery();

        private List<AgendamentoViewModel> lstAgendamentos;
        private String idUsuario, empresaNome, servicoPreco, servicoNome, cliNome, cliTel,  petNome, petRaca, petPorte, idPet, idCliente;
        private Context context;
        private Button btn_confirmar, btn_remarcar;
        private MetodosPadraoController m = new MetodosPadraoController();
        private TextView lst_sol_txtNome, lst_sol_txtTelefone ,lst_sol_txt_nome_pet ,lst_sol_txt_porte_pet;
        private TextView lst_sol_txt_raca_pet, lst_sol_txt_data_age, lst_sol_txt_hora_age,  lst_sol_txtStatus;
        public ListaAdapterSolicitacoes(String idUsuario, List<AgendamentoViewModel> lista, Context context)
        {
            this.idUsuario = idUsuario;
            this.lstAgendamentos = lista;
            this.context = context;

            FirebaseApp.initializeApp(context);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

        }

        public List<AgendamentoViewModel> getLista(){
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            AgendamentoViewModel usuarioModelLista = (AgendamentoViewModel) getItem(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.lst_solicitacoes, null);

            lst_sol_txtNome = (TextView) view.findViewById(R.id.lst_sol_txt_cli_nome);
            lst_sol_txtTelefone = (TextView) view.findViewById(R.id.lst_sol_txt_telefone);
            lst_sol_txt_nome_pet = (TextView) view.findViewById(R.id.lst_sol_txt_nome_pet);
            lst_sol_txt_porte_pet = (TextView) view.findViewById(R.id.lst_sol_txt_porte_pet);
            lst_sol_txt_raca_pet = (TextView) view.findViewById(R.id.lst_sol_txt_raca_pet);
            lst_sol_txt_data_age = (TextView) view.findViewById(R.id.lst_sol_txt_data_age);
            lst_sol_txt_hora_age = (TextView) view.findViewById(R.id.lst_sol_txt_hora_age);
            lst_sol_txtStatus = (TextView) view.findViewById(R.id.lst_sol_txt_status);
            btn_confirmar = view.findViewById(R.id.lst_sol_btn_remarcar);
            btn_remarcar = view.findViewById(R.id.lst_sol_btn_confirmar);

            btn_confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m.alertToast(context,String.valueOf(position));
                    setStatusAgendamento("Confirmado", position);

                }
            });

            btn_remarcar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m.alertToast(context,String.valueOf(position));
                    setStatusAgendamento("Remarcar", position);
                }
            });

            setaCampos(usuarioModelLista);

            return view;
        }

        private void setStatusAgendamento(final String strStatus, final int position) {
            String strIdAge = lstAgendamentos.get(position).getAge_id();

            databaseReference.child("Agendamento").orderByChild("age_id").equalTo(strIdAge)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dSnp) {
                            for (DataSnapshot objSnp : dSnp.getChildren()) {
                                AgendamentoViewModel a = objSnp.getValue(AgendamentoViewModel.class);

                                a.setAlt_age_status(strStatus);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        }


        private void setaCampos(AgendamentoViewModel agendamentoModel){

                  lst_sol_txtNome.setText("Nome: " + agendamentoModel.getAlt_age_cli_nome());
              lst_sol_txtTelefone.setText("Telefone: " + agendamentoModel.getAlt_age_cli_telefone());
             lst_sol_txt_nome_pet.setText("Pet: " + agendamentoModel.getAlt_age_pet_nome());
            lst_sol_txt_porte_pet.setText("Porte: " + agendamentoModel.getAlt_age_pet_porte());
             lst_sol_txt_raca_pet.setText("Raça: " + agendamentoModel.getAlt_age_pet_raca());
             lst_sol_txt_data_age.setText("Data: " + agendamentoModel.getAlt_age_data());
             lst_sol_txt_hora_age.setText("Hora: " + agendamentoModel.getAlt_age_hora());
                lst_sol_txtStatus.setText("Status: " + agendamentoModel.getAlt_age_status());
        }
    }
