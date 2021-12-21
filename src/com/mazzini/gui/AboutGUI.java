package com.mazzini.gui;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class AboutGUI {
    public static String getAbout(Boolean log, Boolean adm, String name) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("isLogged", log);
        input.put("isAdmin", adm);
        input.put("username", name);
        Writer output = new StringWriter();
        Template template = configuration.getTemplate("about/about.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
}