package com.mazzini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.mazzini.core.*;
import com.mazzini.dao._Initializer;
import com.mazzini.entity.*;
import com.mazzini.gui.*;
import com.mazzini.security.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import javax.servlet.http.*;
import javax.servlet.*;

import com.google.gson.Gson;

import spark.Request;
import static spark.Spark.*;

public class StartServer {

    public static void main(String[] args) {
        //Configure Spark
        String keyStoreLocation = "deploy/letsencrypt.jks";
        String keyStorePassword = "mysuperpassword";
        
        staticFiles.location("/static/");
        port(8081);
        secure(keyStoreLocation, keyStorePassword, null, null);
        _Initializer.Init();

        /********************************/
        /*          Requêtes UI         */
        /*  firefox && chrome approved  */
        /********************************/

        //Requête acceuil
        get("/", (req, res) -> {
            String n = "Non connect\u00e9";
            if(doLogin.isLogged(req.cookie("auth"))){
                n = doLogin.getLoggedName(req.cookie("auth"));
                int id = Integer.parseInt(doLogin.introspect(req.cookie("auth")).get("id"));
                if(CleanSecurity.isAdmin(req.cookie("auth"))){
                    res.status(200);
                    return HomeGUI.getHome(true, true, n, id);
                }
                else{
                    res.status(200);
                    return HomeGUI.getHome(true, false, n, id);
                }
            }
            else{
                res.status(200);                
                return HomeGUI.getHome(false, false, n, -1);
            }
        });

        //Requête page a propos        
        get("/sujet", (req, res) -> {
            String n = "Non connect\u00e9";
            if(doLogin.isLogged(req.cookie("auth"))){
                n = doLogin.getLoggedName(req.cookie("auth"));
                if(CleanSecurity.isAdmin(req.cookie("auth"))){
                    res.status(200);
                    return AboutGUI.getAbout(true, true, n);
                }
                else{
                    res.status(200);
                    return AboutGUI.getAbout(true, false, n);
                }
            }
            else{
                res.status(200);
                return AboutGUI.getAbout(false, false, n);
            }
        });

        //Requête page inscription        
        get("/register", (req, res) -> {
            res.status(200);
            return RegisterGUI.getRegister();
        });

        //Requête page connexion
        get("/login", (req, res) -> {
            res.status(200);
            return LoginGUI.getLogin();
        });

        //Requête page admin
        get("/admin", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(CleanSecurity.isAdmin(req.cookie("auth"))){
                    String n = doLogin.getLoggedName(req.cookie("auth"));
                    res.status(200);
                    return AdminGUI.getAllUsers(n);
                }
                else{
                    res.status(404);
                    return "Error <404>";
                }
            }
            else{
                res.status(404);                
                return "Error <404>";
            }
        });

        get("/troll", (req, res) ->{
            res.status(200);
            return "T'es B A N !";
        });

        //Requête inscription              
        post("/register/try", (req, res) ->{           
            UserEntity user = new UserEntity();
            String n = req.queryParams("name");
            if(CleanSecurity.existN(n)){
                res.status(400);
                return "Le nom est d\u00e9jà utilis\u00e9";
            }
            else{
                String e = req.queryParams("email");
                if(CleanSecurity.existM(e)){
                    res.status(400);
                    return "Le mail est d\u00e9jà utilis\u00e9";
                }
                else{
                    String p = DirtySecurity.encrypt(req.queryParams("password")); 
                    String r = convertToJsonReg(n,e,p);
                    user = new Gson().fromJson(r, UserEntity.class);            
                    UserCore.create(user);
                    res.redirect("/login", 301);          
                    return "Creation du compte utilisateur r\u00e9alis\u00e9 avec succès";
                }
            }
        });  

        //Requête connexion                    
        post("/login/try", (req, res) ->{
            Gson gson = new Gson();
            UserEntity user = new UserEntity();
            String n = req.queryParams("name");
            String p = DirtySecurity.encrypt(req.queryParams("password"));
            String r = convertToJsonLog(n,p);
            user = gson.fromJson(r, UserEntity.class);
            r = doLogin.tryLogin(user);
            res.cookie("/","auth",r,3600,true,true);
            res.redirect("/", 301);
            return "Essai de Login effectu\u00e9";
        });     

        // Requête création file
        post("/addfile", "multipart/form-data", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(CleanSecurity.isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas upload de fichier si t'es ban";
                }
                else{
                    Map<String,String> user = doLogin.introspect(req.cookie("auth"));
                    int authorid = Integer.parseInt(user.get("id"));
                    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    String n = req.raw().getPart("uploaded_file").getSubmittedFileName();
                    InputStream input = req.raw().getPart("uploaded_file").getInputStream();
                    long inputSize = req.raw().getPart("uploaded_file").getSize();
                    FileCore.create(authorid, n, input, inputSize);
                    res.status(201);
                    res.redirect("/", 301);
                    return "Tentative upload effectue";
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas upload de fichier si t'es pas connect\u00e9";
            }
        });

        // Requête supression file
        post("/delfile/:id", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(CleanSecurity.isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas supprimer de fichier si t'es ban";
                }
                else{
                    int fileid = Integer.parseInt(req.params(":id"));
                    if(CleanSecurity.isOwner(req.cookie("auth"),fileid) || CleanSecurity.isAdmin(req.cookie("auth"))){
                        FileCore.delete(fileid);
                        res.status(201);
                        res.redirect("/", 301);
                        return "Tentative suppression effectue";
                    }
                    else{
                        res.status(401);
                        return "Tu ne peux pas supprimer un fichier qui ne t'appartiens pas";
                    }                    
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas supprimer de fichier si t'es pas connect\u00e9";
            }
        });

        // Requête rename file
        post("/rnmfile/:id", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(CleanSecurity.isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas renommer de fichier si t'es ban";
                }
                else{
                    int fileid = Integer.parseInt(req.params(":id"));
                    String newname = req.queryParams("rename");
                    if(CleanSecurity.isOwner(req.cookie("auth"),fileid) || CleanSecurity.isAdmin(req.cookie("auth"))){
                        FileCore.rename(fileid,newname);
                        res.status(201);
                        res.redirect("/", 301);
                        return "Tentative renommage effectue";
                    }
                    else{
                        res.status(401);
                        return "Tu ne peux pas renommer un fichier qui ne t'appartiens pas";
                    }                    
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas renommer de fichier si t'es pas connect\u00e9";
            }
        });

        // Requête download file
        get("/dwlfile/:id", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(CleanSecurity.isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas t\u00e9l\u00e9charger de fichier si t'es ban";
                }
                else{
                    int fileid = Integer.parseInt(req.params(":id"));
                    if(CleanSecurity.isOwner(req.cookie("auth"),fileid) || CleanSecurity.isAdmin(req.cookie("auth"))){
                        FileEntity f = FileCore.download(fileid);
                        res.raw().setContentType("application/octet-stream");
                        res.raw().setHeader("Content-Disposition","attachment; filename="+f.getFileName());
                        try {
                            try(OutputStream output = new BufferedOutputStream(res.raw().getOutputStream());
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(f.getFileData()))
                            {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = bufferedInputStream.read(buffer)) > 0) {
                                    output.write(buffer,0,len);
                                }
                            }
                        } catch (Exception e) {
                            halt(405,"server error");
                        }
                        res.status(201);
                        res.redirect("/", 301);
                        return "Tentative download effectue";
                    }
                    else{
                        res.status(401);
                        return "Tu ne peux pas t\u00e9l\u00e9charger un fichier qui ne t'appartiens pas";
                    }                    
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas t\u00e9l\u00e9charger de fichier si t'es pas connect\u00e9";
            }
        });

        // Requête ban user
        post("/ban/:id", (req, res) -> {
            if(CleanSecurity.isAdmin(req.cookie("auth"))){
                res.type("application/json");
                UserCore.ban(req.params(":id"));
                res.redirect("/admin", 301);
                return "User Banned";
            }
            else{
                res.status(401);
                return "Tu ne peut pas ban un utilisateur si tu n'es pas admin";
            }
        });        

        // Requête unban user 
        post("/unban/:id", (req, res) -> {
            if(CleanSecurity.isAdmin(req.cookie("auth"))){
                res.type("application/json");
                UserCore.unban(req.params(":id"));
                res.redirect("/admin", 301);
                return "User Unbanned";
            }
            else{
                res.status(401);
                return "Tu ne peut pas unban un utilisateur si tu n'es pas admin";
            }
        });

        // Requête déconnexion
        get("/logout", (req, res) -> {
            res.removeCookie("auth");
            res.redirect("/", 301);
            return "";
        });

        // Tu veux un café?
        get("/coffee", (req, res) -> {
            res.status(418);
            return "Error <418> I'm a teapot!";
        });

        //Requête certificat
        //get("/.well-known/acme-challenge/wSlYu4CjvHfRYDS46LCUPICkzCXqvV1dbB5OiJsgwnc", (req, res) -> {
        //    return "wSlYu4CjvHfRYDS46LCUPICkzCXqvV1dbB5OiJsgwnc.c0PEIAgCANLJPrnK-oa6FYrt2U9floeMtGTuy-utW3A";
        //});

        /********************************/
        /*          Requêtes API        */
        /*        Postman approved      */
        /********************************/

        // Sans negociation de contenu, le body doit être en json

        // login par api
        // nécessite de copier le header Authorization de la réponse et l'ajouter aux headers des request
        /*
        post("/api/login", (req, res) -> {
            UserEntity user = new UserEntity();
            if(req.headers("Content-Type").equals("application/json")) {          
                user = new Gson().fromJson(req.body(), UserEntity.class);
                user.setPwd(DirtySecurity.encrypt(user.getPwd()));
            }
            else{
                res.status(406);
                return "";
            }
            String r = doLogin.tryLogin(user);
            if (r == "Une information d'authentification est erronée ou l'utilisateur n'existe pas, veuillez réessayer"){
                res.status(400);
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, r));
            }
            else{
                res.header("Authorization",r);
                res.status(200);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, r));
            }
        });
        */

        // Requête ban user
        /*
        put("/api/ban/:id", (req, res) -> {
            if(CleanSecurity.isAdmin(req.cookie("auth"))){
                res.type("application/json");
                UserCore.ban(req.params(":id"));
                res.redirect("/admin", 301);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user banned"));
            }
            else{
                res.status(401);
                return "Tu ne peut pas ban un utilisateur si tu n'es pas admin";
            }
        });
        */

        // Requête unban user
        /*
        put("/api/unban/:id", (req, res) -> {
            if(CleanSecurity.isAdmin(req.cookie("auth"))){
                res.type("application/json");
                UserCore.unban(req.params(":id"));
                res.redirect("/admin", 301);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user unbanned"));
            }
            else{
                res.status(401);
                return "Tu ne peut pas unban un utilisateur si tu n'es pas admin";
            }
        });
        */

        // Requête déconnexion api
        /* 
        get("/api/logout", (req, res) -> {
            res.header("Authorization", "");
            return "";
        });*/
        
    }
    
    private static Boolean useXML(Request req) {
        if (req.headers("Accept") != null && !req.headers("Accept").isEmpty() && !req.headers("Accept").equals("*/*")) {
            int json = req.headers("Accept").indexOf("application/json");
            int xml = req.headers("Accept").indexOf("application/xml");
            if (json == -1 && xml == -1) {
                return null;
            }
            if (xml == -1) {
                return false;
            } else {
                return json == -1 || json >= xml;
            }
        }
        return true;
    }

    private static String parseContent(boolean useXML, Object obj) throws JsonProcessingException {
        if (obj.getClass().getName().equals("java.util.ArrayList")) {
            Map<String, Object> map = new HashMap<>();
            map.put("content", obj);
            obj = map;
        }

        if (useXML) {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(obj);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static <T> T parseBody(Request req, Class<T> expectedClass) throws JsonProcessingException {
        if (req.headers("Content-Type") != null){
            if(req.headers("Content-Type").equals("application/json")) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(req.body(), expectedClass);
            }
            else if (req.headers("Content-Type").equals("application/xml")) {
                XmlMapper mapper = new XmlMapper();
                return mapper.readValue(req.body(), expectedClass);
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
    
    private static String convertToJsonReg(String name, String email, String password){
        String res = "{'name' : '" + name + "', 'email' : '" + email + "', 'password' : '" + password + "'}";
        return res;
    }
    
    private static String convertToJsonLog(String name, String password){
        String res = "{'name' : '" + name + "', 'password' : '" + password + "'}";
        return res;
    }
}