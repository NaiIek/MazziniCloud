package com.mazzini.security;

import com.mazzini.security.doLogin;
import com.mazzini.dao.UserDAO;
import com.mazzini.dao.FileDAO;

import java.util.Map;

public class CleanSecurity {
    public CleanSecurity(){
        // ignored
    }

    public static Boolean isAdmin(String token){
        Map<String,String> user = doLogin.introspect(token);
        String id = user.get("id");
        int identity = Integer.parseInt(id);
        return new UserDAO().isAdmin(identity);
    }

    public static Boolean isBanned(String token){
        int identity;
        // Savoir si un utilisateur est ban depuis ses cookies
        if(doLogin.isLogged(token)){
            Map<String,String> user = doLogin.introspect(token);
            String id = user.get("id");
            identity = Integer.parseInt(id);
        }// Savoir si un utilisateur est ban depuis la page admin par son id
        else{
            identity = Integer.parseInt(token);
        }
        return new UserDAO().isBanned(identity);
    }

    public static Boolean existN(String name){
        return new UserDAO().existN(name);
    }

    public static Boolean existM(String mail){
        return new UserDAO().existM(mail);
    }

    public static Boolean isOwner(String token, int fileId){
        Map<String,String> user = doLogin.introspect(token);
        int authorid = Integer.parseInt(user.get("id"));
        return new FileDAO().isOwner(authorid, fileId);
    }
}
