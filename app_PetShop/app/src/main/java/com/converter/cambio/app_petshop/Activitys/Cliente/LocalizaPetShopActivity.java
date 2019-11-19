package com.converter.cambio.app_petshop.Activitys.Cliente;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.PetModel;
import com.converter.cambio.app_petshop.Model.ServicoEmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocalizaPetShopActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();

    private Spinner spnServicos;
    private Spinner spnEmpresas;
    private Spinner spnNomePet;
    private EditText edtData, edtHora;
    private MaterialButton btnAgendar;
    private String empId, strData, strHora, strPorte, strPetRaca, strNomePorte;
    private String idUsuario;
    private List<EmpresaModel> lstEmpresas = new ArrayList<>();
    private List<ServicoEmpresaModel> lstServicos = new ArrayList<>();
    private List<String> lstPetPorte = new ArrayList<>();
    private List<String> lstPetRaca = new ArrayList<>();
    private List<String> lstPet = new ArrayList<>();
    private List<String> lstIdPet = new ArrayList<>();
    private List<String> lstEmpresaNome = new ArrayList<>();
    private List<String> lstServicoNome = new ArrayList<>();
    private List<String> lstEmpresaId = new ArrayList<>();

    Context context = LocalizaPetShopActivity.this;

    private Calendar calendar;
    private int ano, mes, dia, hora, minuto;

    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_map_servicos);
        getExtraIdUsuario();
        inicializarFirebase();
        inicializaComponentes();
        preencheSpinnerNomePet();
        configuraNavBar();
        preencheSpinnerEmpresas();

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnEmpresas.getSelectedItemPosition() <= 0 && spnServicos.getSelectedItemPosition() <= 0) {
                    if (spnServicos.getSelectedItemPosition() == 0) {
                        alertDialog("ATENÇÃO", "Selecione Uma empresa e um serviço.");
                    }
                } else {
                    Intent intent = new Intent(LocalizaPetShopActivity.this, AgendamentoActivity.class);
                    intent.putExtra("ID_USUARIO", idUsuario);
                    intent.putExtra("ID_EMPRESA", empId);
                    intent.putExtra("DATA", edtData.getText().toString().trim());
                    intent.putExtra("HORA", edtHora.getText().toString().trim());
                    intent.putExtra("PORTE", strPorte);
                    intent.putExtra("RACA", strPetRaca);
                    intent.putExtra("SERVICO", spnServicos.getSelectedItem().toString());
                    intent.putExtra("NOME", spnNomePet.getSelectedItem().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });

        spnEmpresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    empId = getEmpId(position);
                databaseReference.child("Servicos").orderByChild("ser_emp_id").equalTo(empId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dSnp) {
                                List<String> lstVazia = new ArrayList<>();
                                lstServicoNome = lstVazia;
                                List<ServicoEmpresaModel> lstVaziaServicos = new ArrayList<>();
                                lstServicos = lstVaziaServicos;
                                for (DataSnapshot objSnp : dSnp.getChildren()) {
                                    ServicoEmpresaModel s = objSnp.getValue(ServicoEmpresaModel.class);
                                    lstServicos.add(s);
                                }

                                if (lstServicos.size() <= 0) {
                                    lstServicoNome.add("Nenhum serviço cadastrado");
                                } else {
                                    lstServicoNome.add("Selecione um serviço");
                                }

                                for (int i = 0; i < lstServicos.size(); i++) {
                                    lstServicoNome.add(lstServicos.get(i).getSer_nome() + " - " + lstServicos.get(i).getSer_preco());
                                }
                                ArrayAdapter<String> servicos = new ArrayAdapter<>(LocalizaPetShopActivity.this, R.layout.support_simple_spinner_dropdown_item, lstServicoNome);
                                spnServicos.setAdapter(servicos);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventosClick();
    }

    private void configuraNavBar() {
        setTitle("Agendamento");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LocalizaPetShopActivity.this, PaginaPrincipalActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_EMPRESA", empId);
                intent.putExtra("SERVICO", spnServicos.getSelectedItem().toString());
                startActivity(intent);
                // finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LocalizaPetShopActivity.this, PaginaPrincipalActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    private String getEmpId(int position) {
        for (int i = 0; i < lstEmpresaNome.size(); i++) {
            if (position == i && position > 0) {
                return lstEmpresaId.get(i - 1);
            }
        }
        return "";
    }

    private void inicializaComponentes() {
        edtData = findViewById(R.id.alt_age_cli_edt_data);
        edtHora = findViewById(R.id.alt_age_cli_edt_hora);
        btnAgendar = findViewById(R.id.map_btn_agendar);
        spnEmpresas = findViewById(R.id.map_spnEmpresas);
        spnServicos = findViewById(R.id.map_spnServicos);
        spnNomePet = findViewById(R.id.alt_age_cli_spn_pet);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(LocalizaPetShopActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void preencheSpinnerEmpresas() {

        databaseReference.child("Empresa").orderByChild("emp_id")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);


//                            Calendar cal = Calendar.getInstance();
//                            try {
//                                cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(edtData.getText().toString()));
//                            } catch (ParseException ex) {
//
//                            }
//                            if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
//                                e.getEmp_hor_sem_ini();
//                                e.getEmp_hor_sem_fim();
//
//                                int hSemIni = Integer.valueOf(e.getEmp_hor_sem_ini().replace(":",""));
//                                int hSemFim = Integer.valueOf(e.getEmp_hor_sem_fim().replace(":",""));
//                                int hDigitado = Integer.valueOf(edtHora.getText().toString());
//                                if( hDigitado >= hSemIni || hDigitado <= hSemFim) {
//                                    lstEmpresas.add(e);
//                                    lstEmpresaId.add(e.getEmp_id());
//                                }
//                            }else{
//                                if(!e.getEmp_hor_fds_ini().equals("")){
//                                    int hSemIni = Integer.valueOf(e.getEmp_hor_fds_ini().replace(":",""));
//                                    int hSemFim = Integer.valueOf(e.getEmp_hor_fds_fim().replace(":",""));
//                                    int hDigitado = Integer.valueOf(edtHora.getText().toString());
//                                    if( hDigitado >= hSemIni || hDigitado <= hSemFim) {
                                        lstEmpresas.add(e);
                                        lstEmpresaId.add(e.getEmp_id());



                        }

                        if (lstEmpresas.size() <= 0) {
                            lstEmpresaNome.add("Nenhuma empresa cadastrada");
                        } else {
                            lstEmpresaNome.add("Selecione um PetShop");
                        }

                        for (int i = 0; i < lstEmpresas.size(); i++) {
                            lstEmpresaNome.add(lstEmpresas.get(i).getEmp_nome());
                        }
                        ArrayAdapter<String> empresas = new ArrayAdapter<>(LocalizaPetShopActivity.this, R.layout.support_simple_spinner_dropdown_item, lstEmpresaNome);
                        spnEmpresas.setAdapter(empresas);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void preencheSpinnerNomePet() {
        Query query;
        query = databaseReference.child("Pet").orderByChild("pet_cli_id").equalTo(idUsuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstPet.clear();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    PetModel p = objSnapshot.getValue(PetModel.class);
                    lstPet.add(p.getPet_nome());
                    lstIdPet.add(p.getPet_id());
                    strPorte = p.getPet_porte();
                    strPetRaca = p.getPet_raca();
                }
                ArrayAdapter<String> arrayAdapterPet = new ArrayAdapter<>(LocalizaPetShopActivity.this, R.layout.support_simple_spinner_dropdown_item, lstPet);
                spnNomePet.setAdapter(arrayAdapterPet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void eventosClick() {
        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                DatePickerDialog dpdData = new DatePickerDialog(LocalizaPetShopActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        if (intTime >= 0 && intTime < 10) {
            strTime = "0" + intTime;
        } else {
            strTime = String.valueOf(intTime);
        }
        return strTime;
    }

}
