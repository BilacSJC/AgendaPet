package com.converter.cambio.app_petshop.Activitys.Empresa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.converter.cambio.app_petshop.Model.AgendamentoModel;
import com.converter.cambio.app_petshop.R;

import java.util.List;

    public class ListaAdapterSolicitacoes extends  BaseAdapter{

        private List<AgendamentoModel> lstAgendamentos;
        private Context context;

        TextView txtNome, txtDocumento, txtQuadraLote, txtEndereco, txtQualificadorUsuarioLista;

        public ListaAdapterSolicitacoes(List<AgendamentoModel> lista, Context context)
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

            AgendamentoModel agendamentoModel = (AgendamentoModel) getItem(position);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.lst_solicitacoes, null);

            txtNome = (TextView) view.findViewById(R.id.txtNomeLista);
            txtDocumento = (TextView) view.findViewById(R.id.txtDocumentoLista);
            txtQuadraLote = (TextView) view.findViewById(R.id.txtQuadraLoteLista);
            txtEndereco = (TextView) view.findViewById(R.id.txtEnderecoLista);
            txtQualificadorUsuarioLista = (TextView) view.findViewById(R.id.txtQualificadorUsuarioLista);

            setaCampos(agendamentoModel);

            return view;
        }

        private void setaCampos(AgendamentoModel usuario){

        txtQualificadorUsuarioLista.setText(usuario.getAge_status().trim());
        txtNome.setText("Nome: " + usuario.getAge_status().trim());
        txtDocumento.setText("RG.: " + usuario.getAge_status().trim());
        txtQuadraLote.setText("Quara/Lote: " + usuario.getAge_status().trim() +  "/" + usuario.getAge_status().trim());
        txtEndereco.setText("Endereço: " + usuario.getAge_status().trim());
        }
    }
