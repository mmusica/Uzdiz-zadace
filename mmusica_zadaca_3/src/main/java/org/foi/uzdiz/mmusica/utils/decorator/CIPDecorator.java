package org.foi.uzdiz.mmusica.utils.decorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CIPDecorator extends IPDecorator {

    IPOutputer ipOutputer;
    //Izvor https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";     // RED
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE

    public CIPDecorator(IPOutputer ipOutputer) {
        this.ipOutputer = ipOutputer;
    }

    @Override
    public String outputIPCommand() {
        StringBuilder sb = new StringBuilder();
        String[] arr = ipOutputer.outputIPCommand().split("\n");
        for (String s : arr) {
            if (s.contains("Preuzet")) {
                s = BLUE + s;
            } else if (s.contains("Ukrcan u vozilo")) {
                s = YELLOW + s;
            } else if (s.contains("Dostavljen")) {
                s = GREEN + s;
            } else if (s.contains("Trenutno u isporuci")) {
                s = PURPLE + s;
            }else if(s.contains("null")){
                s = RED + s;
            }
            sb.append(s).append("\n").append(RESET);
        }
        return sb.toString();
    }
}
