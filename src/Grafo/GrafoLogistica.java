package Grafo;

import Modelo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoLogistica {
    private Map<Cidade, List<Estrada>> mapaCidades;

    public GrafoLogistica() {
        this.mapaCidades = new HashMap<>();
    }
}
