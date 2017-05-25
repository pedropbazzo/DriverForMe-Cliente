package com.example.vitor.driverforme_cliente.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vitor.driverforme_cliente.R;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TelaLogin extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button btCadastro, btLogin;
    private EditText fdEmail, fdSenha;
    //o builder e dialog são fundamentais para bloquear as ações do usuario enquanto o sistema realiza a autenticação (é a caixinha que aparece com titulo "Login" e corpo "Logando"
    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        btCadastro = (Button) findViewById(R.id.btCadastro);
        btLogin = (Button) findViewById(R.id.btLogin);

        fdEmail = (EditText) findViewById(R.id.fdEmail);
        fdSenha = (EditText) findViewById(R.id.fdSenha);

        firebaseAuth = FirebaseEstatico.getFirebaseAutenticacao();

        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, Cadastro.class);
                startActivity(intent);

            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(TelaLogin.this);
                builder.setTitle("Login");
                builder.setMessage("Logando...");
                dialog  = builder.create();
                dialog.show();
                login(fdEmail.getText().toString(), fdSenha.getText().toString());
            }
        });


    }

    //classe que realiza a autenticação através de email e senha
    public void login(String email, String senha){
        try {
            firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(TelaLogin.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dialog.cancel();
                        Toast.makeText(TelaLogin.this, "Sucesso ao realizar login", Toast.LENGTH_LONG).show();

                    } else {
                        dialog.cancel();
                        Toast.makeText(TelaLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e){
            dialog.cancel();
            Toast.makeText(TelaLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
