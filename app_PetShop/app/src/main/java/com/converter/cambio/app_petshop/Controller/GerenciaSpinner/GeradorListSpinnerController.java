package com.converter.cambio.app_petshop.Controller.GerenciaSpinner;

import java.util.ArrayList;
import java.util.List;

public class GeradorListSpinnerController {

    public List<String> getLstEstados() {
        List<String> lstEstados = new ArrayList<>();

        lstEstados.add("Selecione um Estado");
        lstEstados.add("AC - Acre");
        lstEstados.add("AL - Alagoas");
        lstEstados.add("AP - Amapá");
        lstEstados.add("AM - Amazonas");
        lstEstados.add("BA - Bahia");
        lstEstados.add("CE - Ceará");
        lstEstados.add("DF - Distrito Federal");
        lstEstados.add("ES - Espírito Santo");
        lstEstados.add("GO - Goiás");
        lstEstados.add("MA - Maranhão");
        lstEstados.add("MT - Mato Grosso");
        lstEstados.add("MS - Mato Grosso do Sul");
        lstEstados.add("MG - Minas Gerais");
        lstEstados.add("PA - Pará");
        lstEstados.add("PB - Paraíba");
        lstEstados.add("PR - Paraná");
        lstEstados.add("PE - Pernambuco");
        lstEstados.add("PI - Piauí");
        lstEstados.add("RJ - Rio de Janeiro");
        lstEstados.add("RN - Rio Grande do Norte");
        lstEstados.add("RS - Rio Grande do Sul");
        lstEstados.add("RO - Rondônia");
        lstEstados.add("RR - Roraima");
        lstEstados.add("SC - Santa Catarina");
        lstEstados.add("SP - São Paulo");
        lstEstados.add("SE - Sergipe");
        lstEstados.add("TO - Tocantins");

        return lstEstados;
    }

    public List<String> getLstSexo() {
        List<String> lstSexo = new ArrayList<>();

        lstSexo.add("Selecione um sexo");
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
