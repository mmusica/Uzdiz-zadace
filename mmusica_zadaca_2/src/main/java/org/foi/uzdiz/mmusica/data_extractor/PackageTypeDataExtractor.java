package org.foi.uzdiz.mmusica.data_extractor;

import org.foi.uzdiz.mmusica.data_reader.DataReader;
import org.foi.uzdiz.mmusica.data_reader.PackageTypeDataReader;

public class PackageTypeDataExtractor extends DataExtractor{
    @Override
    public DataReader createDataReader() {
        return new PackageTypeDataReader();
    }
}
