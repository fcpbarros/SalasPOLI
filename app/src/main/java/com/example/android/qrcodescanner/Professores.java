package com.example.android.qrcodescanner;

public class Professores {

    private String nome;
    private String entrada;
    private String saida;
    private String sala;

    public Professores() {
    }

    public Professores(String nome, String entrada, String saida, String sala) {
        this.nome = nome;
        this.entrada = entrada;
        this.saida = saida;
        this.sala = sala;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }
}
