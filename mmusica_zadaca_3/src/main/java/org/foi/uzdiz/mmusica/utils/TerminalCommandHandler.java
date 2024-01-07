package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.voznja.GPS;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TerminalCommandHandler {
    private static final TerminalCommandHandler INSTANCE;
    private static final String REGEX_STRING = "--vs\\s*'?(?<vs>\\d{2}\\.\\d{2}\\.\\d{4}\\.\\s?\\d{2}:\\d{2}:\\d{2})'?" +
            "|--ms\\s+(?<ms>\\d+)|--pr\\s*'?(?<pr>\\d{2}:\\d{2})'?" +
            "|--kr\\s*'?(?<kr>\\d{2}:\\d{2})'?" +
            "|--mt\\s+(?<mt>\\d+)|--vi\\s+(?<vi>\\d+)|--pp\\s*'?(?<pp>[^\\s]+)'?" +
            "|--pv\\s*'?(?<pv>[^\\s]+)'?|--vp\\s+(?<vp>[^\\s]+)";

    private static final String BAD_REGEX_STRING = "(--ms \\d+\\.|--ms \\d+\\,)|(--mt \\d+\\.|--mt \\d+\\,)|(--vi \\d+\\.|--vi \\d+\\,)";

    private String vrstaPaketaDokument;
    private String popisVozilaDokument;
    private String prijemPaketaDokument;
    private long mnoziteljSekunde;
    private LocalDateTime virtualniSat;
    private int vrijemeIsporuke;
    private LocalTime krajRada;
    private LocalTime pocetakRada;
    private int maxTezina;
    private static final String COMMAND_NUMBER_ERROR_MSG = "Komanda nema dobar broj argumenata";
    private static final String COMMAND_FORMAT_ERROR_MSG = "Komanda nije dobrog formata";
    private boolean[] validCommand = new boolean[9];
    private int errorCount = 0;
    private Properties newProperties = new Properties();

    static {
        INSTANCE = new TerminalCommandHandler();
        for (int i = 0; i < 9; i++) {
            INSTANCE.validCommand[i] = false;
        }
    }

    private TerminalCommandHandler() {
    }

    public String getCroDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return INSTANCE.getVirtualniSat().format(formatter);
    }

    public GPS getOfficeGps(){
        String gps = (String) this.getNewProperties().get("gps");
        String[] latLon = gps.split(",");
        double lat = Double.parseDouble(latLon[0].trim());
        double lon = Double.parseDouble(latLon[1].trim());
        return new GPS(lat,lon);
    }
    public static TerminalCommandHandler getInstance() {
        return INSTANCE;
    }

    public void setCommandData(String komanda) {
        if (checkForBadRegex(komanda)) throw new RuntimeException(COMMAND_FORMAT_ERROR_MSG);
        Pattern pattern = Pattern.compile(REGEX_STRING, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(komanda);
        int numberOfMatches = 0;
        while (matcher.find()) {
            numberOfMatches++;
            for (int i = 0; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                    setIndividualCommandValue(i, matcher);
                }
            }
        }
        if (numberOfMatches != 9) {
            throw new RuntimeException(COMMAND_NUMBER_ERROR_MSG);
        }
        for (int i = 0; i < 9; i++) {
            if (!validCommand[i]) throw new RuntimeException(COMMAND_FORMAT_ERROR_MSG);
        }
    }

    private boolean checkForBadRegex(String komanda) {
        Pattern pattern = Pattern.compile(BAD_REGEX_STRING, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(komanda);
        return matcher.find();
    }

    public void handleError(String[] a, String errorDesc) {
        this.errorCount++;
        Logger.getGlobal().log(Level.SEVERE, "%s %s, Redni broj greske: %d".formatted(errorDesc, Arrays.toString(a), this.errorCount));
    }

    private void setIndividualCommandValue(int i, Matcher matcher) {
        switch (i) {
            case 1: {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
                virtualniSat = LocalDateTime.parse(matcher.group(i).trim(), formatter);
                validCommand[i - 1] = true;
                break;
            }
            case 2: {
                mnoziteljSekunde = Long.decode(matcher.group(i).trim());
                validCommand[i - 1] = true;
                break;
            }
            case 3: {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                pocetakRada = LocalTime.parse(matcher.group(i).trim(), formatter);
                validCommand[i - 1] = true;
                break;
            }
            case 4: {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                krajRada = LocalTime.parse(matcher.group(i).trim(), formatter);
                validCommand[i - 1] = true;
                break;
            }
            case 5: {
                maxTezina = Integer.parseInt(matcher.group(i).trim());
                validCommand[i - 1] = true;
                break;
            }
            case 6: {
                vrijemeIsporuke = Integer.parseInt(matcher.group(i).trim());
                validCommand[i - 1] = true;
                break;
            }
            case 7: {
                prijemPaketaDokument = matcher.group(i).trim();
                validCommand[i - 1] = true;
                break;
            }
            case 8: {
                popisVozilaDokument = matcher.group(i).trim();
                validCommand[i - 1] = true;
                break;
            }
            case 9: {
                vrstaPaketaDokument = matcher.group(i).trim();
                validCommand[i - 1] = true;
                break;
            }
            default: {
                break;
            }
        }
    }

    public String getVrstaPaketaDokument() {
        return vrstaPaketaDokument;
    }

    public void setVrstaPaketaDokument(String vrstaPaketaDokument) {
        this.vrstaPaketaDokument = vrstaPaketaDokument;
    }

    public String getPopisVozilaDokument() {
        return popisVozilaDokument;
    }

    public void setPopisVozilaDokument(String popisVozilaDokument) {
        this.popisVozilaDokument = popisVozilaDokument;
    }

    public String getPrijemPaketaDokument() {
        return prijemPaketaDokument;
    }

    public void setPrijemPaketaDokument(String prijemPaketaDokument) {
        this.prijemPaketaDokument = prijemPaketaDokument;
    }

    public long getMnoziteljSekunde() {
        return mnoziteljSekunde;
    }

    public void setMnoziteljSekunde(long mnoziteljSekunde) {
        this.mnoziteljSekunde = mnoziteljSekunde;
    }

    public LocalDateTime getVirtualniSat() {
        return virtualniSat;
    }

    public void setVirtualniSat(LocalDateTime virtualniSat) {
        this.virtualniSat = virtualniSat;
    }

    public int getVrijemeIsporuke() {
        return vrijemeIsporuke;
    }

    public void setVrijemeIsporuke(int vrijemeIsporuke) {
        this.vrijemeIsporuke = vrijemeIsporuke;
    }

    public LocalTime getKrajRada() {
        return krajRada;
    }

    public void setKrajRada(LocalTime krajRada) {
        this.krajRada = krajRada;
    }

    public LocalTime getPocetakRada() {
        return pocetakRada;
    }

    public void setPocetakRada(LocalTime pocetakRada) {
        this.pocetakRada = pocetakRada;
    }

    public int getMaxTezina() {
        return maxTezina;
    }

    public void setMaxTezina(int maxTezina) {
        this.maxTezina = maxTezina;
    }

    public boolean[] getValidCommand() {
        return validCommand;
    }

    public void setValidCommand(boolean[] validCommand) {
        this.validCommand = validCommand;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public Properties getNewProperties() {
        return newProperties;
    }

    public void setNewProperties(Properties newProperties) {
        this.newProperties = newProperties;
    }

    public TerminalCommandHandlerMemento takeSnapshot(){
        return new TerminalCommandHandlerMemento(this.getVirtualniSat());
    }
    public void restore(TerminalCommandHandlerMemento memento){
        this.setVirtualniSat(memento.getVr());
    }
    public static class TerminalCommandHandlerMemento{
        private final LocalDateTime vr;

        public TerminalCommandHandlerMemento(LocalDateTime vr) {
            this.vr = vr;
        }

        private LocalDateTime getVr() {
            return vr;
        }
    }
}
