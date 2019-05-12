package com.rightside.fisiowork.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rightside.fisiowork.R;
import com.rightside.fisiowork.helper.ConfiguracaoFirebase;
import com.rightside.fisiowork.model.Cliente;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button btnCadastrar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();


        //metodo on click do botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = editNome.getText().toString();
                String textoEmail = editEmail.getText().toString();
                String textoSenha = editSenha.getText().toString();

                if (!textoNome.isEmpty()) {
                    if(!textoEmail.isEmpty()) {
                        if(!textoSenha.isEmpty()) {

                            Cliente cliente = new Cliente();
                            cliente.setNome(textoNome);
                            cliente.setEmail(textoEmail);
                            cliente.setSenha(textoSenha);
                            cadastrarCliente(cliente);
                        } else {
                            Toast.makeText(CadastroActivity.this, "Senha vazia", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CadastroActivity.this, "Email Vazio", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CadastroActivity.this, "Nome vazio", Toast.LENGTH_SHORT).show();
                }


            }
        });





    }



    //metodo para cadastrar o cliente

    public void cadastrarCliente(final Cliente cliente) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //cadastra no firebase Auth passando campo email e senha do objeto cliente
        autenticacao.createUserWithEmailAndPassword(cliente.getEmail(),cliente.getSenha()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            try {

                                String idCliente = task.getResult().getUser().getUid();
                                //passa o id gerado na autentificação
                                cliente.setId(idCliente);
                                //salva o cliente no Database
                                cliente.salvarUsuarioFirebase();

                                Toast.makeText(CadastroActivity.this, "Cadastro realizado", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            //TRATAMENTO DE POSSIVEIS ERROS DO FIREBASE (SENHA FRACA, EMAIL FRACO, USER REPETIDO)

                            String erro = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e ) {
                                erro = "digite uma senha mais forte";

                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "digite um email valido";

                            } catch (FirebaseAuthUserCollisionException e ) {
                                erro = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                erro = "ao cadastrar Usuario" + e.getMessage();
                                e.printStackTrace();
                            }
                                   //exibe o erro tratado na tela
                                    Toast.makeText(CadastroActivity.this,
                                    "Erro: " + erro ,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

    }




    //metodo que faz conexão dos meus componentes xml com Java

    public void inicializarComponentes() {

        editNome = findViewById(R.id.editCadastroNome);
        editEmail = findViewById(R.id.editCadastroEmail);
        editSenha = findViewById(R.id.editCadastroSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        editNome.requestFocus();
    }




}
