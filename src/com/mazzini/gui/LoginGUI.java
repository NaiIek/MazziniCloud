package com.mazzini.gui;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class LoginGUI {

    public static String getLogin() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();
        input.put("isLogged", false);
        input.put("isAdmin", false);
        input.put("username", "Non connecté");
        Writer output = new StringWriter();
        Template template = configuration.getTemplate("login/login.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
    
}