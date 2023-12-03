package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSaver<T> {
    public List<String[]> readDataFromFile(String fileName){
        List<String[]> listOfAttributes = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {
            String instructionLine = br.readLine();
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
    public void repositorySave(Repository<T> repository){
        repository.saveAll(this.createDataList());
    }
    public abstract List<T> createDataList();
}
