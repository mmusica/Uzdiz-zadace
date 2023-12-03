package org.foi.uzdiz.mmusica.data_extractor;

import org.foi.uzdiz.mmusica.data_reader.DataReader;
import org.foi.uzdiz.mmusica.data_reader.PackageDataReader;

public class PackageDataExtractor extends DataExtractor{
    @Override
    public DataReader createDataReader() {
        return new PackageDataReader();
    }
}
