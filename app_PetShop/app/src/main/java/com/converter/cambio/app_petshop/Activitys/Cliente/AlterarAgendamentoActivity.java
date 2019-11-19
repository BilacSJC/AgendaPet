package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.EnderecoModel;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.R;
import com.converter.cambio.app_petshop.ViewModel.AgendamentoViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AlterarAgendamentoActivity extends AppCompatActivity {

    private MaterialButton btnAlterar, btnLimpar;
    private EditText edtData, edtHora;
    private TextView txtNomeEmpresa, txtEnderecoEmpresa, txtServico, txtStatus, txtNomeCliente, txtTelefoneCliente;
    private Spinner spnNomePet, spnPet;
    private List<String> lstPet = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterPet;
    private String idAgendamento, idUsuario, idEmpresa, strPetNome, strPetRaca, strPetPorte, strEmpNome, strEmpEnd, strCliNome, strCliTelefone, strServico, strData, strHora, strStatus;

    private Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private MetodosPadraoController m = new MetodosPadraoController();

    private Calendar calendar;
    private int ano, mes, dia, hora, minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_alterar_agendamento);
        inicializarFirebase();
        getExtraIdUsuario();
        inicializaCampos();
        configuraNavBar();
        eventosClick();
        setCampos();

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void eventosClick() {
        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtData.setText("");
                edtHora.setText("");
                spnNomePet.setSelection(0);
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgendamentoViewModel agendamentoModel = validaCampos();

                if (agendamentoModel.getAge_id() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                cadastrarAgendamento(agendamentoModel);

                Intent intent = new Intent(AlterarAgendamentoActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            }
        });

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                DatePickerDialog dpdData = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String strDia = "";
                        int intMes = month + 1;
                        String strMes = "";
                        String strAno = String.valueOf(year);
                        strDia = intTimeToStr(day);
                        strMes = intTimeToStr(intMes);
                        String strData = strDia + "/" + strMes + "/" + strAno;
                        edtData.setText(strData);
                    }
                }, ano, mes, dia);
                dpdData.show();
            }
        });

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog tpdHora = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String strHora = "";
                        String strMinutes = "";
                        strHora = intTimeToStr(hourOfDay);
                        strMinutes = intTimeToStr(minutes);
                        String strTempo = strHora + ":" + strMinutes;
                        edtHora.setText(strTempo);
                    }
                }, hora, minuto, true);

                tpdHora.show();
            }
        });
    }

    public void horarioAtual() {
        calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);
    }

    private String intTimeToStr(int intTime) {
        String strTime;
        if(intTime >= 0 && intTime < 10){
            strTime = "0"+intTime;
        }else{
            strTime = String.valueOf(intTime);
        }
        return strTime;
    }

    private void cadastrarAgendamento(AgendamentoViewModel agendamentoModel) {
        fireBaseQuery.UpdateObjetcDb(agendamentoModel, "Agendamento", agendamentoModel.getAge_id(), databaseReference);
    }

    private AgendamentoViewModel validaCampos() {
        AgendamentoViewModel a = new AgendamentoViewModel();
        ValidaCampos v = new ValidaCampos();

        String strMensagemData = v.vStringData(edtData.getText().toString().trim());
        String strMensagemHora = v.vStringHora(edtHora.getText().toString().trim());

        int contMsg = 0;

        if (!strMensagemData.equals("ok")) {
            edtData.setError(strMensagemData);
            contMsg += 1;
        }
        if (!strMensagemHora.equals("ok")) {
            edtHora.setError(strMensagemHora);
            contMsg += 1;
        }

        if (contMsg > 0) {
            return new AgendamentoViewModel();
        }

        strPetNome = spnNomePet.getSelectedItem().toString().trim();
        getPetPorte();

        a.setAge_id(idAgendamento);
        a.setAge_cli_id(idUsuario);
        a.setAge_emp_id(idEmpresa);
        a.setAlt_age_cli_nome(strCliNome);
        a.setAlt_age_cli_telefone(strCliTelefone);
        a.setAlt_age_pet_nome(strPetNome);
        a.setAlt_age_pet_raca(strPetRaca);
        a.setAlt_age_pet_porte(strPetPorte);
        a.setAlt_age_emp_nome(strEmpNome);
        a.setAlt_age_emp_endereco(strEmpEnd);
        a.setAlt_age_data(edtData.getText().toString().trim());
        a.setAlt_age_hora(edtHora.getText().toString().trim());
        a.setAlt_age_servico(strServico);
        a.setAlt_age_status("Aguardando Confirmação");

        return a;
    }

    private String getPetPorte() {
        Query query;
        query = databaseReference.child("Pet").orderByChild("pet_cli_nome").equalTo(strPetNome);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    PetModel p = objSnapshot.getValue(PetModel.class);
                    strPetPorte = p.getPet_porte();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return strPetPorte;
    }

    private void configuraNavBar() {
        setTitle("Alterar Agendamento");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AlterarAgendamentoActivity.this, PaginaPrincipalActivity.class);
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
        Intent intent = new Intent(AlterarAgendamentoActivity.this, PaginaPrincipalActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
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

    private void setCampos() {

        Query query;
        query = databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstPet.clear();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    PetModel p = objSnapshot.getValue(PetModel.class);
                    lstPet.add(p.getPet_nome());
                }
                arrayAdapterPet = new ArrayAdapter<>(AlterarAgendamentoActivity.this, R.layout.support_simple_spinner_dropdown_item, lstPet);
                spnNomePet.setAdapter(arrayAdapterPet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Agendamento").orderByChild("age_id").equalTo(idAgendamento)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            AgendamentoViewModel age = objSnapshot.getValue(AgendamentoViewModel.class);
                            txtNomeCliente.setText("Nome: " + age.getAlt_age_cli_nome());
                            txtTelefoneCliente.setText("Telefone: " + age.getAlt_age_cli_telefone());
                            txtEnderecoEmpresa.setText("Endereço: " + age.getAlt_age_emp_endereco());
                            txtNomeEmpresa.setText("Empresa: " + age.getAlt_age_emp_nome());
                            txtServico.setText("Serviço: " + age.getAlt_age_servico());
                            txtStatus.setText("Status: " + age.getAlt_age_status());
                            edtData.setText(age.getAlt_age_data());
                            edtHora.setText(age.getAlt_age_hora());

                            for (int i = 0; i < lstPet.size(); i++) {
                                if (lstPet.get(i).equals(age.getAlt_age_pet_nome())) {
                                    spnNomePet.setSelection(i);
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void inicializaCampos() {
        btnAlterar = findViewById(R.id.alt_age_btn_alterar);
        btnLimpar = findViewById(R.id.alt_age_btn_limpar);

        spnNomePet = findViewById(R.id.alt_age_cli_spn_pet);

        edtData = findViewById(R.id.alt_age_cli_edt_data);
        edtHora = findViewById(R.id.alt_age_cli_edt_hora);

        txtNomeEmpresa = findViewById(R.id.alt_age_cli_txt_nome_empresa);
        txtEnderecoEmpresa = findViewById(R.id.alt_age_cli_txt_endereco_empresa);
        txtServico = findViewById(R.id.alt_age_cli_txt_servico);
        txtStatus = findViewById(R.id.alt_age_cli_txt_status);
        txtNomeCliente = findViewById(R.id.alt_age_cli_txt_nome_cliente);
        txtTelefoneCliente = findViewById(R.id.alt_age_cli_txt_telefone_cliente);

        context = AlterarAgendamentoActivity.this;
        txtServico.setText("Serviço: " + strServico);
        getEmpresaNome();
        getCliDados();
        getEmpresaEndereco();
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        idEmpresa = getIntent().getStringExtra("ID_EMPRESA");
        strServico = getIntent().getStringExtra("SERVICO");
        idAgendamento = getIntent().getStringExtra("ID_AGENDAMENTO");
    }

    private void getCliDados() {
        databaseReference.child("Cliente").orderByChild("cli_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            ClienteModel c = objSnp.getValue(ClienteModel.class);
                            strCliNome = c.getCli_nome().trim();
                            strCliTelefone = c.getCli_telefone();
                            txtNomeCliente.setText("Nome: " + strCliNome);
                            txtTelefoneCliente.setText("Telefone: " + strCliTelefone);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void getEmpresaNome() {
        databaseReference.child("Empresa").orderByChild("emp_id").equalTo(idEmpresa)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);
                            strEmpNome = e.getEmp_nome().trim();
                            txtNomeEmpresa.setText("Empresa: " + strEmpNome);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void getEmpresaEndereco() {
        databaseReference.child("Endereco").orderByChild("id_usuario").equalTo(idEmpresa)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EnderecoModel e = objSnp.getValue(EnderecoModel.class);
                            strEmpEnd = e.getBairro() + " " + e.getNumero() + " " + e.getCidade() + "-" + e.getEstado();
                            txtEnderecoEmpresa.setText("Endereço da empresa: " + strEmpEnd);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}

