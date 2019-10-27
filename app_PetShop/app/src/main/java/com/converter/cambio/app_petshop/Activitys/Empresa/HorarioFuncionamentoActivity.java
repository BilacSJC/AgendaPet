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
import android.widget.EditText;
import android.widget.TimePicker;

import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FireBaseQuery fireBaseQuery;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_horario_funcionamento);

        context = HorarioFuncionamentoActivity.this;
        getExtraIdUsuario();
        inicializarComponentes();
        inicializarFirebase();
        configuraNavBar();
        eventosClick();
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

        btnConfirmar = findViewById(R.id.hor_fun_btn_confirmar);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(context);
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

    private void eventosClick() {
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edtHorarioSemanaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarioAtual();
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        edtHorarioSemanaInicio.setText(hourOfDay + ":" + minutes);
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
                        edtHorarioSemanaFim.setText(hourOfDay + ":" + minutes);
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
                        edtHorarioFdsInicio.setText(hourOfDay + ":" + minutes);
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
                        edtHorarioFdsFim.setText(hourOfDay + ":" + minutes);
                    }
                }, hora, minuto, true);

                timePickerDialog.show();

            }
        });

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
