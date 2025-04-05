package Grafo;

import Modelo.Cidade;
import Modelo.Estrada;

import java.util.*;
import java.util.stream.Collectors;

public class AlgoritmoDijkstra {
    public static void encontrarMenorRota(GrafoLogistica grafo, String nomeOrigem, String nomeDestino) {
        Cidade origem = grafo.getMapaCidades().keySet().stream()
                .filter(c -> c.getNome().equals(nomeOrigem))
                .findFirst().orElse(null);

        Cidade destino = grafo.getMapaCidades().keySet().stream()
                .filter(c -> c.getNome().equals(nomeDestino))
                .findFirst().orElse(null);

        if(origem == null || destino == null)
            System.out.println("Cidades não encontradas");


        Map<Cidade, Double> distancias = new HashMap<>();
        Map<Cidade, Cidade> predecessores = new HashMap<>();
        PriorityQueue<Cidade> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for(Cidade cidade : grafo.getMapaCidades().keySet())
            distancias.put(cidade, Double.MAX_VALUE);

        distancias.put(origem, 0.0);
        fila.add(origem);

        while(!fila.isEmpty()) {
            Cidade atual = fila.poll();
            for(Estrada estrada : grafo.getMapaCidades().get(atual)) {
                Cidade vizinho = estrada.getDestino();
                double novaDistancia = distancias.get(atual) + estrada.getDistancia();

                if(novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        List<Cidade> caminho = new ArrayList<>();
        for(Cidade cidade = destino; cidade != null; cidade = predecessores.get(cidade))
            caminho.add(cidade);

        Collections.reverse(caminho);

        System.out.println("Melhor rota: " + caminho.stream()
                        .map(Cidade::getNome)
                        .collect(Collectors.joining(" -> ")));
        System.out.println("Distância total: " + distancias.get(destino));
    }
}
