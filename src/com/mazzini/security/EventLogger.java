package com.mazzini.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;  

public class EventLogger{

    private final static Path logFilePath = Paths.get("./logs/ServActivity.log");

    public EventLogger(){
        // ignored
    }

    public static void logEvent(String loggedEvent, int logPriority){
        
        String logTime = getCurrentTime();
        String fullLog = "[" + logTime + "]: " + loggedEvent + "\n";
        try{
            Files.write(logFilePath, fullLog.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
        }
        catch(IOException e){
            System.out.println("ERROR: Log file does not exists");
        }
        if(logPriority == 1){System.out.println(fullLog);}
    }

    private static String getCurrentTime(){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date); 
        return strDate;
    }

    public static String getConnectionEvent(String name){
        return name + " s'est connect\u00E9";
    }

    public static String getBanEvent(String name, boolean becomeBanned){
        if(becomeBanned){
            return name + " devient banni";
        }
        else{
            return name + " devient unban";
        } 
    }

    public static String getAdminEvent(String name, boolean becomeAdmin){
        if(becomeAdmin){
            return name + " devient Admin";
        }
        else{
            return name + " devient Utilisateur";
        }        
    }

    public static String getRegisterEvent(String name){
        return name + " s'est inscrit"; 
    }

    public static String getUnregisterEvent(String name){
        return name + " s'est d\u00E9sinscrit"; 
    }

    public static String getNewFileEvent(int authorid){
        return authorid + " a ajout\u00E9 un fichier";
    }

    public static String getFileEvent(int fileid, String action){
        return "Le fichier " + fileid + " a \u00E9t\u00E9" + action + " par un utilisateur";
    }
}