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
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.AlterarAgendamentoActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.PaginaPrincipalActivity;
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

public class ListaAdapter extends BaseAdapter {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private List<AgendamentoViewModel> lstAgendamentos;
    private String idUsuario;
    private Context context;

    private MetodosPadraoController m = new MetodosPadraoController();
    String idAgendamento;

    TextView txt_age_emp_nome, txt_age_ser_preco, txt_age_ser_nome_pet, txt_age_data, txt_age_hora, txt_age_status;

    Button age_btn_editar, age_btn_cancelar;

    public ListaAdapter(String idUsuario, List<AgendamentoViewModel> lista, Context context) {
        this.idUsuario = idUsuario;
        this.lstAgendamentos = lista;
        this.context = context;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public List<AgendamentoViewModel> getLista() {
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
        View view = inflater.inflate(R.layout.lst_agendamento, parent, false);

        txt_age_emp_nome = (TextView) view.findViewById(R.id.lst_age_txt_emp_nome);
        txt_age_ser_preco = (TextView) view.findViewById(R.id.lst_age_txt_ser_preco);
        txt_age_ser_nome_pet = (TextView) view.findViewById(R.id.lst_age_txt_nome_pet);
        txt_age_data = (TextView) view.findViewById(R.id.lst_age_txt_data);
        txt_age_hora = (TextView) view.findViewById(R.id.lst_age_txt_hora);
        txt_age_status = (TextView) view.findViewById(R.id.lst_age_txt_status);

        age_btn_editar = view.findViewById(R.id.lst_age_btn_editar);
        age_btn_cancelar = view.findViewById(R.id.lst_age_btn_cancelar);

        age_btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.alertToast(context,String.valueOf(position));
                alertDialogButtonLst("ATENÇÃO!", "Tem certeza que deseja cancelar o agendamento?", position);
            }
        });

        age_btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlterarAgendamentoActivity.class);
                intent.putExtra("ID_AGENDAMENTO", lstAgendamentos.get(position).getAge_id());
            }
        });

        setaCampos(usuarioModelLista);

        return view;
    }

    private void setaCampos(AgendamentoViewModel agendamentoModel) {

        txt_age_ser_preco.setText("Serviço: " + agendamentoModel.getAlt_age_servico());
        txt_age_ser_nome_pet.setText("Pet: " + agendamentoModel.getAlt_age_pet_nome());
        txt_age_data.setText("Data: " + agendamentoModel.getAlt_age_data().trim());
        txt_age_hora.setText("Hora: " + agendamentoModel.getAlt_age_hora().trim());
        txt_age_status.setText("Status: " + agendamentoModel.getAlt_age_status().trim());
        txt_age_emp_nome.setText("Empresa: " + agendamentoModel.getAlt_age_emp_nome());

        idAgendamento = agendamentoModel.getAge_id();
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

                        String strIdAge = lstAgendamentos.get(position).getAge_id();

                        databaseReference.child("Agendamento").orderByChild("age_id").equalTo(strIdAge)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dSnp) {
                                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                                            AgendamentoViewModel a = objSnp.getValue(AgendamentoViewModel.class);

                                            a.setAlt_age_status("Cancelado");
                                            atualizarAgendamento(a);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                    }
                }).show();
    }

    private void atualizarAgendamento(AgendamentoViewModel a) {
        fireBaseQuery.UpdateObjetcDb(a,"Agendamento", a.getAge_id(), databaseReference);
        m.alertToast(context, "Agendameto cancelado com sucesso!.");
    }

}
