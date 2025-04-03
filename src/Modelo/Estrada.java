package Modelo;

public class Estrada {
    private Cidade origem;
    private Cidade destino;
    private double distancia; //peso

    public Estrada(Cidade destino, double distancia, Cidade origem) {
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
        return "Estrada{" +
                "destino=" + destino +
                ", origem=" + origem +
                ", distancia=" + distancia +
                '}';
    }
}
