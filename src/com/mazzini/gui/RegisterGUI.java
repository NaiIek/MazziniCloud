package com.mazzini.gui;

import com.mazzini.core.UserCore;
import com.mazzini.entity.UserEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterGUI {

    public static String getRegister() throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();
        input.put("isLogged", false);
        input.put("isAdmin", false);
        input.put("username", "Non Connecté");
        Writer output = new StringWriter();
        Template template = configuration.getTemplate("login/register.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }

    public static ArrayList<UserEntity> getAllUsers() {
        return new UserCore().getAllUsers();
    } 
}