package ru.dgp.services;

import java.util.List;
import ru.dgp.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
