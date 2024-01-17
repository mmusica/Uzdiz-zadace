package org.foi.uzdiz.mmusica.parameter_handler;

import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.Enumeration;
import java.util.Properties;

public class ParameterHandler {
    public ParameterHandler() {
    }

    public String getOldProperties(Properties properties) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement().toString();
            String oldPropertyString = getOldPropertiesString(key, properties);
            stringBuilder.append(oldPropertyString);
        }
        return stringBuilder.toString().trim();
    }
    public void setNewProperties(Properties properties) {
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement().toString();
            setNewProperties(key, properties);
        }
    }
    private String getOldPropertiesString(String key, Properties properties){
        StringBuilder stringBuilder = new StringBuilder();
        switch (key){
            case "po", "pmu", "pm", "pu", "gps", "isporuka":
                break;
            case "vp", "pv", "pp","mt", "vi", "vs", "ms", "pr", "kr":
                stringBuilder.append(" --");
                stringBuilder.append(key).append(" ").append(properties.get(key));
                break;
            default:
                throw new RuntimeException("Nepoznati argument");
        }
        return stringBuilder.toString();
    }
    public void setNewProperties(String key, Properties properties){
        switch (key){
            case "po", "pmu", "pm", "pu", "gps", "isporuka":
                TerminalCommandHandler.getInstance().getNewProperties().setProperty(key,properties.get(key).toString());
                break;
            default:
                break;
        }
    }

    public void handleProperties(Properties properties) {
        TerminalCommandHandler.getInstance().setCommandData(this.getOldProperties(properties));
        this.setNewProperties(properties);
    }
}
