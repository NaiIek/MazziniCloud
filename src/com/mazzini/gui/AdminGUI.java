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
}