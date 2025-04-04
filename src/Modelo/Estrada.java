package Modelo;

public class Estrada {
    private Cidade origem;
    private Cidade destino;
    private double distancia; //peso

    public Estrada(Cidade destino, Cidade origem, double distancia) {
        this.destino = destino;
        this.distancia = distancia;
        this.origem = origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public Cidade getOrigem() {
        return origem;
    }

    @Override
    public String toString() {
        return origem.getNome() + " -> " + destino.getNome() + " (" + distancia + " km)";
    }
}
