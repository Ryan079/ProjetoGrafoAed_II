import Algoritmo.AlgoritmoDijkstra;
import Algoritmo.AlgoritmoVizinhoMaisProximo;
import Grafo.GrafoLogistica;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GrafoLogistica grafo = new GrafoLogistica();

        grafo.adicionarCidade("Recife");
        grafo.adicionarCidade("Caruaru");
        grafo.adicionarCidade("Garanhuns");
        grafo.adicionarCidade("Petrolina");

        grafo.adicionarEstrada("Recife", "Caruaru", 130);
        grafo.adicionarEstrada("Caruaru", "Garanhuns", 100);
        grafo.adicionarEstrada("Recife", "Garanhuns", 200);
        grafo.adicionarEstrada("Garanhuns", "Petrolina", 400);

        grafo.exibirMapa();

        AlgoritmoDijkstra.encontrarMenorRota(grafo, "Recife", "Petrolina");

        try {
            List<String> viagem = AlgoritmoVizinhoMaisProximo.calcularRotaMaisProxima(grafo, "Recife");

            System.out.println("Rota otimizada:");
            System.out.println(String.join(" -> ", viagem));
        } catch (IllegalArgumentException ex) {
            System.err.println("Erro: " + ex.getMessage());
        }


    }
}