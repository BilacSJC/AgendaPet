package com.converter.cambio.app_petshop.Activitys.Empresa;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.converter.cambio.app_petshop.Activitys.Cliente.PaginaPrincipalActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.PerfilActivity;
import com.converter.cambio.app_petshop.Activitys.TipoLoginActivity;
import com.converter.cambio.app_petshop.Controller.FireBaseConexao;
import com.converter.cambio.app_petshop.Controller.FireBaseQuery;
import com.converter.cambio.app_petshop.Controller.GerenciaSpinner.GeradorListSpinnerController;
import com.converter.cambio.app_petshop.Controller.MetodosPadraoController;
import com.converter.cambio.app_petshop.Controller.ValidaCampos;
import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.converter.cambio.app_petshop.Model.EmpresaModel;
import com.converter.cambio.app_petshop.Model.EnderecoModel;
import com.converter.cambio.app_petshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CadastroEmpresaActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtCnpj, edtTelefone, edtSenha;
    private EditText edtCidade, edtBairro, edtLogradouro, edtNumero, edtCep;
    private EditText edtHorarioSemanaInicio, edtHorarioSemanaFim, edtHorarioFdsInicio, edtHorarioFdsFim;
    private MaterialButton btnCadastrar;
    private Spinner spnEstado;
    private Calendar calendar;
    private int hora, minuto;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private FireBaseQuery fireBaseQuery;
    private FirebaseAuth auth;
    private CheckBox checkBox;
    private LinearLayout linHorarioFdsFeriados;
    private DatabaseReference databaseReference;
    private Context context;
    private ValidaCampos v;
    private MetodosPadraoController m = new MetodosPadraoController();
    private GeradorListSpinnerController geradorListSpinnerController = new GeradorListSpinnerController();
    private TimePickerDialog tpdHorarioSemanaInicio, tpdHorarioSemanaFim, tpdHorarioFdsInicio, tpdHorarioFdsFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_cadastro);
        inicializarComponentes();
        inicializarFirebase();
        configuraNavBar();
        preencheSpinnerEstados();
        eventosClick();
    }

    private void configuraNavBar() {
        setTitle("Cadastro de Usuário");
        ActionBar actionBar = getSupportActionBar(); //instancia objt da BAR
        actionBar.setDisplayHomeAsUpEnabled(true); //exibe o icone
        actionBar.setHomeButtonEnabled(true); //habilita click
    }

    //Para inserir a ação e selecionar para qual página voltar...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CadastroEmpresaActivity.this, LoginEmpresaActivity.class);
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
        Intent intent = new Intent(CadastroEmpresaActivity.this, LoginEmpresaActivity.class);
        startActivity(intent);
        super.onBackPressed();
        finish();
    }

    private void preencheSpinnerEstados() {
        List<String> lsEstados = geradorListSpinnerController.getLstEstados();
        ArrayAdapter<String> estado = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, lsEstados);
        spnEstado.setAdapter(estado);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FireBaseConexao.getFirebaseAuth();
    }

    private void eventosClick() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpresaModel empresaModel = validacaoInputUsuario();
                if (empresaModel.getEmp_id() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }
                EnderecoModel enderecoModel = setEnderecoModel(empresaModel);

                if (enderecoModel.getId_endereco() == null) {
                    m.alertDialog(context, "ATENCÃO", "Preencha todos os campos.");
                    return;
                }

                cadastrarUsuario(empresaModel, enderecoModel);
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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    private void cadastrarUsuario(EmpresaModel empresaModel, EnderecoModel enderecoModel) {
        fireBaseQuery.InsertObjectDb(empresaModel, "Empresa", empresaModel.getEmp_id(), databaseReference);
        if (databaseReference.getDatabase() != null) {
            fireBaseQuery.InsertObjectDb(enderecoModel, "Endereco", enderecoModel.getId_endereco(), databaseReference);
            cadastrarLoginUsuario(empresaModel);
        }
    }

    private void cadastrarLoginUsuario(final EmpresaModel empresaModel) {
        auth.createUserWithEmailAndPassword(empresaModel.getEmp_email(), empresaModel.getEmp_senha())
                .addOnCompleteListener(CadastroEmpresaActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // task retorna o status da autenticação
                                    limparCampos();
                                    alertDialogBackToLogin("Sucesso!", "Usuário cadastrado com sucesso!");
                            }
                        }
                );
    }

    private EmpresaModel validacaoInputUsuario() {
        EmpresaModel e = new EmpresaModel();
        String strMsgHorIni = v.vString(edtHorarioSemanaInicio.getText().toString().trim());
        String strMsgHorFim = v.vString(edtHorarioSemanaFim.getText().toString().trim());
        String strMsgFdsIni = v.vString(edtHorarioFdsInicio.getText().toString().trim());
        String strMsgFdsFim = v.vString(edtHorarioFdsFim.getText().toString().trim());
        String strMsgNome = v.vString(edtNome.getText().toString().trim());
        String strMsgCnpj = v.vStringCnpj(edtCnpj.getText().toString().trim());
        String strMsgTelefone = v.vStringTelefone(edtTelefone.getText().toString().trim());
        String strMsgEmail = v.vStringEmail(edtEmail.getText().toString().trim());
        String strMsgSenha = v.vStringSenha(edtSenha.getText().toString().trim());
        String strMsgEstado = v.vStringSpn(spnEstado.getSelectedItem().toString().trim());
        boolean booCidade = v.validacaoBasicaStr(edtCidade.getText().toString().trim());
        boolean booBairro = v.validacaoBasicaStr(edtBairro.getText().toString().trim());
        boolean booLogradouro = v.validacaoBasicaStr(edtLogradouro.getText().toString().trim());
        boolean booNumero = v.validacaoBasicaStr(edtNumero.getText().toString().trim());
        boolean booCep = v.validacaoBasicaStr(edtCep.getText().toString().trim());
        int contMsg = 0;

        if (!strMsgHorIni.equals("ok")) {
            edtHorarioSemanaInicio.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!strMsgHorFim.equals("ok")) {
            edtHorarioSemanaFim.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if(!checkBox.isChecked()) {
            if (!strMsgFdsIni.equals("ok")) {
                edtHorarioFdsInicio.setError("Campo Obrigatório");
                contMsg = +1;
            }
            if (!strMsgFdsFim.equals("ok")) {
                edtHorarioFdsFim.setError("Campo Obrigatório");
                contMsg = +1;
            }
        }
        if (!strMsgEstado.equals("ok")) {
            m.alertDialog(CadastroEmpresaActivity.this, "ATENÇÃO!", "Selecione um Estado.");
        }
        if (!booCidade) {
            edtCidade.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booBairro) {
            edtBairro.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booLogradouro) {
            edtLogradouro.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booNumero) {
            edtNumero.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!booCep) {
            edtCep.setError("Campo Obrigatório");
            contMsg = +1;
        }
        if (!strMsgNome.equals("ok")) {
            edtNome.setError(strMsgNome);
            contMsg = +1;
        }
        if (!strMsgCnpj.equals("ok")) {
            edtCnpj.setError(strMsgCnpj);
            contMsg = +1;
        }
        if (!strMsgTelefone.equals("ok")) {
            edtTelefone.setError(strMsgTelefone);
            contMsg = +1;
        }
        if (!strMsgEmail.equals("ok")) {
            edtEmail.setError(strMsgEmail);
            contMsg = +1;
        }
        if (!strMsgSenha.equals("ok")) {
            edtSenha.setError(strMsgSenha);
            contMsg = +1;
        }

        if (contMsg > 0) {
            return new EmpresaModel();
        }

        e.setEmp_id(String.valueOf(UUID.randomUUID()));
        e.setEmp_nome(edtNome.getText().toString());
        e.setEmp_cnpj(edtCnpj.getText().toString());
        e.setEmp_email(edtEmail.getText().toString());
        e.setEmp_senha(edtSenha.getText().toString());
        e.setEmp_usu_tipo("Empresa");
        e.setEmp_telefone(edtTelefone.getText().toString());
        e.setEmp_senha_antiga(edtSenha.getText().toString().trim());
        e.setEmp_id_endereco(UUID.randomUUID().toString().trim());
        e.setEmp_hor_sem_ini(edtHorarioSemanaInicio.getText().toString().trim());
        e.setEmp_hor_sem_fim(edtHorarioSemanaFim.getText().toString().trim());

        if(!checkBox.isChecked()) {
            e.setEmp_hor_fds_ini(edtHorarioFdsInicio.getText().toString().trim());
            e.setEmp_hor_fds_fim(edtHorarioFdsFim.getText().toString().trim());
        }
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        e.setEmp_data_ultima_alteracao_senha(dataFormatada);

        return e;
    }

    private EnderecoModel setEnderecoModel(EmpresaModel empresaModel) {
        EnderecoModel e = new EnderecoModel();
        ValidaCampos v = new ValidaCampos();

        boolean booMensagemCep = v.validacaoBasicaStr(edtCep.getText().toString());
        boolean booMensagemCidade = v.validacaoBasicaStr(edtCidade.getText().toString());
        boolean booMensagemNumero = v.validacaoBasicaStr(edtNumero.getText().toString());
        boolean booMensagemLogradouro = v.validacaoBasicaStr(edtLogradouro.getText().toString());
        int intPositionSelected = spnEstado.getSelectedItemPosition();
        boolean strMensagemBairro = v.validacaoBasicaStr(edtBairro.getText().toString());

        int contMsg = 0;
        String strMsg = "Preencha este campo";
        if (intPositionSelected <= 0) {
            m.alertDialog(context, "ATENÇÃO", "Selecione um estado");
            contMsg += 1;
        }
        if (!booMensagemCep) {
            edtCep.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemCidade) {
            edtCidade.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemNumero) {
            edtNumero.setError(strMsg);
            contMsg += 1;
        }
        if (!booMensagemLogradouro) {
            edtLogradouro.setError(strMsg);
            contMsg += 1;
        }
        if (!strMensagemBairro) {
            edtBairro.setError(strMsg);
            contMsg += 1;
        }

        if (contMsg > 0) {
            return new EnderecoModel();
        }

        e.setId_usuario(empresaModel.getEmp_id());
        e.setId_endereco(empresaModel.getEmp_id_endereco());
        e.setEstado(spnEstado.getSelectedItem().toString().trim());
        e.setCidade(edtCidade.getText().toString().trim());
        e.setBairro(edtBairro.getText().toString().trim());
        e.setLogradouro(edtLogradouro.getText().toString().trim());
        e.setNumero(edtNumero.getText().toString().trim());
        e.setCep(edtCep.getText().toString().trim());

        return e;
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
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroEmpresaActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtCnpj.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
        edtTelefone.setText("");
        btnCadastrar.setText("");
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.cad_emp_edt_nome);
        edtCnpj = findViewById(R.id.cad_emp_edt_cnpj);
        edtEmail = findViewById(R.id.cad_emp_edt_email);
        edtSenha = findViewById(R.id.cad_emp_edt_senha);
        edtTelefone = findViewById(R.id.cad_emp_edt_telefone);
        btnCadastrar = findViewById(R.id.cad_emp_btn_cadastrar);

        edtCidade = findViewById(R.id.cad_emp_edt_cidade);
        edtBairro = findViewById(R.id.cad_emp_edt_bairro);
        edtLogradouro = findViewById(R.id.cad_emp_edt_logradouro);
        edtNumero = findViewById(R.id.cad_emp_edt_numero);
        edtCep = findViewById(R.id.cad_emp_edt_cep);
        spnEstado = findViewById(R.id.cad_emp_spn_estado);

        edtHorarioSemanaInicio = findViewById(R.id.cad_hor_fun_edt_semana_inicio);
        edtHorarioSemanaFim = findViewById(R.id.cad_hor_fun_edt_semana_fim);
        edtHorarioFdsInicio = findViewById(R.id.cad_hor_fun_edt_fds_feriados_inicio);
        edtHorarioFdsFim = findViewById(R.id.cad_hor_fun_edt_fds_feriados_fim);
        linHorarioFdsFeriados = findViewById(R.id.emp_cad_lin_lay);
        checkBox = findViewById(R.id.cad_hor_fun_chk_fds_feriados_fechado);
        context = CadastroEmpresaActivity.this;
        v = new ValidaCampos();
        fireBaseQuery = new FireBaseQuery();
    }

    private void alertDialogBackToLogin(String strTitle, String strMsg) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CadastroEmpresaActivity.this, LoginEmpresaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }
}