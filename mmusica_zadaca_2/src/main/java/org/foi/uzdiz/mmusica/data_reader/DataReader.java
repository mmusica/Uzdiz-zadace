package org.foi.uzdiz.mmusica.data_reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class DataReader {

    private static final String OZNAKA = "OZNAKA";
    private static final String REGISTRACIJA = "REGISTRACIJA";
    public List<String[]> readDataFromFile(String fileName){
        List<String[]> listOfAttributes = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {
            String instructionLine = br.readLine();
            if(!isInstructionInFirstLine(instructionLine)) throw new RuntimeException("Nema prvi informativni redak");
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");
                line = br.readLine();
                listOfAttributes.add(attributes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listOfAttributes;
    }

    private boolean isInstructionInFirstLine(String instructionLine) {
        String[] attributes = instructionLine.split(";");
        return attributes[0].equalsIgnoreCase(OZNAKA) || attributes[0].equalsIgnoreCase(REGISTRACIJA);
    }

    public abstract void saveData();
}
