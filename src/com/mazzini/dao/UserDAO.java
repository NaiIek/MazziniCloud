package com.mazzini.dao;

import com.mazzini.entity.UserEntity;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO extends _Generic<UserEntity> {


    public ArrayList<UserEntity> getAllUsers() {
        ArrayList<UserEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users ORDER BY id DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserEntity entity = new UserEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setEmail(resultSet.getString("email"));
                entity.setPwd(resultSet.getString("pwd"));
                entity.setStorageSize(resultSet.getLong("storageSize"));
                entity.setAdmin(resultSet.getInt("isAdmin"));
                entity.setBanned(resultSet.getInt("isBanned"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    public UserEntity GetUserByLog(UserEntity u){
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users WHERE name = ? AND pwd = ?;");
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getPwd());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setEmail(resultSet.getString("email"));
                entity.setPwd(resultSet.getString("pwd"));
                entity.setStorageSize(resultSet.getLong("storageSize"));
                entity.setAdmin(resultSet.getInt("isAdmin"));
                entity.setBanned(resultSet.getInt("isBanned"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public UserEntity GetUserById(int u){
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users WHERE id = ?;");
            preparedStatement.setInt(1, u);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity.setId(resultSet.getInt("id"));
                entity.setName(resultSet.getString("name"));
                entity.setEmail(resultSet.getString("email"));
                entity.setPwd(resultSet.getString("pwd"));
                entity.setStorageSize(resultSet.getLong("storageSize"));
                entity.setAdmin(resultSet.getInt("isAdmin"));
                entity.setBanned(resultSet.getInt("isBanned"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }
    
    public Boolean isAdmin(int u){
        UserEntity e = GetUserById(u);
        if(e.getIsAdmin() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isBanned(int u){
        UserEntity e = GetUserById(u);
        if(e.getIsBanned() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean existN(String n){
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users WHERE name = ?;");
            preparedStatement.setString(1, n);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity.setName(resultSet.getString("name"));
                entity.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(entity.getName() != null){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean existM(String m){
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users WHERE email = ?;");
            preparedStatement.setString(1, m);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entity.setName(resultSet.getString("name"));
                entity.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(entity.getEmail() != null){
            return true;
        }
        else{
            return false;
        }
    }

    public void ban(int u){
        try{
            PreparedStatement preparedStatement = this.connect.prepareStatement("UPDATE users SET isBanned = 1 WHERE id = ?;");
            preparedStatement.setInt(1, u);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unban(int u){
        try{
            PreparedStatement preparedStatement = this.connect.prepareStatement("UPDATE users SET isBanned = 0 WHERE id = ?;");
            preparedStatement.setInt(1, u);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getUserSize(int u){
        long size = -1L;
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT storageSize FROM users WHERE id = ?;");
            preparedStatement.setInt(1, u);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                size = resultSet.getLong("storageSize");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void addData(int u, long size){
        long old_size = getUserSize(u);
        if(old_size < 0){
            System.out.println("Something went wrong with a user storage");
        }
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("UPDATE users SET storageSize = ? WHERE id = ?;");
            preparedStatement.setLong(1, old_size + size);
            preparedStatement.setInt(2, u);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeData(int u, long size){
        long new_size = getUserSize(u) - size;
        if(new_size < 0){
            System.out.println("Something went wrong with a user storage");
        }
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("UPDATE users SET storageSize = ? WHERE id = ?;");
            preparedStatement.setLong(1, new_size);
            preparedStatement.setInt(2, u);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserEntity create(UserEntity obj) {
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("INSERT INTO users(name, email, pwd, storageSize, isAdmin, isBanned) " + "VALUES (?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setString(3, obj.getPwd());
            preparedStatement.setLong(4, obj.getStorageSize());
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return null;
    }

    @Override
    public void delete(UserEntity obj) {
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("DELETE FROM users WHERE id = ?;");
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }        
    }
}