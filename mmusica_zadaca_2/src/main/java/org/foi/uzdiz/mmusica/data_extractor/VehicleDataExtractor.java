package org.foi.uzdiz.mmusica.data_extractor;

import org.foi.uzdiz.mmusica.data_reader.DataReader;
import org.foi.uzdiz.mmusica.data_reader.VehicleDataReader;

public class VehicleDataExtractor extends DataExtractor{
    @Override
    public DataReader createDataReader() {
        return new VehicleDataReader();
    }
}
