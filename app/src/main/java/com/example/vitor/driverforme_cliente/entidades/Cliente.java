package com.example.vitor.driverforme_cliente.entidades;

import com.google.firebase.database.Exclude;

/**
 * Created by vitor on 25/05/17.
 */

public class Cliente {
    private String nome, telefone, rua, bairro, cidade, estado, pais, email, senha, cartao, cpf;
    private double avaliacao;

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }


    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String toString(){
        return "Nome: "+getNome()+"\nCpf: "+getCpf()+"\nCartao: "+getCartao()+"\nEmail: "+getEmail()+"\nSenha:" +getSenha()+
                "\nAvaliacao: "+getAvaliacao()+"\nPais: "+getPais()+"\nEstado: "+getEstado()+
                "\nCidade: "+getCidade()+"\nBairro: "+getBairro()+"\nRua: "+getRua()+"\nTelefone: "+getTelefone();
    }

}
