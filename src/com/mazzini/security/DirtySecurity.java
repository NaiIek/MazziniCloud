package com.mazzini.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

public class DirtySecurity{
    public DirtySecurity(){
        // ignored
    }

    public static String encrypt(String password){
        try
        {
            String key = "coronavirus";
            SecretKey myKey = new SecretKeySpec(key.getBytes(),"ARCFOUR");
            Cipher cipher = Cipher.getInstance("ARCFOUR");
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            byte[] enc = cipher.doFinal(password.getBytes());
            return new String(enc);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}