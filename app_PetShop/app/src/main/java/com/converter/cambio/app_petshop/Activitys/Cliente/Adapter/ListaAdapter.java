package com.converter.cambio.app_petshop.Activitys.Cliente.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.R;

import java.util.List;

public class ListaAdapter extends BaseAdapter {

    private List<AgendamentoModel> lstAgendamentos;
    private Context context;

    ImageView imgFotoLista;
    TextView txt_age_emp_nome, txt_age_ser_preco, txt_age_ser_nome_pet, txt_age_data, txt_age_hora, txt_age_status;

    public ListaAdapter(List<AgendamentoModel> lista, Context context)
    {
        this.lstAgendamentos = lista;
        this.context = context;
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

        setaCampos(usuarioModelLista);

        return view;
    }

    private void setaCampos(AgendamentoModel agendamentoModel){

        txt_age_emp_nome.setText("Empresa: " + agendamentoModel.getAge_empresa_id().trim());
        txt_age_ser_preco.setText("Serviço: " + agendamentoModel.getAge_empresa_id().trim());
        txt_age_ser_nome_pet.setText("Pet: " + agendamentoModel.getAge_pet_id().trim());
        txt_age_data.setText("Data: " + agendamentoModel.getAge_data().trim());
        txt_age_hora.setText("Hora: " + agendamentoModel.getAge_hora().trim());
        txt_age_status.setText("Status: " + agendamentoModel.getAge_status().trim());
    }
}
