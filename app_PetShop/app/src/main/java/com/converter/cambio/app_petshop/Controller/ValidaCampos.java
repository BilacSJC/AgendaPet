package com.converter.cambio.app_petshop.Controller;

import com.converter.cambio.app_petshop.Model.ClienteModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidaCampos {


    public String vString(String strCampo){
        if(strCampo == null || strCampo.equals("")){
            return "O campo nome é obrigatório";
        }
        else if(strCampo.trim().length() < 3){
            return "No mínimo 3 caracteres";
        }

        return "ok";
    }

    public String vStringEmail(String strEmail) {
        if(strEmail.trim().contains("@") && strEmail.trim().contains(".") && strEmail.trim().length() > 4){
            return "ok";
        }
        else if(strEmail.trim().equals("")){
            return "O campo e-mail é obrigatório!";
        }
        return "Digite um e-mail válido.";
    }

    public String vStringSenha(String strSenha) {

        if(strSenha.trim().equals("")){
            return "O campo senha é obrigatório!";
        }
        else if(strSenha.trim().length() < 8){
            return "A senha deve conter no mínimo 8 caracteres!";
        }
        return "ok";
    }

    public String vStringTelefone(String strTelefone) {
        if(strTelefone.trim().equals("")){
            return "O campo Telefone deve ser preenchido!";
        }
        else if(strTelefone.trim().length() < 8){
            return "Digite um telefone válido";
        }
        if(strTelefone.trim().length() >= 8) {
            return "ok";
        }
        return "formato de telefone inválido";
    }

    public String vStringCpf(String strCpf) {

        if(strCpf.trim().equals("")){
            return "O cmapo Cpf é obrigatório";
        }
        else if(strCpf.trim().length() < 11){
            return "Digite um CPF válido!";
        }
        else if(strCpf.trim().length() > 12){
            return  "Digite somente os números!";
        }
        return "ok";
    }

    public String vStringCnpj(String strCnpj) {

        if(strCnpj.trim().equals("")){
            return "O cmapo CNPJ é obrigatório";
        }
        else if(strCnpj.trim().length() < 11){
            return "Digite um CNPJ válido!";
        }
        else if(strCnpj.trim().length() > 14){
            return  "Digite somente os números!";
        }
        return "ok";
    }

    public String vStringSpn(String strTexto) {
        if(strTexto.trim().toUpperCase().contains("SELECIONE")){
            return "Selecione um item.";
        }
        return "ok";
    }

    public String vInt(String strNum) {
        if(strNum.trim().equals("")){
            return "Digite a idade do Pet.";
        }
        return "ok";

    }

    public boolean senhaIsValida(String strDataUltimaAlteracao){

        SimpleDateFormat formatDataMes = new SimpleDateFormat("MM");
        SimpleDateFormat formatDataAno = new SimpleDateFormat("yyyy");
        Date data = new Date();

        int mesAtual = Integer.valueOf(formatDataMes.format(data));
        int anoAtual = Integer.valueOf(formatDataAno.format(data));
        int mesModificacao = 0;
        int anoModificacao = 0;

        if(strDataUltimaAlteracao.trim().length() > 0){
            mesModificacao = Integer.valueOf(strDataUltimaAlteracao.substring(3,5));
            anoModificacao = Integer.valueOf(strDataUltimaAlteracao.substring(6,10));
        }

        if(anoModificacao < anoAtual){
            return false;
        }

        if(mesAtual - mesModificacao >= 2){//60 dias
            return  false;
        }

        return true;
    }

    public boolean validacaoBasicaStr(String str){

        if(str.trim().equals("")){
            return false;
        }
        return true;
    }

    public String vStringData(String strData) {
        if(strData.equals("")){
            return "Selecione uma Data";
        }

        SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String format = formatData.format(data);
        int dSol = Integer.valueOf(format.substring(0,2));
        int dStr = Integer.valueOf(strData.substring(0,2));
        if(dStr - dSol < 0){
            return "Data inválida, Selecione a partir de hoje";
        }

        return "ok";
    }

    public String vStringHora(String strHora) {
        if(strHora.equals("")){
            return "Selecione um horário";
        }



        return "ok";
    }
}
