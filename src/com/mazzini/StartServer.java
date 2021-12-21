package com.mazzini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.mazzini.core.*;
import com.mazzini.dao.*;
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
        staticFiles.location("/static/");
        port(8081);
        _Initializer.Init();

        /********************************/
        /*          Requêtes UI         */
        /*        opti sur firefox      */
        /********************************/

        //Requête acceuil
        get("/", (req, res) -> {
            String n = "Non connecté";
            if(doLogin.isLogged(req.cookie("auth"))){
                n = doLogin.getLoggedName(req.cookie("auth"));
                int id = Integer.parseInt(doLogin.introspect(req.cookie("auth")).get("id"));
                if(isAdmin(req.cookie("auth"))){
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
            String n = "Non connecté";
            if(doLogin.isLogged(req.cookie("auth"))){
                n = doLogin.getLoggedName(req.cookie("auth"));
                if(isAdmin(req.cookie("auth"))){
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
                if(isAdmin(req.cookie("auth"))){
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
            if(existN(n)){
                res.status(400);
                return "Le nom est déjà utilisé";
            }
            else{
                String e = req.queryParams("email");
                if(existM(e)){
                    res.status(400);
                    return "Le mail est déjà utilisé";
                }
                else{
                    String p = DirtySecurity.encrypt(req.queryParams("password")); 
                    String r = convertToJsonReg(n,e,p);
                    user = new Gson().fromJson(r, UserEntity.class);            
                    UserCore.create(user);
                    res.redirect("/login", 301);          
                    return "Creation du compte utilisateur réalisé avec succès";
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
            return "Essai de Login effectué";
        });     

        // Requête création file
        post("/addfile", "multipart/form-data", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas upload de fichier si t'es ban";
                }
                else{
                    Map<String,String> user = doLogin.introspect(req.cookie("auth"));
                    int authorid = Integer.parseInt(user.get("id"));
                    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    String n = req.raw().getPart("uploaded_file").getSubmittedFileName();
                    InputStream input = req.raw().getPart("uploaded_file").getInputStream();
                    FileCore.create(authorid, n, input);
                    res.status(201);
                    res.redirect("/", 301);
                    return "Tentative upload effectue";
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas upload de fichier si t'es pas connecté";
            }
        });

        // Requête supression file
        post("/delfile/:id", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas supprimer de fichier si t'es ban";
                }
                else{
                    int fileid = Integer.parseInt(req.params(":id"));
                    if(isOwner(req.cookie("auth"),fileid) || isAdmin(req.cookie("auth"))){
                        FileCore.delete(fileid);
                        res.status(201);
                        res.redirect("/", 301);
                        return "Tentative delete effectue";
                    }
                    else{
                        res.status(401);
                        return "Tu ne peux pas supprimer un fichier qui ne t'appartiens pas";
                    }                    
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas supprimer de fichier si t'es pas connecté";
            }
        });

        // Requête download file
        get("/dwlfile/:id", (req, res) -> {
            if(doLogin.isLogged(req.cookie("auth"))){
                if(isBanned(req.cookie("auth"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas télécharger de fichier si t'es ban";
                }
                else{
                    int fileid = Integer.parseInt(req.params(":id"));
                    if(isOwner(req.cookie("auth"),fileid) || isAdmin(req.cookie("auth"))){
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
                        return "Tu ne peux pas télécharger un fichier qui ne t'appartiens pas";
                    }                    
                }
            }
            else{
                res.status(401);
                return "Tu ne peux pas télécharger de fichier si t'es pas connecté";
            }
        });

        // Requête ban user
        post("/ban/:id", (req, res) -> {
            if(isAdmin(req.cookie("auth"))){
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
            if(isAdmin(req.cookie("auth"))){
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

        /********************************/
        /*          Requêtes API        */
        /*        opti sur firefox      */
        /********************************/

        // tous les articles
        /*
        get("/api/articles", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }

            ArrayList<ArticleEntity> entities = ArticleCore.getAllArticles();
            if (entities == null || entities.size() == 0) {
                res.status(204);
                return "";
            }

            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entities);
        });
        */

        // articles par id
        /*
        get("/api/articles/:id", (req, res) -> {
            Boolean useXML = useXML(req);
            if (useXML == null) {
                res.status(406);
                return "";
            }        
            ArticleEntity entity = ArticleCore.getArticleById(req.params(":id"));
            if (entity == null) {
                res.status(204);
                return "";
            }        
            res.header("Content-Type", useXML ? "application/xml" : "application/json");
            return parseContent(useXML, entity);
        });
        */

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

        // créer un article par api
        /*
        post("/api/add", (req, res) -> {
            if(doLogin.isLogged(req.headers("Authorization"))){
                if(isBanned(req.headers("Authorization"))){
                    res.redirect("/troll", 301);
                    return "Tu ne peux pas ecrire d'article en étant ban";
                }
                else{
                    ArticleEntity art = new ArticleEntity();
                    if(req.headers("Content-Type").equals("application/json")) {
                        art = new Gson().fromJson(req.body(), ArticleEntity.class);
                    }
                    else{
                        res.status(406);
                        return "";
                    }
                    Map<String,String> user = doLogin.introspect(req.headers("Authorization"));
                    String auteur = user.get("sub");
                    art.setAuthor(auteur);                    
                    ArticleCore.create(art);
                    res.status(201);
                    return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "article created"));
                }
            }
            else{
                res.status(401);
                return "Tu peux pas ecrire un article si t'es pas connecté";
            }
        });
        */
        // modification d'un article (ici on peut modif uniquement le contenu)
        /*
        put("api/edit/:id/content", (req, res) -> {
            if(ArticleCore.existArt(req.params(":id"))){
                if(isAdmin(req.cookie("auth"))){
                    res.type("application/json");
                    ArticleCore.edit(req.params(":id"), req.queryParams("content"));
                    res.redirect("/", 301);
                    return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "article edited"));
                }
                else{
                    res.status(401);
                    return "Tu ne peux pas editer un article si t'es pas admin";
                }
            }
            else{
                res.status(404);
                return "Tu ne peux pas editer un article qui existe pas";
            }
        });
        */
        // modification d'un article (ici on modif le titre et le contenu)
        /*
        put("/api/edit/:id", (req, res) -> {
            if(ArticleCore.existArt(req.params(":id"))){
                if(isAdmin(req.headers("Authorization"))){
                    if(req.headers("Content-Type").equals("application/json")) {
                        ArticleEntity art = new ArticleEntity();
                        art = new Gson().fromJson(req.body(), ArticleEntity.class);
                        String name = art.getName();
                        String content = art.getContent();
                        ArticleCore.edit(req.params(":id"), name, content);
                    }
                    else{
                        res.status(406);
                        return "";
                    }
                    res.type("application/json");
                    return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "article edited"));
                }
                else{
                    res.status(401);
                    return "Tu ne peux pas editer un article si t'es pas admin";
                }
            }
            else{
                res.status(404);
                return "Tu ne peux pas editer un article qui existe pas";
            }
        });
        */

        // suppression d'un article par api
        /*
        delete("/api/del/:id", (req, res) -> {
            if(ArticleCore.existArt(req.params(":id"))){
                if(isAdmin(req.headers("Authorization"))){
                    res.type("application/json");
                    ArticleCore.delete(req.params(":id"));
                    return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "article deleted"));
                }
                else{
                    res.status(401);
                    return "Tu ne peux pas supprimer un article si t'es pas admin";
                }
            }
            else{
                res.status(404);
                return "Tu ne peux pas supprimer un article qui existe pas";
            }
        });
        */

        // suppression d'un commentaire d'article par api
        /*
        delete("/api/del/com/:artid/:id", (req, res) -> {
            if(CommentCore.existCom(req.params(":id"))){
                if(isAdmin(req.cookie("auth"))){
                    res.type("application/json");
                    CommentCore.delete(req.params(":id"));
                    String red = "/article/" + req.params(":artid");
                    res.redirect(red, 301);
                    return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "article deleted"));
                }
                else{
                    res.status(401);
                    return "Tu ne peux pas supprimer un article si t'es pas admin";
                }
            }
            else{
                res.status(404);
                return "Tu ne peux pas supprimer un commentaire qui existe pas";
            }
        });
        */

        // Requête ban user
        /*
        put("/api/ban/:id", (req, res) -> {
            if(isAdmin(req.cookie("auth"))){
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
            if(isAdmin(req.cookie("auth"))){
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

    // Fonctions suivantes a faire dans un fichier dedié à la sécurité ou un Core
    private static Boolean isAdmin(String token){
        Map<String,String> user = doLogin.introspect(token);
        String id = user.get("id");
        int identity = Integer.parseInt(id);
        return new UserDAO().isAdmin(identity);
    }

    private static Boolean isBanned(String token){
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

    private static Boolean existN(String name){
        return new UserDAO().existN(name);
    }

    private static Boolean existM(String mail){
        return new UserDAO().existM(mail);
    }

    private static Boolean isOwner(String token, int fileId){
        Map<String,String> user = doLogin.introspect(token);
        int authorid = Integer.parseInt(user.get("id"));
        return new FileDAO().isOwner(authorid, fileId);
    }
    
}