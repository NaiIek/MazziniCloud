package com.mazzini.dao;

import java.sql.*;
import com.mazzini.security.DirtySecurity;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            // Détruire les tables
            /*
            statement = connection.prepareStatement("DROP TABLE IF EXISTS users; ");
            statement.executeUpdate();
            System.out.println("Oublie pas de recommenter le drop table");
            
            
            statement = connection.prepareStatement("DROP TABLE IF EXISTS attachment; ");
            statement.executeUpdate();
            System.out.println("Oublie pas de recommenter le drop table");
            */

            //Init users table
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id int primary key auto_increment, name varchar(100), email varchar(100), pwd varchar(100), storageSize long, isAdmin int, isBanned int); ");
            statement.executeUpdate();
            
            //Init attachment files Table (ajouter potentiel feature de path)
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS attachment (id int primary key auto_increment, authorid int, filename varchar(260), filedata BLOB, filepath varchar(260), storageSize long);");
            statement.executeUpdate();

            // initialisation d'un utilisateur admin (décommenter pour créer un utilisateur en lançant le serv puis recommenter)
            /*
            String pwd = DirtySecurity.encrypt("YourAdminPwd");
            statement = connection.prepareStatement("INSERT INTO users(name, email, pwd, isAdmin) VALUES(?,?,?,?);");
            statement.setString(1, "YourAdminUsername");
            statement.setString(2, "YourName@mazzland.fr");
            statement.setString(3, pwd);
            statement.setInt(4, 1);
            statement.executeUpdate();
            */

            System.out.println("Initialisation Database termin\u00E9e");
        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
