package com.mazzini.gui;

import com.mazzini.core.UserCore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.mazzini.core.FileCore;
import com.mazzini.entity.FileEntity;

public class AdminGUI {
    public static String getAllUsers(String name) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("users", UserCore.getAllUsers());
        input.put("isLogged", true);
        input.put("isAdmin", true);
        input.put("username", name);
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

        ArrayList<FileEntity> entities = FileCore.getAllFiles(id);
        input.put("files", entities);

        Long storageSize = FileCore.getAllFilesSize(entities);
        input.put("storageSize", storageSize);

        Writer output = new StringWriter();
        Template template = configuration.getTemplate("admin/admin-view-files.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
}