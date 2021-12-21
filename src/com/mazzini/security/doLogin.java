package com.mazzini.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.mazzini.entity.UserEntity;
import com.mazzini.dao.UserDAO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class doLogin {
    
    private final static String TOKEN = "Fiuzghv48AZdE9bfdjpp7RftG4A924cvGFPmjiH826Zv7QcoBiKp0pR6Sxef31n7eLl1gEn7C0r0N4v1rU5c0nn4rDD3v1Ru5uvS";

    public static Map<String, String> introspect(String token){
        Claims claims = Jwts.parser().setSigningKey(TOKEN).parseClaimsJws(token).getBody();
        Map<String, String> map = new HashMap<>();
        map.put("sub", claims.get("sub", String.class));
        map.put("email", claims.get("email", String.class));
        map.put("id", claims.get("id", String.class));
        return map;
    }

    public static String createToken(String name, String email, int i){
        Map<String, String> content = new HashMap<>();
        String id = "" + i;
        content.put("sub", name);
        content.put("email", email);
        content.put("id", id);

        return Jwts.builder().setClaims(content)
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .signWith(SignatureAlgorithm.HS256, TOKEN).compact();      
    }

    public static String tryLogin(UserEntity u){
        UserEntity l = new UserDAO().GetUserByLog(u);
        if(l.getEmail() != null){
            return createToken(l.getName(),l.getEmail(),l.getId());
        }
        else{
            return "Une information d'authentification est erronée ou l'utilisateur n'existe pas, veuillez réessayer";
        }
    }

    public static Boolean isLogged(String t){
        if(t == null){
            return false;
        }
        else if(introspect(t) != null){
            return true;
        }
        else{
            return false;
        }
    }

    public static String getLoggedName(String t){
        return introspect(t).get("sub");
    }
}