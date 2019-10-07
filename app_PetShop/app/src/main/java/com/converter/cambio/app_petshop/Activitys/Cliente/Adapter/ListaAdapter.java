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
    TextView txtNome, txtDocumento, txtQuadraLote, txtEndereco, txtQualificadorUsuarioLista;

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

        txtNome = (TextView) view.findViewById(R.id.txtNomeLista);
        txtDocumento = (TextView) view.findViewById(R.id.txtDocumentoLista);
        txtQuadraLote = (TextView) view.findViewById(R.id.txtQuadraLoteLista);
        txtEndereco = (TextView) view.findViewById(R.id.txtEnderecoLista);
        txtQualificadorUsuarioLista = (TextView) view.findViewById(R.id.txtQualificadorUsuarioLista);

        setaCampos(usuarioModelLista);

        return view;
    }

    private void setaCampos(AgendamentoModel usuario){

//        txtQualificadorUsuarioLista.setText(usuario.getStrQualificador1().trim());
//        txtNome.setText("Nome: " + usuario.getStrNome().trim());
//        txtDocumento.setText("RG.: " + usuario.getStrDocumento().trim());
//        txtQuadraLote.setText("Quara/Lote: " + usuario.getStrQuadra().trim() +  "/" + usuario.getStrLote().trim());
//        txtEndereco.setText("Endereço: " + usuario.getStrMatricula().trim());
    }
}
