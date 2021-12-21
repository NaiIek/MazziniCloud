package com.mazzini.entity;

public class UserEntity {
    private String name;
    private String email;
    private String password;
    private int id;
    private int isAdmin;
    private int isBanned;
    
    public UserEntity(){
        //Ignored
    }

    public UserEntity(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPwd(){
        return password;
    }

    public void setPwd(String password){
        this.password = password;
    }

    public int getIsAdmin(){
        return isAdmin;
    }

    public void setAdmin(int a){
        this.isAdmin = a;
    }

    public int getIsBanned(){
        return isBanned;
    }

    public void setBanned(int b){
        this.isBanned = b;
    }

    public String toString(){
        return "l'entité utilisateur est: " + this.name + ", " + this.email;
    }
}