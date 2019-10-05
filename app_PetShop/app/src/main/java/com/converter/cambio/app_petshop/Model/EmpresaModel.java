package com.converter.cambio.app_petshop.Model;

public class EmpresaModel extends EnderecoModel{
    private String emp_id;
    private String emp_nome;
    private String emp_telefone;
    private EnderecoModel emp_endereco;
    private String emp_cnpj;
    private String emp_email;
    private String emp_senha;
    private String emp_senha_antiga;
    private String emp_data_ultima_alteracao_senha;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_nome() {
        return emp_nome;
    }

    public void setEmp_nome(String emp_nome) {
        this.emp_nome = emp_nome;
    }

    public String getEmp_telefone() {
        return emp_telefone;
    }

    public void setEmp_telefone(String emp_telefone) {
        this.emp_telefone = emp_telefone;
    }

    public String getEmp_cnpj() {
        return emp_cnpj;
    }

    public void setEmp_cnpj(String emp_cnpj) {
        this.emp_cnpj = emp_cnpj;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_senha() {
        return emp_senha;
    }

    public void setEmp_senha(String emp_senha) {
        this.emp_senha = emp_senha;
    }

    public String getEmp_senha_antiga() {
        return emp_senha_antiga;
    }

    public void setEmp_senha_antiga(String emp_senha_antiga) {
        this.emp_senha_antiga = emp_senha_antiga;
    }

    public String getEmp_data_ultima_alteracao_senha() {
        return emp_data_ultima_alteracao_senha;
    }

    public void setEmp_data_ultima_alteracao_senha(String emp_data_ultima_alteracao_senha) {
        this.emp_data_ultima_alteracao_senha = emp_data_ultima_alteracao_senha;
    }

    public EnderecoModel getEmp_endereco() {
        return emp_endereco;
    }

    public void setEmp_endereco(EnderecoModel emp_endereco) {
        this.emp_endereco = emp_endereco;
    }
}
