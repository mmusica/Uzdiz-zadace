package org.foi.uzdiz.mmusica.model.factory;

import org.foi.uzdiz.mmusica.builder.PackageBuildDirector;
import org.foi.uzdiz.mmusica.builder.PackageBuilderImpl;
import org.foi.uzdiz.mmusica.model.PackageType;
import org.foi.uzdiz.mmusica.model.Paket;
import org.foi.uzdiz.mmusica.model.factory.DataSaver;
import org.foi.uzdiz.mmusica.repository.Repository;
import org.foi.uzdiz.mmusica.repository.RepositoryManager;
import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageSaver extends DataSaver<Paket> {
    private static final int VRSTA_PAKETA = 4;
    private static final int NUMBER_OF_ARGS = 11;

    @Override
    public List<Paket> createDataList() {
        List<String[]> attributes = this.readDataFromFile(TerminalCommandHandler.getInstance().getPrijemPaketaDokument());
        final int[] counter = {2};
        List<Paket> pakets = new ArrayList<>();
        attributes.forEach(a -> {
            try {
                Paket paket = createPackage(a, counter[0]);
                if (paket != null) {
                    pakets.add(paket);
                }
                counter[0]++;

            } catch (Exception e) {
                TerminalCommandHandler.getInstance().handleError(a, "Paket: tekst umjesto broja");
            }
        });
        return pakets;
    }
    public void saveData() throws RuntimeException {

    }


    private Paket createPackage(String[] a, int counter) {
        Paket paket = null;
        if (Arrays.stream(a).count() != NUMBER_OF_ARGS) {
            TerminalCommandHandler.getInstance().handleError(a, "Paketi: Neispravan broj argumenata u redu %d".formatted(counter));
            return null;
        }
        if (typeOfPackageContains(a[VRSTA_PAKETA])) {
            PackageBuildDirector packageBuildDirector = new PackageBuildDirector(new PackageBuilderImpl());
            paket = packageBuildDirector.constructPackage(a);

        } else {
            TerminalCommandHandler.getInstance().handleError(a, "Ovakva vrsta paketa ne postoji: %s, red broj: %d".formatted(a[VRSTA_PAKETA], counter));
        }
        return paket;
    }

    public boolean typeOfPackageContains(String vrstaPaketa) {
        Repository<PackageType> packageTypeRepository = RepositoryManager.getINSTANCE().getPackageTypeRepository();
        List<PackageType> allPackageTypes = packageTypeRepository.getAll();
        for (PackageType pt : allPackageTypes) {
            if (pt.getOznaka().equals(vrstaPaketa)) {
                return true;
            }
        }
        return false;
    }

}
