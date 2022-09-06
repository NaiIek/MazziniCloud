package com.mazzini.core;

import com.mazzini.dao.UserDAO;
import com.mazzini.entity.UserEntity;
import com.mazzini.core.FileCore;
import com.mazzini.dao.FileDAO;
import com.mazzini.entity.FileEntity;

import java.util.ArrayList;

public class UserCore {

    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDAO().getAllUsers();
    }

    public static UserEntity create(UserEntity u){
        return new UserDAO().create(u);
    }

    public static void delete(UserEntity u){
        ArrayList<FileEntity> entities = FileCore.getAllFiles(u.getId());
        for (FileEntity file : entities){
            new FileDAO().delete(file);
        }
        new UserDAO().delete(u);
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
