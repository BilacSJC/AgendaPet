package com.converter.cambio.app_petshop.Model;

public class ServicoEmpresaModel {
    private int ser_id;
    private String ser_nome;
    private double ser_preco;
    private String ser_descricao;
    private int ser_emp_id;


    public int getSer_id() {
        return ser_id;
    }

    public void setSer_id(int ser_id) {
        this.ser_id = ser_id;
    }

    public String getSer_nome() {
        return ser_nome;
    }

    public void setSer_nome(String ser_nome) {
        this.ser_nome = ser_nome;
    }

    public double getSer_preco() {
        return ser_preco;
    }

    public void setSer_preco(double ser_preco) {
        this.ser_preco = ser_preco;
    }

    public int getSer_emp_id() {
        return ser_emp_id;
    }

    public void setSer_emp_id(int ser_emp_id) {
        this.ser_emp_id = ser_emp_id;
    }

    public String getSer_descricao() {
        return ser_descricao;
    }

    public void setSer_descricao(String ser_descricao) {
        this.ser_descricao = ser_descricao;
    }
}
