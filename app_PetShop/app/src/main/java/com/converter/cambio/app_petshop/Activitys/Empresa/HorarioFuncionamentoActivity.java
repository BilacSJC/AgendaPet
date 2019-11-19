package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HorarioFuncionamentoActivity extends AppCompatActivity {

    private String idUsuario;

    private EditText edtHorarioSemanaInicio, edtHorarioSemanaFim, edtHorarioFdsInicio, edtHorarioFdsFim;
    private CheckBox chkFdsFechado;
    private MaterialButton btnConfirmar;

    private Calendar calendar;
    private int hora, minuto;
    String amPm;
    private TimePickerDialog tpdHorarioSemanaInicio, tpdHorarioSemanaFim, tpdHorarioFdsInicio, tpdHorarioFdsFim;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private FireBaseQuery fireBaseQuery = new FireBaseQuery();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Context context;

    private LinearLayout linHorarioFdsFeriados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_horario_funcionamento);

        context = HorarioFuncionamentoActivity.this;
        getExtraIdUsuario();
        inicializarComponentes();
        inicializarFirebase();
        configuraNavBar();
        setaCampos();
        eventosClick();

        linHorarioFdsFeriados.setVisibility(View.GONE);
    }

    private void setaCampos() {
        databaseReference.child("Empresa").orderByChild("emp_id").equalTo(idUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnp) {
                        for (DataSnapshot objSnp : dSnp.getChildren()) {
                            EmpresaModel e = objSnp.getValue(EmpresaModel.class);
                            assert e != null;
                            edtHorarioSemanaInicio.setText(e.getEmp_hor_sem_ini());
                            edtHorarioSemanaFim.setText(e.getEmp_hor_sem_fim());
                            if(e.getEmp_hor_fds_ini() != null) {
                                chkFdsFechado.setChecked(false);
                                edtHorarioFdsInicio.setText(e.getEmp_hor_fds_ini());
                                edtHorarioFdsFim.setText(e.getEmp_hor_fds_fim());
                            }else {
                                chkFdsFechado.setChecked(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void getExtraIdUsuario() {
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
    }

    private void inicializarComponentes() {
        edtHorarioSemanaInicio = findViewById(R.id.hor_fun_edt_semana_inicio);
        edtHorarioSemanaFim = findViewById(R.id.hor_fun_edt_semana_fim);
        edtHorarioFdsInicio = findViewById(R.id.hor_fun_edt_fds_feriados_inicio);
        edtHorarioFdsFim = findViewById(R.id.hor_fun_edt_fds_feriados_fim);

        chkFdsFechado = findViewById(R.id.hor_fun_chk_fds_feriados_fechado);
        linHorarioFdsFeriados = findViewById(R.id.hor_fun_lin_fds_feriados);

        btnConfirmar = findViewById(R.id.hor_fun_btn_confirmar);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(HorarioFuncionamentoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void configuraNavBar() {
        setTitle("Horário de Funcionamento");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(HorarioFuncionamentoActivity.this, HomeEmpActivity.class);
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
        Intent intent = new Intent(HorarioFuncionamentoActivity.this, HomeEmpActivity.class);
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    private void eventosClick() {
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Empresa").orderByChild("emp_id").equalTo(idUsuario)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dSnp) {
                                for (DataSnapshot objSnp : dSnp.getChildren()) {
                                    EmpresaModel e = objSnp.getValue(EmpresaModel.class);

                                    String strHini = edtHorarioSemanaInicio.getText().toString().trim();
                                    String strHfim = edtHorarioSemanaFim.getText().toString().trim();
                                    if(!strHini.equals("") && !strHfim.equals("")) {
                                        assert e != null;
                                        e.setEmp_hor_sem_ini(strHini);
                                        e.setEmp_hor_sem_fim(strHfim);
                                    }else{
                                        Toast.makeText(HorarioFuncionamentoActivity.this, "Preenche o horário de início e fim.", Toast.LENGTH_LONG).show();
                                    }
                                    if(!chkFdsFechado.isChecked()) {
                                        String strHfdsI = edtHorarioFdsInicio.getText().toString();
                                        String strHfdsF = edtHorarioFdsFim.getText().toString();
                                        if (!strHfdsI.equals("") && !strHfdsF.equals("")) {
                                            assert e != null;
                                            e.setEmp_hor_fds_ini(strHini);
                                            e.setEmp_hor_fds_fim(strHfdsF);
                                        } else {
                                            Toast.makeText(HorarioFuncionamentoActivity.this, "Preenche o horário de início e fim.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    fireBaseQuery.UpdateObjetcDb(e, "Empresa", idUsuario, databaseReference);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }
        });

        edtHorarioSemanaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String strHora = "";
                        String strMinutes = "";
                        strHora = intTimeToStr(hourOfDay);
                        strMinutes = intTimeToStr(minutes);
                        String strTempo = strHora+":"+strMinutes;

                        edtHorarioSemanaInicio.setText(strTempo);
                    }
                }, hora, minuto, true);

                timePickerDialog.show();

            }
        });

        edtHorarioSemanaFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String strHora = "";
                        String strMinutes = "";
                        strHora = intTimeToStr(hourOfDay);
                        strMinutes = intTimeToStr(minutes);
                        String strTempo = strHora+":"+strMinutes;
                        edtHorarioSemanaFim.setText(strTempo);
                    }
                }, hora, minuto, true);

                timePickerDialog.show();

            }
        });

        edtHorarioFdsInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String strHora = "";
                        String strMinutes = "";
                        strHora = intTimeToStr(hourOfDay);
                        strMinutes = intTimeToStr(minutes);
                        String strTempo = strHora+":"+strMinutes;
                        edtHorarioFdsInicio.setText(strTempo);
                    }
                }, hora, minuto, true);

                timePickerDialog.show();

            }
        });

        edtHorarioFdsFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String strHora = "";
                        String strMinutes = "";
                        strHora = intTimeToStr(hourOfDay);
                        strMinutes = intTimeToStr(minutes);
                        String strTempo = strHora+":"+strMinutes;
                        edtHorarioFdsFim.setText(strTempo);
                    }
                }, hora, minuto, true);

                timePickerDialog.show();

            }
        });

        chkFdsFechado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    linHorarioFdsFeriados.setVisibility(View.GONE);
                } else {
                    linHorarioFdsFeriados.setVisibility(View.VISIBLE);
                }
            }
        });

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

    public void horarioAtual() {
        calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);
    }

//    public void selectedTimeFormat() {
//        if (hora == 0) {
//            hora += 12;
//            amPm = "AM";
//        } else if (hora == 12) {
//            amPm = "PM";
//        } else if (hora > 12){
//            hora -= 12;
//            amPm = "PM";
//        } else {
//            amPm = "AM";
//        }
//    }

}
