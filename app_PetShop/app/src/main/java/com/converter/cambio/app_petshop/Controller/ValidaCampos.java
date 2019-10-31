package com.converter.cambio.app_petshop.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaCampos {


    public String vString(String strCampo) {
        if (strCampo == null || strCampo.equals("")) {
            return "O campo nome é obrigatório";
        } else if (strCampo.trim().length() < 3) {
            return "No mínimo 3 caracteres";
        }

        return "ok";
    }

    public String vStringCpf(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return "Digite um Cpf válido!";

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return "ok";
            else return "Digite um Cpf válido";
        } catch (InputMismatchException erro) {
            return "Digite um Cpf válido";
        }
    }

    public String vStringEmail(String email) {
        String isEmailIdValid = "";
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = "ok";
            } else {
                return "Digiteum e-mail válido";
            }
        } else {
            return "O e-mail deve ser preenchido";
        }
        return isEmailIdValid;
    }

//    public String vStringEmail(String strEmail) {
//        if(strEmail.trim().contains("@") && strEmail.trim().contains(".") && strEmail.trim().length() > 4){
//            return "ok";
//        }
//        else if(strEmail.trim().equals("")){
//            return "O campo e-mail é obrigatório!";
//        }
//        return "Digite um e-mail válido.";
//    }

    public String vStringSenha(String strSenha) {

        if (strSenha.trim().equals("")) {
            return "O campo senha é obrigatório!";
        } else if (strSenha.trim().length() < 8) {
            return "A senha deve conter no mínimo 8 caracteres!";
        }
        return "ok";
    }

    public String vStringTelefone(String strTelefone) {
        if (strTelefone.trim().equals("")) {
            return "O campo Telefone deve ser preenchido!";
        } else if (strTelefone.trim().length() < 8) {
            return "Digite um telefone válido";
        }
        if (strTelefone.trim().length() >= 8) {
            return "ok";
        }
        return "Formato de telefone inválido";
    }

//    public String vStringCpf(String strCpf) {
//
//        if(strCpf.trim().equals("")){
//            return "O campo CPF é obrigatório";
//        }
//        else if(strCpf.trim().length() < 11){
//            return "Digite um CPF válido!";
//        }
//        else if(strCpf.trim().length() > 12){
//            return  "Digite somente os números!";
//        }
//        return "ok";
//    }

    public String vStringCnpj(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return "Digite um CNPJ válido";

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return "ok";
            else return "Digite um CNPJ válido";
        } catch (InputMismatchException erro) {
            return "Digite um CNPJ válido";
        }
    }


//    public String vStringCnpj(String strCnpj) {
//
//        if(strCnpj.trim().equals("")){
//            return "O campo CNPJ é obrigatório";
//        }
//        else if(strCnpj.trim().length() < 11){
//            return "Digite um CNPJ válido!";
//        }
//        else if(strCnpj.trim().length() > 14){
//            return  "Digite somente os números!";
//        }
//        return "ok";
//    }

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
