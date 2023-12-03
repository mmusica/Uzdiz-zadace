package org.foi.uzdiz.mmusica.data_extractor;

import org.foi.uzdiz.mmusica.data_reader.DataReader;

public abstract class DataExtractor {
     public void extractData(){
          DataReader dataReader = createDataReader();
          try {
               dataReader.saveData();
          }catch (RuntimeException exception){
               throw new RuntimeException(exception.getMessage());
          }
     }
     public abstract DataReader createDataReader();
}
