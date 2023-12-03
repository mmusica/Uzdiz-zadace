package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.factory.DataSaver;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageTypeSaver extends DataSaver<PackageType> {
    private static final int OZNAKA = 0;
    private static final int OPIS = 1;
    private static final int VISINA = 2;
    private static final int SIRINA = 3;
    private static final int DUZINA = 4;
    private static final int MAX_TEZINA = 5;
    private static final int CIJENA = 6;
    private static final int CIJENA_HITNO = 7;
    private static final int CIJENA_P = 8;
    private static final int CIJENA_T = 9;
    private static final int NUMBER_OF_ARGS = 10;

    @Override
    public List<PackageType> createDataList() {
        List<String[]> attributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getVrstaPaketaDokument());
        List<PackageType> packageTypes = new ArrayList<>();
        final int[] counter = {2};
        attributes.forEach(a -> {
            try {
                PackageType packageType = createPackageType(a, counter[0]);
                if (packageType != null) {
                    packageTypes.add(packageType);
                }
                counter[0]++;
            } catch (Exception e) {
                TerminalCommandHandler.getInstance().handleError(a, "Tip Paketa: tekst umjesto broja");
            }
        });
        return packageTypes;
    }
    private PackageType createPackageType(String[] a, int counter) {
        PackageType packageType = null;
        if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
            TerminalCommandHandler.getInstance().handleError(a,"Neispravan broj argumenata u redu %d".formatted(counter));
            return null;
        }
        packageType = new PackageType(
                a[OZNAKA], a[OPIS], Double.parseDouble(a[VISINA].replace(',', '.').trim()),
                Double.parseDouble(a[SIRINA].replace(',', '.').trim())
                , Double.parseDouble(a[DUZINA].replace(',', '.').trim()),
                Double.parseDouble(a[MAX_TEZINA].replace(',', '.').trim()),
                new BigDecimal(a[CIJENA].replace(',', '.').trim()), new BigDecimal(a[CIJENA_HITNO].replace(',', '.').trim()),
                new BigDecimal(a[CIJENA_P].replace(',', '.').trim()), new BigDecimal(a[CIJENA_T].replace(',', '.').trim()));

        return packageType;
    }
}
