package com.mazzini.gui;

import com.mazzini.core.UserCore;
import com.mazzini.core.FileCore;
import com.mazzini.entity.UserEntity;
import com.mazzini.entity.FileEntity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class AdminGUI {
    public static String getAllUsers(String name) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        ArrayList<UserEntity> users = UserCore.getAllUsers();
        input.put("users", users);
        input.put("isLogged", true);
        input.put("isAdmin", true);
        input.put("username", name);

        long globalStorage = 0L;
        for (UserEntity user : users){
            globalStorage += user.getStorageSize();
        }
        input.put("globalStorage", globalStorage);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("admin/admin.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static String getUserFilesView(Boolean log, Boolean adm, String name, int id)  throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("isLogged", log);
        input.put("isAdmin", adm);
        input.put("username", name);
        input.put("files", FileCore.getAllFiles(id));
        input.put("storageSize", UserCore.getUserStorage(id));

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("admin/admin-view-files.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
}