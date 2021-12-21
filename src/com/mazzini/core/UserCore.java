package com.mazzini.core;

import com.mazzini.dao.UserDAO;
import com.mazzini.entity.UserEntity;

import java.util.ArrayList;

public class UserCore {

    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDAO().getAllUsers();
    }

    public static UserEntity create(UserEntity u){
        return new UserDAO().create(u);
    }

    public static void ban(String id){
        int i = Integer.parseInt(id);
        new UserDAO().ban(i);
    }

    public static void unban(String id){
        int i = Integer.parseInt(id);
        new UserDAO().unban(i);
    }
}
