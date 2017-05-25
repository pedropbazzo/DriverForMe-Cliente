package com.example.vitor.driverforme_cliente.estaticos;

import com.example.vitor.driverforme_cliente.entidades.Cliente;

/**
 * Created by vitor on 25/05/17.
 */

public class ClienteEstatico {
    private static Cliente cliente = new Cliente();

    public ClienteEstatico(Cliente c){
        cliente = c;
    }

    public ClienteEstatico(){

    }

    public static Cliente getCliente(){
        return cliente;
    }

    public static  void setCliente(Cliente c){
        cliente = c;
    }
}
