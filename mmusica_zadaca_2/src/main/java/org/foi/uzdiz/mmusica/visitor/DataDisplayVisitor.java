package org.foi.uzdiz.mmusica.visitor;

import org.foi.uzdiz.mmusica.model.Vehicle;

public interface DataDisplayVisitor {
    void visitVehicle(Vehicle vehicle);
}
