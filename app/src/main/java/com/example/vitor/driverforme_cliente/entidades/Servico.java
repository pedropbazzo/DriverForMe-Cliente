package com.example.vitor.driverforme_cliente.entidades;

/**
 * Created by vitor on 25/05/17.
 */

public class Servico {

    private String data, horarioPedido, origem, destino, cliente, motorista, horarioAtendimento, tipo;
    private double preco;

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHorarioPedido() {
        return horarioPedido;
    }

    public void setHorarioPedido(String horarioPedido) {
        this.horarioPedido = horarioPedido;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getHorarioAtendimento() {
        return horarioAtendimento;
    }

    public void setHorarioAtendimento(String horarioAtendimento) {
        this.horarioAtendimento = horarioAtendimento;
    }

    public String toString(){
        return "----------------"+getTipo()+"---------------------"+"\nOrigem: "+getOrigem() +"\nDestino: "+getDestino()
                +"\nCliente: "+getCliente()+"\nMotorista: "+getMotorista()+"\nPreço: "+getPreco()
                +"\n--------------------------------------------------";
    }

}
