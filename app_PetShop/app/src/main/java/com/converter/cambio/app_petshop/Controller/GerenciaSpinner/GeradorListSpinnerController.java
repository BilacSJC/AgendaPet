package com.converter.cambio.app_petshop.Controller.GerenciaSpinner;

import java.util.ArrayList;
import java.util.List;

public class GeradorListSpinnerController {

    public List<String> getLstEstados() {
        List<String> lstEstados = new ArrayList<>();

        lstEstados.add("Selecione um estado");
        lstEstados.add("AC");
        lstEstados.add("AL");
        lstEstados.add("AP");
        lstEstados.add("AM");
        lstEstados.add("BA");
        lstEstados.add("CE");
        lstEstados.add("DF");
        lstEstados.add("ES");
        lstEstados.add("GO");
        lstEstados.add("MA");
        lstEstados.add("MT");
        lstEstados.add("MS");
        lstEstados.add("MG");
        lstEstados.add("PA");
        lstEstados.add("PB");
        lstEstados.add("PR");
        lstEstados.add("PE");
        lstEstados.add("PI");
        lstEstados.add("RJ");
        lstEstados.add("RN");
        lstEstados.add("RS");
        lstEstados.add("RO");
        lstEstados.add("RR");
        lstEstados.add("SC");
        lstEstados.add("SP");
        lstEstados.add("SE");
        lstEstados.add("TO");

        return lstEstados;
    }

    public List<String> getLstSexo() {
        List<String> lstSexo = new ArrayList<>();

        lstSexo.add("Selecione o sexo");
        lstSexo.add("Macho");
        lstSexo.add("Fêmea");

        return lstSexo;

    }

    public List<String> getLstPorte() {
        List<String> lstPorte = new ArrayList<>();

        lstPorte.add("Selecione um porte");
        lstPorte.add("Pequeno");
        lstPorte.add("Médio");
        lstPorte.add("Grande");

        return lstPorte;
    }
}
