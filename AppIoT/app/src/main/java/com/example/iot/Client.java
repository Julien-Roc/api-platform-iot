package com.example.iot;

public class Client {
    String nom;
    String code;
    int credit;

    public Client(){

    }

    public Client(String nom, String code, int credit){
        this.nom = nom;
        this.code = code;
        this.credit = credit;
    }

    public  void setNom(String nom){
        this.nom = nom;
    }

    public  void setCode(String code){
        this.code = code;
    }

    public void setCredit(int credit){
        this.credit = credit;
    }

    public String getNom(){
        return this.nom;
    }

    public String getCode(){
        return this.code;
    }

    public int getCredit() {
        return this.credit;
    }
}
