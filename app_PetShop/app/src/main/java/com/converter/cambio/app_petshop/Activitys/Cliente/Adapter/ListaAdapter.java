package com.converter.cambio.app_petshop.Activitys.Cliente.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.AlterarAgendamentoActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.HistoricoAgendamentosActivity;
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
    private Boolean isHistorico;

    private MetodosPadraoController m = new MetodosPadraoController();
    String idAgendamento;

    TextView txt_age_emp_nome, txt_age_ser_preco, txt_age_ser_nome_pet, txt_age_data, txt_age_hora, txt_age_status, txt_age_bkg_titulo;

    Button age_btn_editar, age_btn_cancelar;

    public ListaAdapter(String idUsuario, List<AgendamentoViewModel> lista, Context context, Boolean isHistorico) {
        this.idUsuario = idUsuario;
        this.lstAgendamentos = lista;
        this.context = context;
        this.isHistorico = isHistorico;

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
        txt_age_bkg_titulo = view.findViewById(R.id.lst_age_txt_titulo_agendamento);

        age_btn_editar = view.findViewById(R.id.lst_age_btn_editar);
        age_btn_cancelar = view.findViewById(R.id.lst_age_btn_cancelar);

        if (isHistorico) {
            age_btn_cancelar.setVisibility(View.GONE);
            age_btn_editar.setVisibility(View.GONE);
        }

        age_btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                m.alertToast(context,String.valueOf(position));
                alertDialogButtonLst("ATENÇÃO!", "Tem certeza que deseja cancelar o agendamento?", position);
            }
        });

        age_btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlterarAgendamentoActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_AGENDAMENTO", lstAgendamentos.get(position).getAge_id());
                intent.putExtra("ID_EMPRESA", lstAgendamentos.get(position).getAge_emp_id());
                intent.putExtra("SERVICO", lstAgendamentos.get(position).getAlt_age_servico());
                context.startActivity(intent);
            }
        });

        setaCampos(usuarioModelLista);

        return view;
    }

    private void setaCampos(AgendamentoViewModel agendamentoModel) {

        txt_age_ser_preco.setText(agendamentoModel.getAlt_age_servico());
        txt_age_ser_nome_pet.setText(agendamentoModel.getAlt_age_pet_nome());
        txt_age_data.setText(agendamentoModel.getAlt_age_data().trim());
        txt_age_hora.setText(agendamentoModel.getAlt_age_hora().trim());
        txt_age_status.setText(agendamentoModel.getAlt_age_status().trim());
        txt_age_emp_nome.setText(agendamentoModel.getAlt_age_emp_nome());

        idAgendamento = agendamentoModel.getAge_id();

//        if (txt_age_status.equals("Confirmado")) {
//            // int intCor = Color.parseColor("#FFA500");
//            txt_age_bkg_titulo.setBackgroundResource(R.color.bkgConfirmar);
//        } else if (txt_age_status.equals("Cancelado")) {
//            txt_age_bkg_titulo.setBackgroundResource(R.color.bkgCancelar);
//        } else if (txt_age_status.equals("Remarcar")) {
//            txt_age_bkg_titulo.setBackgroundColor(0xFFA500);
//        } else if (txt_age_status.equals("Aguardando Confirmação")){
//            txt_age_bkg_titulo.setBackgroundColor(0x0000CD);
//        }

//        txt_age_bkg_titulo.setBackgroundColor(0x00ffffff);

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
        fireBaseQuery.UpdateObjetcDb(a, "Agendamento", a.getAge_id(), databaseReference);
        m.alertToast(context, "Agendameto cancelado com sucesso!");
    }

}
