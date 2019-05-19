package com.rightside.fisiowork.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.rightside.fisiowork.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Cliente {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String urlFoto;

    //construtor do cliente, obrigando ter um id nome email senah e url para o caminho da foto

    public Cliente() {

    }

    //metodo para salvar o Objeto usuario no database do firebase

    public void salvarUsuarioFirebase() {

        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        DatabaseReference clientesRef = databaseReference.child("clientes").child( getId() ); //referencia meus clientes no database

        clientesRef.setValue(this); //salva o usuario criado

    }

    //metodo para retornar o usuario Logado

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public Map<String, Object> converterMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", getId());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("email",getEmail());
        usuarioMap.put("UrlFoto", getUrlFoto());

        return usuarioMap;
    }

    //getters e setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
