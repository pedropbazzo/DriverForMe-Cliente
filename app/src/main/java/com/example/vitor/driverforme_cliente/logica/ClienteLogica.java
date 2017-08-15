package com.example.vitor.driverforme_cliente.logica;

import android.util.Log;
import android.widget.EditText;

import com.example.vitor.driverforme_cliente.entidades.Cliente;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Vitor on 06/06/2017.
 * Esta classe é responsável por realizar as autenticações dos campos do aplicativo, valida dados como email, senha e informações
 * digitadas pelo usuário
 */

public class ClienteLogica {

    DatabaseReference raizCliente = FirebaseEstatico.getFirebase().child("cliente");

   /* Esse método valida o telefone digitado pelo usuário apesar do problema ja ser praticamente
    * resolvido pelo próprio Android Studio que so permite a entrada de caracteres numéricos e das
    * máscaras aplicadas na interface
    */
    public String validaTelefone(String telefone){
        if(telefone.length()<13){
            return "\nCampo telefone invalido";
        }
        else
            return "";
    }
    /* Mesmo coméntario da validação do telefone com a diferença que é o CPF.
    */
    public String validaCpf(String cpf){
        if(cpf.length()<11)
            return "\nCampo cpf invalido";
        else
            return "";
    }

    public String validaCartao(String cartao){
        if(cartao.length()<16)
            return "\nCampo cartao invalido";
        else
            return "";

    }

    //verifica se o email digitado pelo usuário possui uma estrutura correta, não verifica a existência ou não, apenas se é válido
    public final static String validaEmail(CharSequence target) {
        if (target == null) {
            return "O campo email está em branco\n";
        } else {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()){
                return "";
            }
            else{
                return "O email possui estrutura invalida\n";
            }
        }
    }
   /* Valida o nome da pessoa, evitando que o usuário digite caracteres inválidos no campo de nome como @, *, &, - e etc
   *
   */
    public String validaNome(String nome) {
        nome = nome.replace(" ", "");
        if(!nome.matches("[A-Z][a-z][\\wÀ-ú]*")){
            return "Campo nome inválido. O campo deve conter apenas letras e as iniciais devem ser maiúsculas!\n";
        }
        else
            return "";
    }
    /* Valida a senha do usuário, evitando que o usuário entre com uma senha considerada "fraca"
   *   obrigando-o a entrar com uma senha com 10 ou mais dígitos, e com caracteres maiúsculos e
   *   minúsculos
   */
    public String validaSenha(String senha){
        boolean numero = false;
        boolean minuscula =false;
        boolean maiuscula = false;
        boolean tamanho = false;
        Log.i("Senha na logica", senha);

        if(senha.length()>=10){
            tamanho =true;
        }

        for(int i=0; i<senha.length();i++){
            if((int) senha.charAt(i)>64&&(int) senha.charAt(i)<90){
                maiuscula = true;
            }
            if((int) senha.charAt(i)>96&&(int) senha.charAt(i)<123){
                minuscula = true;
            }
            if((int) senha.charAt(i)>47&&(int) senha.charAt(i)<58){
                numero = true;
            }
        }

        if(numero&&minuscula&&maiuscula&&tamanho)
            return "";
        else
            return "Campo senha invalido! Sua senha deve possuir letras maiusculas, minusculas, numero e ser maior do que 10!\n";
    }

    public boolean verificaCampo(EditText et){

        if(et.getText().toString().equals(""))
            return false;
        else
            return true;
    }

    public boolean validaHorario(String horario){

        horario = horario.replaceAll(":", "");
        int aux = Integer.parseInt(horario);

        if(aux>2359){
            return false;
        }
        else{
            return true;
        }
    }


    public boolean validaData(String data){
        data = data.replaceAll("/", "");

        int aux = Integer.parseInt(data);

        aux = aux-2017;

        if(aux>31120000){
            return false;
        }
        else{
            return true;
        }
    }
}
