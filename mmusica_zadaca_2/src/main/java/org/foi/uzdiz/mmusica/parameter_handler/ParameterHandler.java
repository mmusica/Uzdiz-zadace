package org.foi.uzdiz.mmusica.parameter_handler;

import java.util.Enumeration;
import java.util.Properties;

public class ParameterHandler {
    public ParameterHandler() {
    }

    public String handleParameters(Properties properties){
        String oldPropertiesString = getOldProperties(properties);
        return oldPropertiesString;
    }

    private String getOldProperties(Properties properties) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement().toString();
            String oldPropertyString = getOldPropertyString(key, properties);
            stringBuilder.append(oldPropertyString);
        }

        return stringBuilder.toString().trim();
    }
    private String getOldPropertyString(String key, Properties properties){
        StringBuilder stringBuilder = new StringBuilder();
        switch (key){
            case "po", "pmu", "pm", "pu", "gps", "isporuka":
                break;
            default:
                stringBuilder.append(" --");
                stringBuilder.append(key).append(" ").append(properties.get(key));
                break;
        }
        return stringBuilder.toString();
    }
}
