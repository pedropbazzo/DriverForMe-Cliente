package com.example.vitor.driverforme_cliente.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitor.driverforme_cliente.R;
import com.example.vitor.driverforme_cliente.entidades.Cliente;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_cliente.logica.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Cadastro extends AppCompatActivity {

    private EditText fdNome, fdEmail, fdSenha, fdPais, fdEstado, fdCidade, fdBairro, fdRua, fdTelefone, fdCpf, fdCartao;
    private Button btCadastro;
    //classe que realiza a autenticação e cadastro do usuário no firebase
    private FirebaseAuth firebaseAuth;
    //classe que permite a manipulação do banco de dados em tempo real do firebase, é a referência do banco de dados (óbvio pelo nome_
    private DatabaseReference referenciaCliente = FirebaseEstatico.getFirebase().child("clientes");
    private Cliente cliente = new Cliente();
    private Base64Custom codificador;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        fdNome = (EditText) findViewById(R.id.fdNome);
        fdEmail = (EditText) findViewById(R.id.fdEmail);
        fdSenha = (EditText) findViewById(R.id.fdSenha);
        fdPais = (EditText) findViewById(R.id.fdPais);
        fdEstado = (EditText) findViewById(R.id.fdEstado);
        fdCidade = (EditText) findViewById(R.id.fdCidade);
        fdBairro = (EditText) findViewById(R.id.fdBairro);
        fdRua = (EditText) findViewById(R.id.fdRua);
        fdTelefone = (EditText) findViewById(R.id.fdTelefone);
        fdCpf = (EditText) findViewById(R.id.fdCpf);
        fdCartao = (EditText) findViewById(R.id.fdCartao);

        btCadastro = (Button) findViewById(R.id.btCadastro);

        codificador = new Base64Custom();



        //recupera a instancia estática criando no FirebaseEstatico do tipo FirebaseAuth.getInstance
        firebaseAuth = FirebaseEstatico.getFirebaseAutenticacao();
        //adicionando escutador no botao de cadastro
        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.setNome(fdNome.getText().toString());
                cliente.setEmail(fdEmail.getText().toString());
                cliente.setSenha(fdSenha.getText().toString());
                cliente.setPais(fdPais.getText().toString());
                cliente.setEstado(fdEstado.getText().toString());
                cliente.setCidade(fdCidade.getText().toString());
                cliente.setBairro(fdBairro.getText().toString());
                cliente.setRua(fdRua.getText().toString());
                cliente.setTelefone(fdTelefone.getText().toString());
                cliente.setCpf(fdCpf.getText().toString());
                cliente.setCartao(fdCartao.getText().toString());
                cliente.setAvaliacao(10);
                Log.i("Cliente", cliente.toString());
                builder = new AlertDialog.Builder(Cadastro.this);
                builder.setTitle("Cadastro");
                builder.setMessage("Cadastrando...");
                dialog  = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                cadastraCliente(cliente.getEmail(), cliente.getSenha());
            }
        });



    }


    //cadastrando o usuário no firebase com email e senha
    public void cadastraCliente(String email, String senha){
        try{
            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Cadastro.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();
                        referenciaCliente.child(codificador.codificar(cliente.getEmail())).setValue(cliente);
                        firebaseAuth.signOut();
                        Intent intent = new Intent(Cadastro.this, TelaLogin.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        dialog.cancel();
                        Toast.makeText(Cadastro.this, "Erro ao cadastrar usuário", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }catch(Exception e){
            dialog.cancel();
            Toast.makeText(Cadastro.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
