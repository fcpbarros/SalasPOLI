package com.example.android.qrcodescanner;

public class Avisos {

    private String professor;
    private String mensagem;

    public Avisos(String professor, String mensagem) {
        this.professor = professor;
        this.mensagem = mensagem;
    }

    public Avisos() {
    }

    public String getProfessor() {
        return professor;
    }

    public String getMensagem() {
        return mensagem;
    }

}
