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

import com.converter.cambio.app_petshop.Activitys.Empresa.AlterarServicoActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListaAdapterServicos extends BaseAdapter {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private List<ServicoEmpresaModel> lstServicos;
    private MetodosPadraoController m = new MetodosPadraoController();
    private Context context;

    private String idUsuario, idServico;

    private TextView txt_ser_nome_servico, txt_ser_preco;
    private Button ser_btn_excluir, serv_btn_editar;

    public ListaAdapterServicos(String idUsuario, List<ServicoEmpresaModel> lista, Context context)
    {
        this.idUsuario = idUsuario;
        this.lstServicos = lista;
        this.context = context;

        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public List<ServicoEmpresaModel> getLista(){
        return lstServicos;
    }

    @Override
    public int getCount() {
        return lstServicos.size();
    }

    @Override
    public Object getItem(int position) {
        return lstServicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ServicoEmpresaModel usuarioModelLista = (ServicoEmpresaModel) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lst_servicos, null);

        txt_ser_nome_servico = view.findViewById(R.id.lst_ser_txt_nome_servico);
        txt_ser_preco = view.findViewById(R.id.lst_ser_txt_preco);

        ser_btn_excluir = view.findViewById(R.id.lst_ser_btn_excluir);
        serv_btn_editar = view.findViewById(R.id.lst_ser_btn_editar);

        ser_btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogButtonLst("ATENÇÃO!", "Tem certeza que deseja excluir este serviço?", position);
            }
        });

        serv_btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlterarServicoActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_SERVICO", lstServicos.get(position).getSer_id());
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

                        String strIdServico = lstServicos.get(position).getSer_id();

                        databaseReference.child("Servicos").orderByChild("ser_id").equalTo(strIdServico)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dSnp) {
                                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                                            ServicoEmpresaModel s = objSnp.getValue(ServicoEmpresaModel.class);
                                            excluirServico(s);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                    }
                }).show();
    }

    private void excluirServico(ServicoEmpresaModel s) {
        fireBaseQuery.DeleteObjectDb(s, "Servicos", s.getSer_id(), databaseReference);
        m.alertToast(context, "Serviço excluído com sucesso!");
    }

    private void setaCampos(ServicoEmpresaModel servicoEmpresaModel) {
        txt_ser_nome_servico.setText(servicoEmpresaModel.getSer_nome());
        txt_ser_preco.setText(servicoEmpresaModel.getSer_preco());

        idServico = servicoEmpresaModel.getSer_id();
    }

}
