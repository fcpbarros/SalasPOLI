package com.example.android.qrcodescanner;

import java.io.Serializable;

public class Salas implements Serializable {

    //private String chave;
    private String prof;
    private String status;
    private String sala;
    private String materia;

    public Salas(String Prof, String Status, String Sala, String Materia){
        //chave = Chave;
        prof = Prof;
        status = Status;
        sala = Sala;
        materia = Materia;
    }

    public Salas(){}

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getSala(){return sala;}
    //public String getChave(){return chave;}
    public String getProf(){return prof;}
    public String getStatus(){return status;}

    public void setProf(String prof) {
        this.prof = prof;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
