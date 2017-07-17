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
import com.example.vitor.driverforme_cliente.estaticos.ClienteEstatico;
import com.example.vitor.driverforme_cliente.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_cliente.logica.Base64Custom;
import com.example.vitor.driverforme_cliente.logica.ClienteLogica;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private ClienteEstatico ce;
    //criando query para pesquisar um cliente na hora da edição
    private Query qrCliente;

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

        ce = new ClienteEstatico();


        //criação das mascaras e dos escutadores
        SimpleMaskFormatter mascaraTelefone = new SimpleMaskFormatter("+NN (NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(fdTelefone, mascaraTelefone);

        SimpleMaskFormatter mascaraCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskCpf = new MaskTextWatcher(fdCpf, mascaraCpf);

        SimpleMaskFormatter mascaraCartao = new SimpleMaskFormatter("NNNN NNNN NNNN NNNN");
        MaskTextWatcher maskCartao = new MaskTextWatcher(fdCartao, mascaraCartao);

        //adicionando os escutadores criados nas fields

        fdTelefone.addTextChangedListener(maskTelefone);
        fdCpf.addTextChangedListener(maskCpf);
        fdCartao.addTextChangedListener(maskCartao);

        Bundle extras = getIntent().getExtras();


        if(extras!=null) {

            btCadastro.setText("CADASTRO");
            //recupera a instancia estática criando no FirebaseEstatico do tipo FirebaseAuth.getInstance
            firebaseAuth = FirebaseEstatico.getFirebaseAutenticacao();
            //adicionando escutador no botao de cadastro
            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClienteLogica cl = new ClienteLogica();
                    String erro = "";
                    erro += cl.validaNome(fdNome.getText().toString());
                    erro += cl.validaEmail(fdEmail.getText().toString());
                    erro += cl.validaSenha(fdSenha.getText().toString());
                    erro += cl.validaTelefone(fdTelefone.getText().toString());
                    erro += cl.validaCpf(fdCpf.getText().toString());
                    erro += cl.validaCartao(fdCartao.getText().toString());

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

                    if(erro.equals("")) {
                        builder = new AlertDialog.Builder(Cadastro.this);
                        builder.setTitle("Cadastro");
                        builder.setMessage("Cadastrando...");
                        dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        cadastraCliente(cliente.getEmail(), cliente.getSenha());
                    }
                    else{
                        Toast.makeText(Cadastro.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            qrCliente = referenciaCliente.child(codificador.codificar(ce.getCliente().getEmail()));
            final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
            btCadastro.setText("CONFIRMAR");
            qrCliente.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cliente = dataSnapshot.getValue(Cliente.class);
                    Log.i("Cliente", cliente.toString());
                    fdNome.setText(cliente.getNome());
                    fdEmail.setText(usuario.getEmail());
                    fdEmail.setEnabled(false);
                    fdSenha.setText(cliente.getSenha());
                    fdPais.setText(cliente.getPais());
                    fdEstado.setText(cliente.getEstado());
                    fdCidade.setText(cliente.getCidade());
                    fdBairro.setText(cliente.getBairro());
                    fdRua.setText(cliente.getRua());
                    fdTelefone.setText(cliente.getTelefone());
                    fdCpf.setText(cliente.getCpf());
                    fdCartao.setText(cliente.getCartao());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            btCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClienteLogica cl = new ClienteLogica();
                    String erro = "";
                    erro += cl.validaNome(fdNome.getText().toString());
                    erro += cl.validaEmail(fdEmail.getText().toString());
                    erro += cl.validaSenha(fdSenha.getText().toString());
                    erro += cl.validaTelefone(fdTelefone.getText().toString());
                    erro += cl.validaCpf(fdCpf.getText().toString());
                    erro += cl.validaCartao(fdCartao.getText().toString());

                    if (erro.equals("")){
                    builder = new AlertDialog.Builder(Cadastro.this);
                    builder.setTitle("Edição");
                    builder.setMessage("Editando...");
                    dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
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
                    referenciaCliente.child(codificador.codificar(ce.getCliente().getEmail())).setValue(cliente);

                    usuario.updatePassword(fdSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Cadastro.this, "Perfil editado com sucesso", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Cadastro.this, TelaInicial.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.cancel();
                                Toast.makeText(Cadastro.this, "Erro ao editar usuário", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                        Toast.makeText(Cadastro.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });
            Toast.makeText(Cadastro.this, "Perfil em edição", Toast.LENGTH_LONG).show();
        }



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
            Log.i("Excecao", "Entrou no exception");
            Toast.makeText(Cadastro.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
